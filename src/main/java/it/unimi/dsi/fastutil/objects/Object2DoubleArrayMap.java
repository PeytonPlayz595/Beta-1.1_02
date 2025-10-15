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
/* Object/Reference-only definitions (values) */
/* Primitive-type-only definitions (values) */
/*		 
 * Copyright (C) 2007-2013 Sebastiano Vigna 
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
package it.unimi.dsi.fastutil.objects;

import java.util.Map;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.doubles.DoubleCollections;
import it.unimi.dsi.fastutil.doubles.DoubleArraySet;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;

/**
 * A simple, brute-force implementation of a map based on two parallel backing
 * arrays.
 * 
 * <p>
 * The main purpose of this implementation is that of wrapping cleanly the
 * brute-force approach to the storage of a very small number of pairs: just put
 * them into two parallel arrays and scan linearly to find an item.
 */
public class Object2DoubleArrayMap<K> extends AbstractObject2DoubleMap<K> implements java.io.Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	/** The keys (valid up to {@link #size}, excluded). */
	private transient Object[] key;
	/** The values (parallel to {@link #key}). */
	private transient double[] value;
	/** The number of valid entries in {@link #key} and {@link #value}. */
	private int size;

	/**
	 * Creates a new empty array map with given key and value backing arrays. The
	 * resulting map will have as many entries as the given arrays.
	 * 
	 * <p>
	 * It is responsibility of the caller that the elements of <code>key</code> are
	 * distinct.
	 * 
	 * @param key   the key array.
	 * @param value the value array (it <em>must</em> have the same length as
	 *              <code>key</code>).
	 */
	public Object2DoubleArrayMap(final Object[] key, final double[] value) {
		this.key = key;
		this.value = value;
		size = key.length;
		if (key.length != value.length)
			throw new IllegalArgumentException(
					"Keys and values have different lengths (" + key.length + ", " + value.length + ")");
	}

	/**
	 * Creates a new empty array map.
	 */
	public Object2DoubleArrayMap() {
		this.key = ObjectArrays.EMPTY_ARRAY;
		this.value = DoubleArrays.EMPTY_ARRAY;
	}

	/**
	 * Creates a new empty array map of given capacity.
	 *
	 * @param capacity the initial capacity.
	 */
	public Object2DoubleArrayMap(final int capacity) {
		this.key = new Object[capacity];
		this.value = new double[capacity];
	}

	/**
	 * Creates a new empty array map copying the entries of a given map.
	 *
	 * @param m a map.
	 */
	public Object2DoubleArrayMap(final Object2DoubleMap<K> m) {
		this(m.size());
		putAll(m);
	}

	/**
	 * Creates a new empty array map copying the entries of a given map.
	 *
	 * @param m a map.
	 */
	public Object2DoubleArrayMap(final Map<? extends K, ? extends Double> m) {
		this(m.size());
		putAll(m);
	}

	/**
	 * Creates a new array map with given key and value backing arrays, using the
	 * given number of elements.
	 * 
	 * <p>
	 * It is responsibility of the caller that the first <code>size</code> elements
	 * of <code>key</code> are distinct.
	 * 
	 * @param key   the key array.
	 * @param value the value array (it <em>must</em> have the same length as
	 *              <code>key</code>).
	 * @param size  the number of valid elements in <code>key</code> and
	 *              <code>value</code>.
	 */
	public Object2DoubleArrayMap(final Object[] key, final double[] value, final int size) {
		this.key = key;
		this.value = value;
		this.size = size;
		if (key.length != value.length)
			throw new IllegalArgumentException(
					"Keys and values have different lengths (" + key.length + ", " + value.length + ")");
		if (size > key.length)
			throw new IllegalArgumentException("The provided size (" + size
					+ ") is larger than or equal to the backing-arrays size (" + key.length + ")");
	}

	private final class EntrySet extends AbstractObjectSet<Object2DoubleMap.Entry<K>> implements FastEntrySet<K> {
		@Override
		public ObjectIterator<Object2DoubleMap.Entry<K>> iterator() {
			return new AbstractObjectIterator<Object2DoubleMap.Entry<K>>() {
				int next = 0;

				public boolean hasNext() {
					return next < size;
				}

				@SuppressWarnings("unchecked")
				public Entry<K> next() {
					if (!hasNext())
						throw new NoSuchElementException();
					return new AbstractObject2DoubleMap.BasicEntry<K>((K) key[next], value[next++]);
				}
			};
		}

		public ObjectIterator<Object2DoubleMap.Entry<K>> fastIterator() {
			return new AbstractObjectIterator<Object2DoubleMap.Entry<K>>() {
				int next = 0;
				final BasicEntry<K> entry = new BasicEntry<K>((null), (0));

				public boolean hasNext() {
					return next < size;
				}

				@SuppressWarnings("unchecked")
				public Entry<K> next() {
					if (!hasNext())
						throw new NoSuchElementException();
					entry.key = (K) key[next];
					entry.value = value[next++];
					return entry;
				}
			};
		}

		public int size() {
			return size;
		}

		@SuppressWarnings("unchecked")
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			final Map.Entry<K, Double> e = (Map.Entry<K, Double>) o;
			final K k = (e.getKey());
			return Object2DoubleArrayMap.this.containsKey(k)
					&& ((Object2DoubleArrayMap.this.getDouble(k)) == (((e.getValue()).doubleValue())));
		}
	}

	public FastEntrySet<K> object2DoubleEntrySet() {
		return new EntrySet();
	}

	private int findKey(final Object k) {
		final Object[] key = this.key;
		for (int i = size; i-- != 0;)
			if (((key[i]) == null ? (k) == null : (key[i]).equals(k)))
				return i;
		return -1;
	}


	public double getDouble(final Object k) {

		final Object[] key = this.key;
		for (int i = size; i-- != 0;)
			if (((key[i]) == null ? (k) == null : (key[i]).equals(k)))
				return value[i];
		return defRetValue;
	}

	public int size() {
		return size;
	}

	@Override
	public void clear() {

		for (int i = size; i-- != 0;) {

			key[i] = null;

		}

		size = 0;
	}

	@Override
	public boolean containsKey(final Object k) {
		return findKey(k) != -1;
	}

	@Override
	public boolean containsValue(double v) {
		for (int i = size; i-- != 0;)
			if (((value[i]) == (v)))
				return true;
		return false;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public double put(K k, double v) {
		final int oldKey = findKey(k);
		if (oldKey != -1) {
			final double oldValue = value[oldKey];
			value[oldKey] = v;
			return oldValue;
		}
		if (size == key.length) {
			final Object[] newKey = new Object[size == 0 ? 2 : size * 2];
			final double[] newValue = new double[size == 0 ? 2 : size * 2];
			for (int i = size; i-- != 0;) {
				newKey[i] = key[i];
				newValue[i] = value[i];
			}
			key = newKey;
			value = newValue;
		}
		key[size] = k;
		value[size] = v;
		size++;
		return defRetValue;
	}

	@Override

	public double removeDouble(final Object k) {

		final int oldPos = findKey(k);
		if (oldPos == -1)
			return defRetValue;
		final double oldValue = value[oldPos];
		final int tail = size - oldPos - 1;
		for (int i = 0; i < tail; i++) {
			key[oldPos + i] = key[oldPos + i + 1];
			value[oldPos + i] = value[oldPos + i + 1];
		}
		size--;

		key[size] = null;

		return oldValue;
	}

	@Override

	public ObjectSet<K> keySet() {
		return new ObjectArraySet<K>(key, size);
	}

	@Override
	public DoubleCollection values() {
		return DoubleCollections.unmodifiable(new DoubleArraySet(value, size));
	}

	/**
	 * Returns a deep copy of this map.
	 *
	 * <P>
	 * This method performs a deep copy of this hash map; the data stored in the
	 * map, however, is not cloned. Note that this makes a difference only for
	 * object keys.
	 *
	 * @return a deep copy of this map.
	 */

	@SuppressWarnings("unchecked")
	public Object2DoubleArrayMap<K> clone() {
		Object2DoubleArrayMap<K> c;
		try {
			c = (Object2DoubleArrayMap<K>) super.clone();
		} catch (CloneNotSupportedException cantHappen) {
			throw new InternalError();
		}
		c.key = key.clone();
		c.value = value.clone();
		return c;
	}

	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
		s.defaultWriteObject();
		for (int i = 0; i < size; i++) {
			s.writeObject(key[i]);
			s.writeDouble(value[i]);
		}
	}

	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
		s.defaultReadObject();
		key = new Object[size];
		value = new double[size];
		for (int i = 0; i < size; i++) {
			key[i] = s.readObject();
			value[i] = s.readDouble();
		}
	}
}