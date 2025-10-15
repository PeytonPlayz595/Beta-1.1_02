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
package it.unimi.dsi.fastutil.doubles;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A class providing static methods and objects that do useful things with
 * type-specific iterators.
 *
 * @see Iterator
 */
public class DoubleIterators {
	private DoubleIterators() {
	}

	/**
	 * A class returning no elements and a type-specific iterator interface.
	 *
	 * <P>
	 * This class may be useful to implement your own in case you subclass a
	 * type-specific iterator.
	 */
	public static class EmptyIterator extends AbstractDoubleListIterator implements java.io.Serializable, Cloneable {
		private static final long serialVersionUID = -7046029254386353129L;

		protected EmptyIterator() {
		}

		public boolean hasNext() {
			return false;
		}

		public boolean hasPrevious() {
			return false;
		}

		public double nextDouble() {
			throw new NoSuchElementException();
		}

		public double previousDouble() {
			throw new NoSuchElementException();
		}

		public int nextIndex() {
			return 0;
		}

		public int previousIndex() {
			return -1;
		}

		public int skip(int n) {
			return 0;
		};

		public int back(int n) {
			return 0;
		};

		public Object clone() {
			return EMPTY_ITERATOR;
		}

		private Object readResolve() {
			return EMPTY_ITERATOR;
		}
	}

	/**
	 * An empty iterator (immutable). It is serializable and cloneable.
	 *
	 * <P>
	 * The class of this objects represent an abstract empty iterator that can
	 * iterate as a type-specific (list) iterator.
	 */
	public final static EmptyIterator EMPTY_ITERATOR = new EmptyIterator();

	/** An iterator returning a single element. */
	private static class SingletonIterator extends AbstractDoubleListIterator {
		private final double element;
		private int curr;

		public SingletonIterator(final double element) {
			this.element = element;
		}

		public boolean hasNext() {
			return curr == 0;
		}

		public boolean hasPrevious() {
			return curr == 1;
		}

		public double nextDouble() {
			if (!hasNext())
				throw new NoSuchElementException();
			curr = 1;
			return element;
		}

		public double previousDouble() {
			if (!hasPrevious())
				throw new NoSuchElementException();
			curr = 0;
			return element;
		}

		public int nextIndex() {
			return curr;
		}

		public int previousIndex() {
			return curr - 1;
		}
	}

	/**
	 * Returns an iterator that iterates just over the given element.
	 *
	 * @param element the only element to be returned by a type-specific list
	 *                iterator.
	 * @return an iterator that iterates just over <code>element</code>.
	 */
	public static DoubleListIterator singleton(final double element) {
		return new SingletonIterator(element);
	}

	/** A class to wrap arrays in iterators. */

	private static class ArrayIterator extends AbstractDoubleListIterator {
		private final double[] array;
		private final int offset, length;
		private int curr;

		public ArrayIterator(final double[] array, final int offset, final int length) {
			this.array = array;
			this.offset = offset;
			this.length = length;
		}

		public boolean hasNext() {
			return curr < length;
		}

		public boolean hasPrevious() {
			return curr > 0;
		}

		public double nextDouble() {
			if (!hasNext())
				throw new NoSuchElementException();
			return array[offset + curr++];
		}

		public double previousDouble() {
			if (!hasPrevious())
				throw new NoSuchElementException();
			return array[offset + --curr];
		}

		public int skip(int n) {
			if (n <= length - curr) {
				curr += n;
				return n;
			}
			n = length - curr;
			curr = length;
			return n;
		}

		public int back(int n) {
			if (n <= curr) {
				curr -= n;
				return n;
			}
			n = curr;
			curr = 0;
			return n;
		}

		public int nextIndex() {
			return curr;
		}

		public int previousIndex() {
			return curr - 1;
		}
	}

	/**
	 * Wraps the given part of an array into a type-specific list iterator.
	 *
	 * <P>
	 * The type-specific list iterator returned by this method will iterate
	 * <code>length</code> times, returning consecutive elements of the given array
	 * starting from the one with index <code>offset</code>.
	 *
	 * @param array  an array to wrap into a type-specific list iterator.
	 * @param offset the first element of the array to be returned.
	 * @param length the number of elements to return.
	 */
	public static DoubleListIterator wrap(final double[] array, final int offset, final int length) {
		DoubleArrays.ensureOffsetLength(array, offset, length);
		return new ArrayIterator(array, offset, length);
	}

	/**
	 * Wraps the given array into a type-specific list iterator.
	 *
	 * <P>
	 * The type-specific list iterator returned by this method will return all
	 * elements of the given array.
	 *
	 * @param array an array to wrap into a type-specific list iterator.
	 */
	public static DoubleListIterator wrap(final double[] array) {
		return new ArrayIterator(array, 0, array.length);
	}

	/**
	 * Unwraps an iterator into an array starting at a given offset for a given
	 * number of elements.
	 *
	 * <P>
	 * This method iterates over the given type-specific iterator and stores the
	 * elements returned, up to a maximum of <code>length</code>, in the given array
	 * starting at <code>offset</code>. The number of actually unwrapped elements is
	 * returned (it may be less than <code>max</code> if the iterator emits less
	 * than <code>max</code> elements).
	 *
	 * @param i      a type-specific iterator.
	 * @param array  an array to contain the output of the iterator.
	 * @param offset the first element of the array to be returned.
	 * @param max    the maximum number of elements to unwrap.
	 * @return the number of elements unwrapped.
	 */
	public static int unwrap(final DoubleIterator i, final double array[], int offset, final int max) {
		if (max < 0)
			throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
		if (offset < 0 || offset + max > array.length)
			throw new IllegalArgumentException();
		int j = max;
		while (j-- != 0 && i.hasNext())
			array[offset++] = i.nextDouble();
		return max - j - 1;
	}

	/**
	 * Unwraps an iterator into an array.
	 *
	 * <P>
	 * This method iterates over the given type-specific iterator and stores the
	 * elements returned in the given array. The iteration will stop when the
	 * iterator has no more elements or when the end of the array has been reached.
	 *
	 * @param i     a type-specific iterator.
	 * @param array an array to contain the output of the iterator.
	 * @return the number of elements unwrapped.
	 */
	public static int unwrap(final DoubleIterator i, final double array[]) {
		return unwrap(i, array, 0, array.length);
	}

	/**
	 * Unwraps an iterator, returning an array, with a limit on the number of
	 * elements.
	 *
	 * <P>
	 * This method iterates over the given type-specific iterator and returns an
	 * array containing the elements returned by the iterator. At most
	 * <code>max</code> elements will be returned.
	 *
	 * @param i   a type-specific iterator.
	 * @param max the maximum number of elements to be unwrapped.
	 * @return an array containing the elements returned by the iterator (at most
	 *         <ocde>max</code>).
	 */
	public static double[] unwrap(final DoubleIterator i, int max) {
		if (max < 0)
			throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
		double array[] = new double[16];
		int j = 0;

		while (max-- != 0 && i.hasNext()) {
			if (j == array.length)
				array = DoubleArrays.grow(array, j + 1);
			array[j++] = i.nextDouble();
		}

		return DoubleArrays.trim(array, j);
	}

	/**
	 * Unwraps an iterator, returning an array.
	 *
	 * <P>
	 * This method iterates over the given type-specific iterator and returns an
	 * array containing the elements returned by the iterator.
	 *
	 * @param i a type-specific iterator.
	 * @return an array containing the elements returned by the iterator.
	 */

	public static double[] unwrap(final DoubleIterator i) {
		return unwrap(i, Integer.MAX_VALUE);
	}

	/**
	 * Unwraps an iterator into a type-specific collection, with a limit on the
	 * number of elements.
	 *
	 * <P>
	 * This method iterates over the given type-specific iterator and stores the
	 * elements returned, up to a maximum of <code>max</code>, in the given
	 * type-specific collection. The number of actually unwrapped elements is
	 * returned (it may be less than <code>max</code> if the iterator emits less
	 * than <code>max</code> elements).
	 *
	 * @param i   a type-specific iterator.
	 * @param c   a type-specific collection array to contain the output of the
	 *            iterator.
	 * @param max the maximum number of elements to unwrap.
	 * @return the number of elements unwrapped. Note that this is the number of
	 *         elements returned by the iterator, which is not necessarily the
	 *         number of elements that have been added to the collection (because of
	 *         duplicates).
	 */
	public static int unwrap(final DoubleIterator i, final DoubleCollection c, final int max) {
		if (max < 0)
			throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
		int j = max;
		while (j-- != 0 && i.hasNext())
			c.add(i.nextDouble());
		return max - j - 1;
	}

	/**
	 * Unwraps an iterator into a type-specific collection.
	 *
	 * <P>
	 * This method iterates over the given type-specific iterator and stores the
	 * elements returned in the given type-specific collection. The returned count
	 * on the number unwrapped elements is a long, so that it will work also with
	 * very large collections.
	 *
	 * @param i a type-specific iterator.
	 * @param c a type-specific collection to contain the output of the iterator.
	 * @return the number of elements unwrapped. Note that this is the number of
	 *         elements returned by the iterator, which is not necessarily the
	 *         number of elements that have been added to the collection (because of
	 *         duplicates).
	 */
	public static long unwrap(final DoubleIterator i, final DoubleCollection c) {
		long n = 0;
		while (i.hasNext()) {
			c.add(i.nextDouble());
			n++;
		}
		return n;
	}

	/**
	 * Pours an iterator into a type-specific collection, with a limit on the number
	 * of elements.
	 *
	 * <P>
	 * This method iterates over the given type-specific iterator and adds the
	 * returned elements to the given collection (up to <code>max</code>).
	 *
	 * @param i   a type-specific iterator.
	 * @param s   a type-specific collection.
	 * @param max the maximum number of elements to be poured.
	 * @return the number of elements poured. Note that this is the number of
	 *         elements returned by the iterator, which is not necessarily the
	 *         number of elements that have been added to the collection (because of
	 *         duplicates).
	 */

	public static int pour(final DoubleIterator i, final DoubleCollection s, final int max) {
		if (max < 0)
			throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
		int j = max;
		while (j-- != 0 && i.hasNext())
			s.add(i.nextDouble());
		return max - j - 1;
	}

	/**
	 * Pours an iterator into a type-specific collection.
	 *
	 * <P>
	 * This method iterates over the given type-specific iterator and adds the
	 * returned elements to the given collection.
	 *
	 * @param i a type-specific iterator.
	 * @param s a type-specific collection.
	 * @return the number of elements poured. Note that this is the number of
	 *         elements returned by the iterator, which is not necessarily the
	 *         number of elements that have been added to the collection (because of
	 *         duplicates).
	 */

	public static int pour(final DoubleIterator i, final DoubleCollection s) {
		return pour(i, s, Integer.MAX_VALUE);
	}

	/**
	 * Pours an iterator, returning a type-specific list, with a limit on the number
	 * of elements.
	 *
	 * <P>
	 * This method iterates over the given type-specific iterator and returns a
	 * type-specific list containing the returned elements (up to <code>max</code>).
	 * Iteration on the returned list is guaranteed to produce the elements in the
	 * same order in which they appeared in the iterator.
	 *
	 *
	 * @param i   a type-specific iterator.
	 * @param max the maximum number of elements to be poured.
	 * @return a type-specific list containing the returned elements, up to
	 *         <code>max</code>.
	 */

	public static DoubleList pour(final DoubleIterator i, int max) {
		final DoubleArrayList l = new DoubleArrayList();
		pour(i, l, max);
		l.trim();
		return l;
	}

	/**
	 * Pours an iterator, returning a type-specific list.
	 *
	 * <P>
	 * This method iterates over the given type-specific iterator and returns a list
	 * containing the returned elements. Iteration on the returned list is
	 * guaranteed to produce the elements in the same order in which they appeared
	 * in the iterator.
	 *
	 * @param i a type-specific iterator.
	 * @return a type-specific list containing the returned elements.
	 */

	public static DoubleList pour(final DoubleIterator i) {
		return pour(i, Integer.MAX_VALUE);
	}

	private static class IteratorWrapper extends AbstractDoubleIterator {
		final Iterator<Double> i;

		public IteratorWrapper(final Iterator<Double> i) {
			this.i = i;
		}

		public boolean hasNext() {
			return i.hasNext();
		}

		public void remove() {
			i.remove();
		}

		public double nextDouble() {
			return ((i.next()).doubleValue());
		}
	}

	/**
	 * Wraps a standard iterator into a type-specific iterator.
	 *
	 * <P>
	 * This method wraps a standard iterator into a type-specific one which will
	 * handle the type conversions for you. Of course, any attempt to wrap an
	 * iterator returning the instances of the wrong class will generate a
	 * {@link ClassCastException}. The returned iterator is backed by
	 * <code>i</code>: changes to one of the iterators will affect the other, too.
	 *
	 * <P>
	 * If <code>i</code> is already type-specific, it will returned and no new
	 * object will be generated.
	 *
	 * @param i an iterator.
	 * @return a type-specific iterator backed by <code>i</code>.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static DoubleIterator asDoubleIterator(final Iterator i) {
		if (i instanceof DoubleIterator)
			return (DoubleIterator) i;
		return new IteratorWrapper(i);
	}

	private static class ListIteratorWrapper extends AbstractDoubleListIterator {
		final ListIterator<Double> i;

		public ListIteratorWrapper(final ListIterator<Double> i) {
			this.i = i;
		}

		public boolean hasNext() {
			return i.hasNext();
		}

		public boolean hasPrevious() {
			return i.hasPrevious();
		}

		public int nextIndex() {
			return i.nextIndex();
		}

		public int previousIndex() {
			return i.previousIndex();
		}

		public void set(double k) {
			i.set((Double.valueOf(k)));
		}

		public void add(double k) {
			i.add((Double.valueOf(k)));
		}

		public void remove() {
			i.remove();
		}

		public double nextDouble() {
			return ((i.next()).doubleValue());
		}

		public double previousDouble() {
			return ((i.previous()).doubleValue());
		}
	}

	/**
	 * Wraps a standard list iterator into a type-specific list iterator.
	 *
	 * <P>
	 * This method wraps a standard list iterator into a type-specific one which
	 * will handle the type conversions for you. Of course, any attempt to wrap an
	 * iterator returning the instances of the wrong class will generate a
	 * {@link ClassCastException}. The returned iterator is backed by
	 * <code>i</code>: changes to one of the iterators will affect the other, too.
	 *
	 * <P>
	 * If <code>i</code> is already type-specific, it will returned and no new
	 * object will be generated.
	 *
	 * @param i a list iterator.
	 * @return a type-specific list iterator backed by <code>i</code>.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static DoubleListIterator asDoubleIterator(final ListIterator i) {
		if (i instanceof DoubleListIterator)
			return (DoubleListIterator) i;
		return new ListIteratorWrapper(i);
	}

	private static class IteratorConcatenator extends AbstractDoubleIterator {
		final DoubleIterator a[];
		int offset, length, lastOffset = -1;

		public IteratorConcatenator(final DoubleIterator a[], int offset, int length) {
			this.a = a;
			this.offset = offset;
			this.length = length;
			advance();
		}

		private void advance() {
			while (length != 0) {
				if (a[offset].hasNext())
					break;
				length--;
				offset++;
			}
			return;
		}

		public boolean hasNext() {
			return length > 0;
		}

		public double nextDouble() {
			if (!hasNext())
				throw new NoSuchElementException();
			double next = a[lastOffset = offset].nextDouble();
			advance();
			return next;
		}

		public void remove() {
			if (lastOffset == -1)
				throw new IllegalStateException();
			a[lastOffset].remove();
		}

		public int skip(int n) {
			lastOffset = -1;
			int skipped = 0;
			while (skipped < n && length != 0) {
				skipped += a[offset].skip(n - skipped);
				if (a[offset].hasNext())
					break;
				length--;
				offset++;
			}
			return skipped;
		}
	}

	/**
	 * Concatenates all iterators contained in an array.
	 *
	 * <P>
	 * This method returns an iterator that will enumerate in order the elements
	 * returned by all iterators contained in the given array.
	 *
	 * @param a an array of iterators.
	 * @return an iterator obtained by concatenation.
	 */
	public static DoubleIterator concat(final DoubleIterator a[]) {
		return concat(a, 0, a.length);
	}

	/**
	 * Concatenates a sequence of iterators contained in an array.
	 *
	 * <P>
	 * This method returns an iterator that will enumerate in order the elements
	 * returned by <code>a[ offset ]</code>, then those returned by
	 * <code>a[ offset + 1 ]</code>, and so on up to
	 * <code>a[ offset + length - 1 ]</code>.
	 *
	 * @param a      an array of iterators.
	 * @param offset the index of the first iterator to concatenate.
	 * @param length the number of iterators to concatenate.
	 * @return an iterator obtained by concatenation of <code>length</code> elements
	 *         of <code>a</code> starting at <code>offset</code>.
	 */
	public static DoubleIterator concat(final DoubleIterator a[], final int offset, final int length) {
		return new IteratorConcatenator(a, offset, length);
	}

	/** An unmodifiable wrapper class for iterators. */
	public static class UnmodifiableIterator extends AbstractDoubleIterator {
		final protected DoubleIterator i;

		public UnmodifiableIterator(final DoubleIterator i) {
			this.i = i;
		}

		public boolean hasNext() {
			return i.hasNext();
		}

		public double nextDouble() {
			return i.nextDouble();
		}

		public Double next() {
			return i.next();
		}
	}

	/**
	 * Returns an unmodifiable iterator backed by the specified iterator.
	 *
	 * @param i the iterator to be wrapped in an unmodifiable iterator.
	 * @return an unmodifiable view of the specified iterator.
	 */
	public static DoubleIterator unmodifiable(final DoubleIterator i) {
		return new UnmodifiableIterator(i);
	}

	/** An unmodifiable wrapper class for bidirectional iterators. */
	public static class UnmodifiableBidirectionalIterator extends AbstractDoubleBidirectionalIterator {
		final protected DoubleBidirectionalIterator i;

		public UnmodifiableBidirectionalIterator(final DoubleBidirectionalIterator i) {
			this.i = i;
		}

		public boolean hasNext() {
			return i.hasNext();
		}

		public boolean hasPrevious() {
			return i.hasPrevious();
		}

		public double nextDouble() {
			return i.nextDouble();
		}

		public double previousDouble() {
			return i.previousDouble();
		}

		public Double next() {
			return i.next();
		}

		public Double previous() {
			return i.previous();
		}
	}

	/**
	 * Returns an unmodifiable bidirectional iterator backed by the specified
	 * bidirectional iterator.
	 *
	 * @param i the bidirectional iterator to be wrapped in an unmodifiable
	 *          bidirectional iterator.
	 * @return an unmodifiable view of the specified bidirectional iterator.
	 */
	public static DoubleBidirectionalIterator unmodifiable(final DoubleBidirectionalIterator i) {
		return new UnmodifiableBidirectionalIterator(i);
	}

	/** An unmodifiable wrapper class for list iterators. */
	public static class UnmodifiableListIterator extends AbstractDoubleListIterator {
		final protected DoubleListIterator i;

		public UnmodifiableListIterator(final DoubleListIterator i) {
			this.i = i;
		}

		public boolean hasNext() {
			return i.hasNext();
		}

		public boolean hasPrevious() {
			return i.hasPrevious();
		}

		public double nextDouble() {
			return i.nextDouble();
		}

		public double previousDouble() {
			return i.previousDouble();
		}

		public int nextIndex() {
			return i.nextIndex();
		}

		public int previousIndex() {
			return i.previousIndex();
		}

		public Double next() {
			return i.next();
		}

		public Double previous() {
			return i.previous();
		}
	}

	/**
	 * Returns an unmodifiable list iterator backed by the specified list iterator.
	 *
	 * @param i the list iterator to be wrapped in an unmodifiable list iterator.
	 * @return an unmodifiable view of the specified list iterator.
	 */
	public static DoubleListIterator unmodifiable(final DoubleListIterator i) {
		return new UnmodifiableListIterator(i);
	}

	/** A wrapper promoting the results of a ByteIterator. */
	protected static class ByteIteratorWrapper implements DoubleIterator {
		final it.unimi.dsi.fastutil.bytes.ByteIterator iterator;

		public ByteIteratorWrapper(final it.unimi.dsi.fastutil.bytes.ByteIterator iterator) {
			this.iterator = iterator;
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public Double next() {
			return Double.valueOf(iterator.nextByte());
		}

		public double nextDouble() {
			return iterator.nextByte();
		}

		public void remove() {
			iterator.remove();
		}

		public int skip(final int n) {
			return iterator.skip(n);
		}
	}

	/**
	 * Returns an iterator backed by the specified byte iterator.
	 * 
	 * @return an iterator backed by the specified byte iterator.
	 */
	public static DoubleIterator wrap(final it.unimi.dsi.fastutil.bytes.ByteIterator iterator) {
		return new ByteIteratorWrapper(iterator);
	}

	/** A wrapper promoting the results of a ShortIterator. */
	protected static class ShortIteratorWrapper implements DoubleIterator {
		final it.unimi.dsi.fastutil.shorts.ShortIterator iterator;

		public ShortIteratorWrapper(final it.unimi.dsi.fastutil.shorts.ShortIterator iterator) {
			this.iterator = iterator;
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public Double next() {
			return Double.valueOf(iterator.nextShort());
		}

		public double nextDouble() {
			return iterator.nextShort();
		}

		public void remove() {
			iterator.remove();
		}

		public int skip(final int n) {
			return iterator.skip(n);
		}
	}

	/**
	 * Returns an iterator backed by the specified short iterator.
	 * 
	 * @return an iterator backed by the specified short iterator.
	 */
	public static DoubleIterator wrap(final it.unimi.dsi.fastutil.shorts.ShortIterator iterator) {
		return new ShortIteratorWrapper(iterator);
	}

	/** A wrapper promoting the results of an IntIterator. */
	protected static class IntIteratorWrapper implements DoubleIterator {
		final it.unimi.dsi.fastutil.ints.IntIterator iterator;

		public IntIteratorWrapper(final it.unimi.dsi.fastutil.ints.IntIterator iterator) {
			this.iterator = iterator;
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public Double next() {
			return Double.valueOf(iterator.nextInt());
		}

		public double nextDouble() {
			return iterator.nextInt();
		}

		public void remove() {
			iterator.remove();
		}

		public int skip(final int n) {
			return iterator.skip(n);
		}
	}

	/**
	 * Returns an iterator backed by the specified integer iterator.
	 * 
	 * @return an iterator backed by the specified integer iterator.
	 */
	public static DoubleIterator wrap(final it.unimi.dsi.fastutil.ints.IntIterator iterator) {
		return new IntIteratorWrapper(iterator);
	}

	/** A wrapper promoting the results of a FloatIterator. */
	protected static class FloatIteratorWrapper implements DoubleIterator {
		final it.unimi.dsi.fastutil.floats.FloatIterator iterator;

		public FloatIteratorWrapper(final it.unimi.dsi.fastutil.floats.FloatIterator iterator) {
			this.iterator = iterator;
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public Double next() {
			return Double.valueOf(iterator.nextFloat());
		}

		public double nextDouble() {
			return iterator.nextFloat();
		}

		public void remove() {
			iterator.remove();
		}

		public int skip(final int n) {
			return iterator.skip(n);
		}
	}

	/**
	 * Returns an iterator backed by the specified float iterator.
	 * 
	 * @return an iterator backed by the specified float iterator.
	 */
	public static DoubleIterator wrap(final it.unimi.dsi.fastutil.floats.FloatIterator iterator) {
		return new FloatIteratorWrapper(iterator);
	}
}