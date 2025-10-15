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

import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * An abstract class providing basic methods for lists implementing a
 * type-specific list interface.
 *
 * <P>
 * As an additional bonus, this class implements on top of the list operations a
 * type-specific stack.
 */
public abstract class AbstractDoubleList extends AbstractDoubleCollection implements DoubleList, DoubleStack {
	protected AbstractDoubleList() {
	}

	/**
	 * Ensures that the given index is nonnegative and not greater than the list
	 * size.
	 *
	 * @param index an index.
	 * @throws IndexOutOfBoundsException if the given index is negative or greater
	 *                                   than the list size.
	 */
	protected void ensureIndex(final int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
		if (index > size())
			throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + (size()) + ")");
	}

	/**
	 * Ensures that the given index is nonnegative and smaller than the list size.
	 *
	 * @param index an index.
	 * @throws IndexOutOfBoundsException if the given index is negative or not
	 *                                   smaller than the list size.
	 */
	protected void ensureRestrictedIndex(final int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
		if (index >= size())
			throw new IndexOutOfBoundsException(
					"Index (" + index + ") is greater than or equal to list size (" + (size()) + ")");
	}

	public void add(final int index, final double k) {
		throw new UnsupportedOperationException();
	}

	public boolean add(final double k) {
		add(size(), k);
		return true;
	}

	public double removeDouble(int i) {
		throw new UnsupportedOperationException();
	}

	public double set(final int index, final double k) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int index, final Collection<? extends Double> c) {
		ensureIndex(index);
		int n = c.size();
		if (n == 0)
			return false;
		Iterator<? extends Double> i = c.iterator();
		while (n-- != 0)
			add(index++, i.next());
		return true;
	}

	/** Delegates to a more generic method. */
	public boolean addAll(final Collection<? extends Double> c) {
		return addAll(size(), c);
	}

	/** Delegates to the new covariantly stronger generic method. */
	@Deprecated
	public DoubleListIterator doubleListIterator() {
		return listIterator();
	}

	/** Delegates to the new covariantly stronger generic method. */
	@Deprecated
	public DoubleListIterator doubleListIterator(final int index) {
		return listIterator(index);
	}

	public DoubleListIterator iterator() {
		return listIterator();
	}

	public DoubleListIterator listIterator() {
		return listIterator(0);
	}

	public DoubleListIterator listIterator(final int index) {
		return new AbstractDoubleListIterator() {
			int pos = index, last = -1;

			public boolean hasNext() {
				return pos < AbstractDoubleList.this.size();
			}

			public boolean hasPrevious() {
				return pos > 0;
			}

			public double nextDouble() {
				if (!hasNext())
					throw new NoSuchElementException();
				return AbstractDoubleList.this.getDouble(last = pos++);
			}

			public double previousDouble() {
				if (!hasPrevious())
					throw new NoSuchElementException();
				return AbstractDoubleList.this.getDouble(last = --pos);
			}

			public int nextIndex() {
				return pos;
			}

			public int previousIndex() {
				return pos - 1;
			}

			public void add(double k) {
				if (last == -1)
					throw new IllegalStateException();
				AbstractDoubleList.this.add(pos++, k);
				last = -1;
			}

			public void set(double k) {
				if (last == -1)
					throw new IllegalStateException();
				AbstractDoubleList.this.set(last, k);
			}

			public void remove() {
				if (last == -1)
					throw new IllegalStateException();
				AbstractDoubleList.this.removeDouble(last);
				/*
				 * If the last operation was a next(), we are removing an element *before* us,
				 * and we must decrease pos correspondingly.
				 */
				if (last < pos)
					pos--;
				last = -1;
			}
		};
	}

	public boolean contains(final double k) {
		return indexOf(k) >= 0;
	}

	public int indexOf(final double k) {
		final DoubleListIterator i = listIterator();
		double e;
		while (i.hasNext()) {
			e = i.nextDouble();
			if (((k) == (e)))
				return i.previousIndex();
		}
		return -1;
	}

	public int lastIndexOf(final double k) {
		DoubleListIterator i = listIterator(size());
		double e;
		while (i.hasPrevious()) {
			e = i.previousDouble();
			if (((k) == (e)))
				return i.nextIndex();
		}
		return -1;
	}

	public void size(final int size) {
		int i = size();
		if (size > i)
			while (i++ < size)
				add(((double) 0));
		else
			while (i-- != size)
				remove(i);
	}

	public DoubleList subList(final int from, final int to) {
		ensureIndex(from);
		ensureIndex(to);
		if (from > to)
			throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");

		return new DoubleSubList(this, from, to);
	}

	/** Delegates to the new covariantly stronger generic method. */

	@Deprecated
	public DoubleList doubleSubList(final int from, final int to) {
		return subList(from, to);
	}

	/**
	 * Removes elements of this type-specific list one-by-one.
	 *
	 * <P>
	 * This is a trivial iterator-based implementation. It is expected that
	 * implementations will override this method with a more optimized version.
	 *
	 *
	 * @param from the start index (inclusive).
	 * @param to   the end index (exclusive).
	 */

	public void removeElements(final int from, final int to) {
		ensureIndex(to);
		DoubleListIterator i = listIterator(from);
		int n = to - from;
		if (n < 0)
			throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
		while (n-- != 0) {
			i.nextDouble();
			i.remove();
		}
	}

	/**
	 * Adds elements to this type-specific list one-by-one.
	 *
	 * <P>
	 * This is a trivial iterator-based implementation. It is expected that
	 * implementations will override this method with a more optimized version.
	 *
	 * @param index  the index at which to add elements.
	 * @param a      the array containing the elements.
	 * @param offset the offset of the first element to add.
	 * @param length the number of elements to add.
	 */

	public void addElements(int index, final double a[], int offset, int length) {
		ensureIndex(index);
		if (offset < 0)
			throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
		if (offset + length > a.length)
			throw new ArrayIndexOutOfBoundsException(
					"End index (" + (offset + length) + ") is greater than array length (" + a.length + ")");
		while (length-- != 0)
			add(index++, a[offset++]);
	}

	public void addElements(final int index, final double a[]) {
		addElements(index, a, 0, a.length);
	}

	/**
	 * Copies element of this type-specific list into the given array one-by-one.
	 *
	 * <P>
	 * This is a trivial iterator-based implementation. It is expected that
	 * implementations will override this method with a more optimized version.
	 *
	 * @param from   the start index (inclusive).
	 * @param a      the destination array.
	 * @param offset the offset into the destination array where to store the first
	 *               element copied.
	 * @param length the number of elements to be copied.
	 */

	public void getElements(final int from, final double a[], int offset, int length) {
		DoubleListIterator i = listIterator(from);
		if (offset < 0)
			throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
		if (offset + length > a.length)
			throw new ArrayIndexOutOfBoundsException(
					"End index (" + (offset + length) + ") is greater than array length (" + a.length + ")");
		if (from + length > size())
			throw new IndexOutOfBoundsException(
					"End index (" + (from + length) + ") is greater than list size (" + size() + ")");
		while (length-- != 0)
			a[offset++] = i.nextDouble();
	}

	private boolean valEquals(final Object a, final Object b) {
		return a == null ? b == null : a.equals(b);
	}

	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (!(o instanceof List))
			return false;
		final List<?> l = (List<?>) o;
		int s = size();
		if (s != l.size())
			return false;

		final ListIterator<?> i1 = listIterator(), i2 = l.listIterator();

		while (s-- != 0)
			if (!valEquals(i1.next(), i2.next()))
				return false;

		return true;
	}

	/**
	 * Compares this list to another object. If the argument is a
	 * {@link java.util.List}, this method performs a lexicographical comparison;
	 * otherwise, it throws a <code>ClassCastException</code>.
	 *
	 * @param l a list.
	 * @return if the argument is a {@link java.util.List}, a negative integer,
	 *         zero, or a positive integer as this list is lexicographically less
	 *         than, equal to, or greater than the argument.
	 * @throws ClassCastException if the argument is not a list.
	 */

	public int compareTo(final List<? extends Double> l) {
		if (l == this)
			return 0;

		if (l instanceof DoubleList) {

			final DoubleListIterator i1 = listIterator(), i2 = ((DoubleList) l).listIterator();
			int r;
			double e1, e2;

			while (i1.hasNext() && i2.hasNext()) {
				e1 = i1.nextDouble();
				e2 = i2.nextDouble();
				if ((r = (Double.compare((e1), (e2)))) != 0)
					return r;
			}
			return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
		}

		ListIterator<? extends Double> i1 = listIterator(), i2 = l.listIterator();
		int r;

		while (i1.hasNext() && i2.hasNext()) {
			if ((r = ((Comparable<? super Double>) i1.next()).compareTo(i2.next())) != 0)
				return r;
		}
		return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
	}

	/**
	 * Returns the hash code for this list, which is identical to
	 * {@link java.util.List#hashCode()}.
	 *
	 * @return the hash code for this list.
	 */
	public int hashCode() {
		DoubleIterator i = iterator();
		int h = 1, s = size();
		while (s-- != 0) {
			double k = i.nextDouble();
			h = 31 * h + it.unimi.dsi.fastutil.HashCommon.double2int(k);
		}
		return h;
	}

	public void push(double o) {
		add(o);
	}

	public double popDouble() {
		if (isEmpty())
			throw new NoSuchElementException();
		return removeDouble(size() - 1);
	}

	public double topDouble() {
		if (isEmpty())
			throw new NoSuchElementException();
		return getDouble(size() - 1);
	}

	public double peekDouble(int i) {
		return getDouble(size() - 1 - i);
	}

	public boolean rem(double k) {
		int index = indexOf(k);
		if (index == -1)
			return false;
		removeDouble(index);
		return true;
	}

	/** Delegates to <code>rem()</code>. */
	public boolean remove(final Object o) {
		return rem(((((Double) (o)).doubleValue())));
	}

	/** Delegates to a more generic method. */
	public boolean addAll(final int index, final DoubleCollection c) {
		return addAll(index, (Collection<? extends Double>) c);
	}

	/** Delegates to a more generic method. */
	public boolean addAll(final int index, final DoubleList l) {
		return addAll(index, (DoubleCollection) l);
	}

	public boolean addAll(final DoubleCollection c) {
		return addAll(size(), c);
	}

	public boolean addAll(final DoubleList l) {
		return addAll(size(), l);
	}

	/** Delegates to the corresponding type-specific method. */
	public void add(final int index, final Double ok) {
		add(index, ok.doubleValue());
	}

	/** Delegates to the corresponding type-specific method. */
	public Double set(final int index, final Double ok) {
		return (Double.valueOf(set(index, ok.doubleValue())));
	}

	/** Delegates to the corresponding type-specific method. */
	public Double get(final int index) {
		return (Double.valueOf(getDouble(index)));
	}

	/** Delegates to the corresponding type-specific method. */
	public int indexOf(final Object ok) {
		return indexOf(((((Double) (ok)).doubleValue())));
	}

	/** Delegates to the corresponding type-specific method. */
	public int lastIndexOf(final Object ok) {
		return lastIndexOf(((((Double) (ok)).doubleValue())));
	}

	/** Delegates to the corresponding type-specific method. */
	public Double remove(final int index) {
		return (Double.valueOf(removeDouble(index)));
	}

	/** Delegates to the corresponding type-specific method. */
	public void push(Double o) {
		push(o.doubleValue());
	}

	/** Delegates to the corresponding type-specific method. */
	public Double pop() {
		return Double.valueOf(popDouble());
	}

	/** Delegates to the corresponding type-specific method. */
	public Double top() {
		return Double.valueOf(topDouble());
	}

	/** Delegates to the corresponding type-specific method. */
	public Double peek(int i) {
		return Double.valueOf(peekDouble(i));
	}

	public String toString() {
		final StringBuilder s = new StringBuilder();
		final DoubleIterator i = iterator();
		int n = size();
		double k;
		boolean first = true;

		s.append("[");

		while (n-- != 0) {
			if (first)
				first = false;
			else
				s.append(", ");
			k = i.nextDouble();

			s.append(String.valueOf(k));
		}

		s.append("]");
		return s.toString();
	}

	public static class DoubleSubList extends AbstractDoubleList implements java.io.Serializable {
		private static final long serialVersionUID = -7046029254386353129L;
		/** The list this sublist restricts. */
		protected final DoubleList l;
		/** Initial (inclusive) index of this sublist. */
		protected final int from;
		/** Final (exclusive) index of this sublist. */
		protected int to;

		private static final boolean ASSERTS = false;

		public DoubleSubList(final DoubleList l, final int from, final int to) {
			this.l = l;
			this.from = from;
			this.to = to;
		}

		private void assertRange() {
			if (ASSERTS) {
				assert from <= l.size();
				assert to <= l.size();
				assert to >= from;
			}
		}

		public boolean add(final double k) {
			l.add(to, k);
			to++;
			if (ASSERTS)
				assertRange();
			return true;
		}

		public void add(final int index, final double k) {
			ensureIndex(index);
			l.add(from + index, k);
			to++;
			if (ASSERTS)
				assertRange();
		}

		public boolean addAll(final int index, final Collection<? extends Double> c) {
			ensureIndex(index);
			to += c.size();
			if (ASSERTS) {
				boolean retVal = l.addAll(from + index, c);
				assertRange();
				return retVal;
			}
			return l.addAll(from + index, c);
		}

		public double getDouble(int index) {
			ensureRestrictedIndex(index);
			return l.getDouble(from + index);
		}

		public double removeDouble(int index) {
			ensureRestrictedIndex(index);
			to--;
			return l.removeDouble(from + index);
		}

		public double set(int index, double k) {
			ensureRestrictedIndex(index);
			return l.set(from + index, k);
		}

		public void clear() {
			removeElements(0, size());
			if (ASSERTS)
				assertRange();
		}

		public int size() {
			return to - from;
		}

		public void getElements(final int from, final double[] a, final int offset, final int length) {
			ensureIndex(from);
			if (from + length > size())
				throw new IndexOutOfBoundsException(
						"End index (" + from + length + ") is greater than list size (" + size() + ")");
			l.getElements(this.from + from, a, offset, length);
		}

		public void removeElements(final int from, final int to) {
			ensureIndex(from);
			ensureIndex(to);
			l.removeElements(this.from + from, this.from + to);
			this.to -= (to - from);
			if (ASSERTS)
				assertRange();
		}

		public void addElements(int index, final double a[], int offset, int length) {
			ensureIndex(index);
			l.addElements(this.from + index, a, offset, length);
			this.to += length;
			if (ASSERTS)
				assertRange();
		}

		public DoubleListIterator listIterator(final int index) {
			ensureIndex(index);

			return new AbstractDoubleListIterator() {
				int pos = index, last = -1;

				public boolean hasNext() {
					return pos < size();
				}

				public boolean hasPrevious() {
					return pos > 0;
				}

				public double nextDouble() {
					if (!hasNext())
						throw new NoSuchElementException();
					return l.getDouble(from + (last = pos++));
				}

				public double previousDouble() {
					if (!hasPrevious())
						throw new NoSuchElementException();
					return l.getDouble(from + (last = --pos));
				}

				public int nextIndex() {
					return pos;
				}

				public int previousIndex() {
					return pos - 1;
				}

				public void add(double k) {
					if (last == -1)
						throw new IllegalStateException();
					DoubleSubList.this.add(pos++, k);
					last = -1;
					if (ASSERTS)
						assertRange();
				}

				public void set(double k) {
					if (last == -1)
						throw new IllegalStateException();
					DoubleSubList.this.set(last, k);
				}

				public void remove() {
					if (last == -1)
						throw new IllegalStateException();
					DoubleSubList.this.removeDouble(last);
					/*
					 * If the last operation was a next(), we are removing an element *before* us,
					 * and we must decrease pos correspondingly.
					 */
					if (last < pos)
						pos--;
					last = -1;
					if (ASSERTS)
						assertRange();
				}
			};
		}

		public DoubleList subList(final int from, final int to) {
			ensureIndex(from);
			ensureIndex(to);
			if (from > to)
				throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");

			return new DoubleSubList(this, from, to);
		}

		public boolean rem(double k) {
			int index = indexOf(k);
			if (index == -1)
				return false;
			to--;
			l.removeDouble(from + index);
			if (ASSERTS)
				assertRange();
			return true;
		}

		public boolean remove(final Object o) {
			return rem(((((Double) (o)).doubleValue())));
		}

		public boolean addAll(final int index, final DoubleCollection c) {
			ensureIndex(index);
			to += c.size();
			if (ASSERTS) {
				boolean retVal = l.addAll(from + index, c);
				assertRange();
				return retVal;
			}
			return l.addAll(from + index, c);
		}

		public boolean addAll(final int index, final DoubleList l) {
			ensureIndex(index);
			to += l.size();
			if (ASSERTS) {
				boolean retVal = this.l.addAll(from + index, l);
				assertRange();
				return retVal;
			}
			return this.l.addAll(from + index, l);
		}
	}
}