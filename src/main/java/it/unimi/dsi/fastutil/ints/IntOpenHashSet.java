/* Generic definitions */

/* Assertions (useful to generate conditional code) */
/* Current type and class (and size, if applicable) */
/* Value methods */
/* Interfaces (keys) */
/* Interfaces (values) */
/* Abstract implementations (keys) */
/* Abstract implementations (values) */
/* Static containers (keys) */
/* Static containers (values) */
/* Implementations */
/* Synchronized wrappers */
/* Unmodifiable wrappers */
/* Other wrappers */
/* Methods (keys) */
/* Methods (values) */
/* Methods (keys/values) */
/* Methods that have special names depending on keys (but the special names depend on values) */
/* Equality */
/* Object/Reference-only definitions (keys) */
/* Primitive-type-only definitions (keys) */
/* Object/Reference-only definitions (values) */
/*		 
 * Copyright (C) 2002-2013 Sebastiano Vigna 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import static it.unimi.dsi.fastutil.HashCommon.arraySize;
import static it.unimi.dsi.fastutil.HashCommon.maxFill;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A type-specific hash set with with a fast, small-footprint implementation.
 *
 * <P>
 * Instances of this class use a hash table to represent a set. The table is
 * enlarged as needed by doubling its size when new entries are created, but it
 * is <em>never</em> made smaller (even on a {@link #clear()}). A family of
 * {@linkplain #trim() trimming methods} lets you control the size of the table;
 * this is particularly useful if you reuse instances of this class.
 *
 * <p>
 * <strong>Warning:</strong> The implementation of this class has significantly
 * changed in <code>fastutil</code> 6.1.0. Please read the comments about this
 * issue in the section &ldquo;Faster Hash Tables&rdquo; of the
 * <a href="../../../../../overview-summary.html">overview</a>.
 *
 * @see Hash
 * @see HashCommon
 */
public class IntOpenHashSet extends AbstractIntSet implements java.io.Serializable, Cloneable, Hash {
	private static final long serialVersionUID = 0L;
	private static final boolean ASSERTS = false;
	/** The array of keys. */
	protected transient int key[];
	/** The array telling whether a position is used. */
	protected transient boolean used[];
	/** The acceptable load factor. */
	protected final float f;
	/** The current table size. */
	protected transient int n;
	/**
	 * Threshold after which we rehash. It must be the table size times {@link #f}.
	 */
	protected transient int maxFill;
	/** The mask for wrapping a position counter. */
	protected transient int mask;
	/** Number of entries in the set. */
	protected int size;

	/**
	 * Creates a new hash set.
	 *
	 * <p>
	 * The actual table size will be the least power of two greater than
	 * <code>expected</code>/<code>f</code>.
	 *
	 * @param expected the expected number of elements in the hash set.
	 * @param f        the load factor.
	 */
	public IntOpenHashSet(final int expected, final float f) {
		if (f <= 0 || f > 1)
			throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
		if (expected < 0)
			throw new IllegalArgumentException("The expected number of elements must be nonnegative");
		this.f = f;
		n = arraySize(expected, f);
		mask = n - 1;
		maxFill = maxFill(n, f);
		key = new int[n];
		used = new boolean[n];
	}

	/**
	 * Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor.
	 *
	 * @param expected the expected number of elements in the hash set.
	 */
	public IntOpenHashSet(final int expected) {
		this(expected, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Creates a new hash set with initial expected
	 * {@link Hash#DEFAULT_INITIAL_SIZE} elements and
	 * {@link Hash#DEFAULT_LOAD_FACTOR} as load factor.
	 */
	public IntOpenHashSet() {
		this(DEFAULT_INITIAL_SIZE, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Creates a new hash set copying a given collection.
	 *
	 * @param c a {@link Collection} to be copied into the new hash set.
	 * @param f the load factor.
	 */
	public IntOpenHashSet(final Collection<? extends Integer> c, final float f) {
		this(c.size(), f);
		addAll(c);
	}

	/**
	 * Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor
	 * copying a given collection.
	 *
	 * @param c a {@link Collection} to be copied into the new hash set.
	 */
	public IntOpenHashSet(final Collection<? extends Integer> c) {
		this(c, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Creates a new hash set copying a given type-specific collection.
	 *
	 * @param c a type-specific collection to be copied into the new hash set.
	 * @param f the load factor.
	 */
	public IntOpenHashSet(final IntCollection c, final float f) {
		this(c.size(), f);
		addAll(c);
	}

	/**
	 * Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor
	 * copying a given type-specific collection.
	 *
	 * @param c a type-specific collection to be copied into the new hash set.
	 */
	public IntOpenHashSet(final IntCollection c) {
		this(c, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Creates a new hash set using elements provided by a type-specific iterator.
	 *
	 * @param i a type-specific iterator whose elements will fill the set.
	 * @param f the load factor.
	 */
	public IntOpenHashSet(final IntIterator i, final float f) {
		this(DEFAULT_INITIAL_SIZE, f);
		while (i.hasNext())
			add(i.nextInt());
	}

	/**
	 * Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor
	 * using elements provided by a type-specific iterator.
	 *
	 * @param i a type-specific iterator whose elements will fill the set.
	 */
	public IntOpenHashSet(final IntIterator i) {
		this(i, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Creates a new hash set using elements provided by an iterator.
	 *
	 * @param i an iterator whose elements will fill the set.
	 * @param f the load factor.
	 */
	public IntOpenHashSet(final Iterator<?> i, final float f) {
		this(IntIterators.asIntIterator(i), f);
	}

	/**
	 * Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor
	 * using elements provided by an iterator.
	 *
	 * @param i an iterator whose elements will fill the set.
	 */
	public IntOpenHashSet(final Iterator<?> i) {
		this(IntIterators.asIntIterator(i));
	}

	/**
	 * Creates a new hash set and fills it with the elements of a given array.
	 *
	 * @param a      an array whose elements will be used to fill the set.
	 * @param offset the first element to use.
	 * @param length the number of elements to use.
	 * @param f      the load factor.
	 */
	public IntOpenHashSet(final int[] a, final int offset, final int length, final float f) {
		this(length < 0 ? 0 : length, f);
		IntArrays.ensureOffsetLength(a, offset, length);
		for (int i = 0; i < length; i++)
			add(a[offset + i]);
	}

	/**
	 * Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor
	 * and fills it with the elements of a given array.
	 *
	 * @param a      an array whose elements will be used to fill the set.
	 * @param offset the first element to use.
	 * @param length the number of elements to use.
	 */
	public IntOpenHashSet(final int[] a, final int offset, final int length) {
		this(a, offset, length, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Creates a new hash set copying the elements of an array.
	 *
	 * @param a an array to be copied into the new hash set.
	 * @param f the load factor.
	 */
	public IntOpenHashSet(final int[] a, final float f) {
		this(a, 0, a.length, f);
	}

	/**
	 * Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor
	 * copying the elements of an array.
	 *
	 * @param a an array to be copied into the new hash set.
	 */
	public IntOpenHashSet(final int[] a) {
		this(a, DEFAULT_LOAD_FACTOR);
	}

	/*
	 * The following methods implements some basic building blocks used by all
	 * accessors. They are (and should be maintained) identical to those used in
	 * HashMap.drv.
	 */
	public boolean add(final int k) {
		// The starting point.
		int pos = (it.unimi.dsi.fastutil.HashCommon.murmurHash3((k))) & mask;
		// There's always an unused entry.
		while (used[pos]) {
			if (((key[pos]) == (k)))
				return false;
			pos = (pos + 1) & mask;
		}
		used[pos] = true;
		key[pos] = k;
		if (++size >= maxFill)
			rehash(arraySize(size + 1, f));
		if (ASSERTS)
			checkTable();
		return true;
	}

	/**
	 * Shifts left entries with the specified hash code, starting at the specified
	 * position, and empties the resulting free entry.
	 *
	 * @param pos a starting position.
	 * @return the position cleared by the shifting process.
	 */
	protected final int shiftKeys(int pos) {
		// Shift entries with the same hash.
		int last, slot;
		for (;;) {
			pos = ((last = pos) + 1) & mask;
			while (used[pos]) {
				slot = (it.unimi.dsi.fastutil.HashCommon.murmurHash3((key[pos]))) & mask;
				if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos)
					break;
				pos = (pos + 1) & mask;
			}
			if (!used[pos])
				break;
			key[last] = key[pos];
		}
		used[last] = false;
		return last;
	}

	public boolean remove(final int k) {
		// The starting point.
		int pos = (it.unimi.dsi.fastutil.HashCommon.murmurHash3((k))) & mask;
		// There's always an unused entry.
		while (used[pos]) {
			if (((key[pos]) == (k))) {
				size--;
				shiftKeys(pos);
				if (ASSERTS)
					checkTable();
				return true;
			}
			pos = (pos + 1) & mask;
		}
		return false;
	}

	public boolean contains(final int k) {
		// The starting point.
		int pos = (it.unimi.dsi.fastutil.HashCommon.murmurHash3((k))) & mask;
		// There's always an unused entry.
		while (used[pos]) {
			if (((key[pos]) == (k)))
				return true;
			pos = (pos + 1) & mask;
		}
		return false;
	}

	/*
	 * Removes all elements from this set.
	 *
	 * <P>To increase object reuse, this method does not change the table size. If
	 * you want to reduce the table size, you must use {@link #trim()}.
	 *
	 */
	public void clear() {
		if (size == 0)
			return;
		size = 0;
		BooleanArrays.fill(used, false);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * A no-op for backward compatibility.
	 * 
	 * @param growthFactor unused.
	 * @deprecated Since <code>fastutil</code> 6.1.0, hash tables are doubled when
	 *             they are too full.
	 */
	@Deprecated
	public void growthFactor(int growthFactor) {
	}

	/**
	 * Gets the growth factor (2).
	 *
	 * @return the growth factor of this set, which is fixed (2).
	 * @see #growthFactor(int)
	 * @deprecated Since <code>fastutil</code> 6.1.0, hash tables are doubled when
	 *             they are too full.
	 */
	@Deprecated
	public int growthFactor() {
		return 16;
	}

	/** An iterator over a hash set. */
	private class SetIterator extends AbstractIntIterator {
		/**
		 * The index of the next entry to be returned, if positive or zero. If negative,
		 * the next entry to be returned, if any, is that of index -pos -2 from the
		 * {@link #wrapped} list.
		 */
		int pos = IntOpenHashSet.this.n;
		/**
		 * The index of the last entry that has been returned (more precisely, the value
		 * of {@link #pos}). It is -1 if either we did not return an entry yet, or the
		 * last returned entry has been removed.
		 */
		int last = -1;
		/** A downward counter measuring how many entries must still be returned. */
		int c = size;
		/**
		 * A lazily allocated list containing elements that have wrapped around the
		 * table because of removals; such elements would not be enumerated (other
		 * elements would be usually enumerated twice in their place).
		 */
		IntArrayList wrapped;
		{
			final boolean used[] = IntOpenHashSet.this.used;
			if (c != 0)
				while (!used[--pos])
					;
		}

		public boolean hasNext() {
			return c != 0;
		}

		public int nextInt() {
			if (!hasNext())
				throw new NoSuchElementException();
			c--;
			// We are just enumerating elements from the wrapped list.
			if (pos < 0)
				return wrapped.getInt(-(last = --pos) - 2);
			final int retVal = key[last = pos];
			// System.err.println( "Count: " + c );
			if (c != 0) {
				final boolean used[] = IntOpenHashSet.this.used;
				while (pos-- != 0 && !used[pos])
					;
				// When here pos < 0 there are no more elements to be enumerated by scanning,
				// but wrapped might be nonempty.
			}
			return retVal;
		}

		/**
		 * Shifts left entries with the specified hash code, starting at the specified
		 * position, and empties the resulting free entry. If any entry wraps around the
		 * table, instantiates lazily {@link #wrapped} and stores the entry.
		 *
		 * @param pos a starting position.
		 * @return the position cleared by the shifting process.
		 */
		final int shiftKeys(int pos) {
			// Shift entries with the same hash.
			int last, slot;
			for (;;) {
				pos = ((last = pos) + 1) & mask;
				while (used[pos]) {
					slot = (it.unimi.dsi.fastutil.HashCommon.murmurHash3((key[pos]))) & mask;
					if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos)
						break;
					pos = (pos + 1) & mask;
				}
				if (!used[pos])
					break;
				if (pos < last) {
					// Wrapped entry.
					if (wrapped == null)
						wrapped = new IntArrayList();
					wrapped.add(key[pos]);
				}
				key[last] = key[pos];
			}
			used[last] = false;
			return last;
		}

		public void remove() {
			if (last == -1)
				throw new IllegalStateException();
			if (pos < -1) {
				// We're removing wrapped entries.
				IntOpenHashSet.this.remove(wrapped.getInt(-pos - 2));
				last = -1;
				return;
			}
			size--;
			if (shiftKeys(last) == pos && c > 0) {
				c++;
				nextInt();
			}
			last = -1; // You can no longer remove this entry.
			if (ASSERTS)
				checkTable();
		}
	}

	public IntIterator iterator() {
		return new SetIterator();
	}

	/**
	 * A no-op for backward compatibility. The kind of tables implemented by this
	 * class never need rehashing.
	 *
	 * <P>
	 * If you need to reduce the table size to fit exactly this set, use
	 * {@link #trim()}.
	 *
	 * @return true.
	 * @see #trim()
	 * @deprecated A no-op.
	 */
	@Deprecated
	public boolean rehash() {
		return true;
	}

	/**
	 * Rehashes this set, making the table as small as possible.
	 * 
	 * <P>
	 * This method rehashes the table to the smallest size satisfying the load
	 * factor. It can be used when the set will not be changed anymore, so to
	 * optimize access speed and size.
	 *
	 * <P>
	 * If the table size is already the minimum possible, this method does nothing.
	 *
	 * @return true if there was enough memory to trim the set.
	 * @see #trim(int)
	 */
	public boolean trim() {
		final int l = arraySize(size, f);
		if (l >= n)
			return true;
		try {
			rehash(l);
		} catch (OutOfMemoryError cantDoIt) {
			return false;
		}
		return true;
	}

	/**
	 * Rehashes this set if the table is too large.
	 * 
	 * <P>
	 * Let <var>N</var> be the smallest table size that can hold
	 * <code>max(n,{@link #size()})</code> entries, still satisfying the load
	 * factor. If the current table size is smaller than or equal to <var>N</var>,
	 * this method does nothing. Otherwise, it rehashes this set in a table of size
	 * <var>N</var>.
	 *
	 * <P>
	 * This method is useful when reusing sets. {@linkplain #clear() Clearing a set}
	 * leaves the table size untouched. If you are reusing a set many times, you can
	 * call this method with a typical size to avoid keeping around a very large
	 * table just because of a few large transient sets.
	 *
	 * @param n the threshold for the trimming.
	 * @return true if there was enough memory to trim the set.
	 * @see #trim()
	 */
	public boolean trim(final int n) {
		final int l = HashCommon.nextPowerOfTwo((int) Math.ceil(n / f));
		if (this.n <= l)
			return true;
		try {
			rehash(l);
		} catch (OutOfMemoryError cantDoIt) {
			return false;
		}
		return true;
	}

	/**
	 * Resizes the set.
	 *
	 * <P>
	 * This method implements the basic rehashing strategy, and may be overriden by
	 * subclasses implementing different rehashing strategies (e.g., disk-based
	 * rehashing). However, you should not override this method unless you
	 * understand the internal workings of this class.
	 *
	 * @param newN the new size
	 */
	protected void rehash(final int newN) {
		int i = 0, pos;
		final boolean used[] = this.used;
		int k;
		final int key[] = this.key;
		final int newMask = newN - 1;
		final int newKey[] = new int[newN];
		final boolean newUsed[] = new boolean[newN];
		for (int j = size; j-- != 0;) {
			while (!used[i])
				i++;
			k = key[i];
			pos = (it.unimi.dsi.fastutil.HashCommon.murmurHash3((k))) & newMask;
			while (newUsed[pos])
				pos = (pos + 1) & newMask;
			newUsed[pos] = true;
			newKey[pos] = k;
			i++;
		}
		n = newN;
		mask = newMask;
		maxFill = maxFill(n, f);
		this.key = newKey;
		this.used = newUsed;
	}

	/**
	 * Returns a deep copy of this set.
	 *
	 * <P>
	 * This method performs a deep copy of this hash set; the data stored in the
	 * set, however, is not cloned. Note that this makes a difference only for
	 * object keys.
	 *
	 * @return a deep copy of this set.
	 */
	public IntOpenHashSet clone() {
		IntOpenHashSet c;
		try {
			c = (IntOpenHashSet) super.clone();
		} catch (CloneNotSupportedException cantHappen) {
			throw new InternalError();
		}
		c.key = key.clone();
		c.used = used.clone();
		return c;
	}

	/**
	 * Returns a hash code for this set.
	 *
	 * This method overrides the generic method provided by the superclass. Since
	 * <code>equals()</code> is not overriden, it is important that the value
	 * returned by this method is the same value as the one returned by the
	 * overriden method.
	 *
	 * @return a hash code for this set.
	 */
	public int hashCode() {
		int h = 0, i = 0, j = size;
		while (j-- != 0) {
			while (!used[i])
				i++;
			h += (key[i]);
			i++;
		}
		return h;
	}

	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
		final IntIterator i = iterator();
		s.defaultWriteObject();
		for (int j = size; j-- != 0;)
			s.writeInt(i.nextInt());
	}

	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
		s.defaultReadObject();
		n = arraySize(size, f);
		maxFill = maxFill(n, f);
		mask = n - 1;
		final int key[] = this.key = new int[n];
		final boolean used[] = this.used = new boolean[n];
		int k;
		for (int i = size, pos = 0; i-- != 0;) {
			k = s.readInt();
			pos = (it.unimi.dsi.fastutil.HashCommon.murmurHash3((k))) & mask;
			while (used[pos])
				pos = (pos + 1) & mask;
			used[pos] = true;
			key[pos] = k;
		}
		if (ASSERTS)
			checkTable();
	}

	private void checkTable() {
	}
}