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
import java.util.Collection;
import java.util.Random;

/**
 * A class providing static methods and objects that do useful things with
 * type-specific lists.
 *
 * @see java.util.Collections
 */
public class DoubleLists {
	private DoubleLists() {
	}

	/**
	 * Shuffles the specified list using the specified pseudorandom number
	 * generator.
	 * 
	 * @param l      the list to be shuffled.
	 * @param random a pseudorandom number generator (please use a <a href=
	 *               "http://dsiutils.dsi.unimi.it/docs/it/unimi/dsi/util/XorShiftStarRandom.html">XorShift*</a>
	 *               generator).
	 * @return <code>l</code>.
	 */
	public static DoubleList shuffle(final DoubleList l, final Random random) {
		for (int i = l.size(); i-- != 0;) {
			final int p = random.nextInt(i + 1);
			final double t = l.getDouble(i);
			l.set(i, l.getDouble(p));
			l.set(p, t);
		}
		return l;
	}

	/**
	 * An immutable class representing an empty type-specific list.
	 *
	 * <P>
	 * This class may be useful to implement your own in case you subclass a
	 * type-specific list.
	 */
	public static class EmptyList extends DoubleCollections.EmptyCollection
			implements DoubleList, java.io.Serializable, Cloneable {
		private static final long serialVersionUID = -7046029254386353129L;

		protected EmptyList() {
		}

		public void add(final int index, final double k) {
			throw new UnsupportedOperationException();
		}

		public boolean add(final double k) {
			throw new UnsupportedOperationException();
		}

		public double removeDouble(int i) {
			throw new UnsupportedOperationException();
		}

		public double set(final int index, final double k) {
			throw new UnsupportedOperationException();
		}

		public int indexOf(double k) {
			return -1;
		}

		public int lastIndexOf(double k) {
			return -1;
		}

		public boolean addAll(Collection<? extends Double> c) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(int i, Collection<? extends Double> c) {
			throw new UnsupportedOperationException();
		}

		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		public Double get(int i) {
			throw new IndexOutOfBoundsException();
		}

		public boolean addAll(DoubleCollection c) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(DoubleList c) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(int i, DoubleCollection c) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(int i, DoubleList c) {
			throw new UnsupportedOperationException();
		}

		public void add(final int index, final Double k) {
			throw new UnsupportedOperationException();
		}

		public boolean add(final Double k) {
			throw new UnsupportedOperationException();
		}

		public Double set(final int index, final Double k) {
			throw new UnsupportedOperationException();
		}

		public double getDouble(int i) {
			throw new IndexOutOfBoundsException();
		}

		public Double remove(int k) {
			throw new UnsupportedOperationException();
		}

		public int indexOf(Object k) {
			return -1;
		}

		public int lastIndexOf(Object k) {
			return -1;
		}

		// @SuppressWarnings("unchecked")
		// public KEY_ITERATOR KEY_GENERIC iterator( int i ) { if ( i == 0 ) return
		// ITERATORS.EMPTY_ITERATOR; throw new IndexOutOfBoundsException(
		// String.valueOf( i ) ); }
		@Deprecated
		public DoubleIterator doubleIterator() {
			return DoubleIterators.EMPTY_ITERATOR;
		}

		public DoubleListIterator listIterator() {
			return DoubleIterators.EMPTY_ITERATOR;
		}

		public DoubleListIterator iterator() {
			return DoubleIterators.EMPTY_ITERATOR;
		}

		public DoubleListIterator listIterator(int i) {
			if (i == 0)
				return DoubleIterators.EMPTY_ITERATOR;
			throw new IndexOutOfBoundsException(String.valueOf(i));
		}

		@Deprecated
		public DoubleListIterator doubleListIterator() {
			return listIterator();
		}

		@Deprecated
		public DoubleListIterator doubleListIterator(int i) {
			return listIterator(i);
		}

		public DoubleList subList(int from, int to) {
			if (from == 0 && to == 0)
				return this;
			throw new IndexOutOfBoundsException();
		}

		@Deprecated
		public DoubleList doubleSubList(int from, int to) {
			return subList(from, to);
		}

		public void getElements(int from, double[] a, int offset, int length) {
			if (from == 0 && length == 0 && offset >= 0 && offset <= a.length)
				return;
			throw new IndexOutOfBoundsException();
		}

		public void removeElements(int from, int to) {
			throw new UnsupportedOperationException();
		}

		public void addElements(int index, final double a[], int offset, int length) {
			throw new UnsupportedOperationException();
		}

		public void addElements(int index, final double a[]) {
			throw new UnsupportedOperationException();
		}

		public void size(int s) {
			throw new UnsupportedOperationException();
		}

		public int compareTo(final List<? extends Double> o) {
			if (o == this)
				return 0;
			return ((List<?>) o).isEmpty() ? 0 : -1;
		}

		private Object readResolve() {
			return EMPTY_LIST;
		}

		public Object clone() {
			return EMPTY_LIST;
		}
	}

	/**
	 * An empty list (immutable). It is serializable and cloneable.
	 *
	 * <P>
	 * The class of this objects represent an abstract empty list that is a sublist
	 * of any type of list. Thus, {@link #EMPTY_LIST} may be assigned to a variable
	 * of any (sorted) type-specific list.
	 */

	public static final EmptyList EMPTY_LIST = new EmptyList();

	/**
	 * An immutable class representing a type-specific singleton list.
	 *
	 * <P>
	 * This class may be useful to implement your own in case you subclass a
	 * type-specific list.
	 */

	public static class Singleton extends AbstractDoubleList implements java.io.Serializable, Cloneable {

		private static final long serialVersionUID = -7046029254386353129L;

		private final double element;

		private Singleton(final double element) {
			this.element = element;
		}

		public double getDouble(final int i) {
			if (i == 0)
				return element;
			throw new IndexOutOfBoundsException();
		}

		public double removeDouble(final int i) {
			throw new UnsupportedOperationException();
		}

		public boolean contains(final double k) {
			return ((k) == (element));
		}

		public boolean addAll(final Collection<? extends Double> c) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(final int i, final Collection<? extends Double> c) {
			throw new UnsupportedOperationException();
		}

		public boolean removeAll(final Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(final Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		/* Slightly optimized w.r.t. the one in ABSTRACT_SET. */

		public double[] toDoubleArray() {
			double a[] = new double[1];
			a[0] = element;
			return a;
		}

		public DoubleListIterator listIterator() {
			return DoubleIterators.singleton(element);
		}

		public DoubleListIterator iterator() {
			return listIterator();
		}

		public DoubleListIterator listIterator(int i) {
			if (i > 1 || i < 0)
				throw new IndexOutOfBoundsException();
			DoubleListIterator l = listIterator();
			if (i == 1)
				l.next();
			return l;
		}

		public DoubleList subList(final int from, final int to) {
			ensureIndex(from);
			ensureIndex(to);
			if (from > to)
				throw new IndexOutOfBoundsException(
						"Start index (" + from + ") is greater than end index (" + to + ")");

			if (from != 0 || to != 1)
				return EMPTY_LIST;
			return this;
		}

		public int size() {
			return 1;
		}

		public void size(final int size) {
			throw new UnsupportedOperationException();
		}

		public void clear() {
			throw new UnsupportedOperationException();
		}

		public Object clone() {
			return this;
		}

		public boolean rem(final double k) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(final DoubleCollection c) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(final int i, final DoubleCollection c) {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * Returns a type-specific immutable list containing only the specified element.
	 * The returned list is serializable and cloneable.
	 *
	 * @param element the only element of the returned list.
	 * @return a type-specific immutable list containing just <code>element</code>.
	 */

	public static DoubleList singleton(final double element) {
		return new Singleton(element);
	}

	/**
	 * Returns a type-specific immutable list containing only the specified element.
	 * The returned list is serializable and cloneable.
	 *
	 * @param element the only element of the returned list.
	 * @return a type-specific immutable list containing just <code>element</code>.
	 */

	public static DoubleList singleton(final Object element) {
		return new Singleton(((((Double) (element)).doubleValue())));
	}

	/** A synchronized wrapper class for lists. */

	public static class SynchronizedList extends DoubleCollections.SynchronizedCollection
			implements DoubleList, java.io.Serializable {

		private static final long serialVersionUID = -7046029254386353129L;

		protected final DoubleList list; // Due to the large number of methods that are not in COLLECTION, this is worth
											// caching.

		protected SynchronizedList(final DoubleList l, final Object sync) {
			super(l, sync);
			this.list = l;
		}

		protected SynchronizedList(final DoubleList l) {
			super(l);
			this.list = l;
		}

		public double getDouble(final int i) {
			synchronized (sync) {
				return list.getDouble(i);
			}
		}

		public double set(final int i, final double k) {
			synchronized (sync) {
				return list.set(i, k);
			}
		}

		public void add(final int i, final double k) {
			synchronized (sync) {
				list.add(i, k);
			}
		}

		public double removeDouble(final int i) {
			synchronized (sync) {
				return list.removeDouble(i);
			}
		}

		public int indexOf(final double k) {
			synchronized (sync) {
				return list.indexOf(k);
			}
		}

		public int lastIndexOf(final double k) {
			synchronized (sync) {
				return list.lastIndexOf(k);
			}
		}

		public boolean addAll(final int index, final Collection<? extends Double> c) {
			synchronized (sync) {
				return list.addAll(index, c);
			}
		}

		public void getElements(final int from, final double a[], final int offset, final int length) {
			synchronized (sync) {
				list.getElements(from, a, offset, length);
			}
		}

		public void removeElements(final int from, final int to) {
			synchronized (sync) {
				list.removeElements(from, to);
			}
		}

		public void addElements(int index, final double a[], int offset, int length) {
			synchronized (sync) {
				list.addElements(index, a, offset, length);
			}
		}

		public void addElements(int index, final double a[]) {
			synchronized (sync) {
				list.addElements(index, a);
			}
		}

		public void size(final int size) {
			synchronized (sync) {
				list.size(size);
			}
		}

		public DoubleListIterator iterator() {
			return list.listIterator();
		}

		public DoubleListIterator listIterator() {
			return list.listIterator();
		}

		public DoubleListIterator listIterator(final int i) {
			return list.listIterator(i);
		}

		@Deprecated
		public DoubleListIterator doubleListIterator() {
			return listIterator();
		}

		@Deprecated
		public DoubleListIterator doubleListIterator(final int i) {
			return listIterator(i);
		}

		public DoubleList subList(final int from, final int to) {
			synchronized (sync) {
				return synchronize(list.subList(from, to), sync);
			}
		}

		@Deprecated
		public DoubleList doubleSubList(final int from, final int to) {
			return subList(from, to);
		}

		public boolean equals(final Object o) {
			synchronized (sync) {
				return collection.equals(o);
			}
		}

		public int hashCode() {
			synchronized (sync) {
				return collection.hashCode();
			}
		}

		public int compareTo(final List<? extends Double> o) {
			synchronized (sync) {
				return list.compareTo(o);
			}
		}

		public boolean addAll(final int index, final DoubleCollection c) {
			synchronized (sync) {
				return list.addAll(index, c);
			}
		}

		public boolean addAll(final int index, DoubleList l) {
			synchronized (sync) {
				return list.addAll(index, l);
			}
		}

		public boolean addAll(DoubleList l) {
			synchronized (sync) {
				return list.addAll(l);
			}
		}

		public Double get(final int i) {
			synchronized (sync) {
				return list.get(i);
			}
		}

		public void add(final int i, Double k) {
			synchronized (sync) {
				list.add(i, k);
			}
		}

		public Double set(final int index, Double k) {
			synchronized (sync) {
				return list.set(index, k);
			}
		}

		public Double remove(final int i) {
			synchronized (sync) {
				return list.remove(i);
			}
		}

		public int indexOf(final Object o) {
			synchronized (sync) {
				return list.indexOf(o);
			}
		}

		public int lastIndexOf(final Object o) {
			synchronized (sync) {
				return list.lastIndexOf(o);
			}
		}

	}

	/**
	 * Returns a synchronized type-specific list backed by the given type-specific
	 * list.
	 *
	 * @param l the list to be wrapped in a synchronized list.
	 * @return a synchronized view of the specified list.
	 * @see java.util.Collections#synchronizedList(List)
	 */
	public static DoubleList synchronize(final DoubleList l) {
		return new SynchronizedList(l);
	}

	/**
	 * Returns a synchronized type-specific list backed by the given type-specific
	 * list, using an assigned object to synchronize.
	 *
	 * @param l    the list to be wrapped in a synchronized list.
	 * @param sync an object that will be used to synchronize the access to the
	 *             list.
	 * @return a synchronized view of the specified list.
	 * @see java.util.Collections#synchronizedList(List)
	 */

	public static DoubleList synchronize(final DoubleList l, final Object sync) {
		return new SynchronizedList(l, sync);
	}

	/** An unmodifiable wrapper class for lists. */

	public static class UnmodifiableList extends DoubleCollections.UnmodifiableCollection
			implements DoubleList, java.io.Serializable {

		private static final long serialVersionUID = -7046029254386353129L;

		protected final DoubleList list; // Due to the large number of methods that are not in COLLECTION, this is worth
											// caching.

		protected UnmodifiableList(final DoubleList l) {
			super(l);
			this.list = l;
		}

		public double getDouble(final int i) {
			return list.getDouble(i);
		}

		public double set(final int i, final double k) {
			throw new UnsupportedOperationException();
		}

		public void add(final int i, final double k) {
			throw new UnsupportedOperationException();
		}

		public double removeDouble(final int i) {
			throw new UnsupportedOperationException();
		}

		public int indexOf(final double k) {
			return list.indexOf(k);
		}

		public int lastIndexOf(final double k) {
			return list.lastIndexOf(k);
		}

		public boolean addAll(final int index, final Collection<? extends Double> c) {
			throw new UnsupportedOperationException();
		}

		public void getElements(final int from, final double a[], final int offset, final int length) {
			list.getElements(from, a, offset, length);
		}

		public void removeElements(final int from, final int to) {
			throw new UnsupportedOperationException();
		}

		public void addElements(int index, final double a[], int offset, int length) {
			throw new UnsupportedOperationException();
		}

		public void addElements(int index, final double a[]) {
			throw new UnsupportedOperationException();
		}

		public void size(final int size) {
			list.size(size);
		}

		public DoubleListIterator iterator() {
			return listIterator();
		}

		public DoubleListIterator listIterator() {
			return DoubleIterators.unmodifiable(list.listIterator());
		}

		public DoubleListIterator listIterator(final int i) {
			return DoubleIterators.unmodifiable(list.listIterator(i));
		}

		@Deprecated
		public DoubleListIterator doubleListIterator() {
			return listIterator();
		}

		@Deprecated
		public DoubleListIterator doubleListIterator(final int i) {
			return listIterator(i);
		}

		public DoubleList subList(final int from, final int to) {
			return unmodifiable(list.subList(from, to));
		}

		@Deprecated
		public DoubleList doubleSubList(final int from, final int to) {
			return subList(from, to);
		}

		public boolean equals(final Object o) {
			return collection.equals(o);
		}

		public int hashCode() {
			return collection.hashCode();
		}

		public int compareTo(final List<? extends Double> o) {
			return list.compareTo(o);
		}

		public boolean addAll(final int index, final DoubleCollection c) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(final DoubleList l) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(final int index, final DoubleList l) {
			throw new UnsupportedOperationException();
		}

		public Double get(final int i) {
			return list.get(i);
		}

		public void add(final int i, Double k) {
			throw new UnsupportedOperationException();
		}

		public Double set(final int index, Double k) {
			throw new UnsupportedOperationException();
		}

		public Double remove(final int i) {
			throw new UnsupportedOperationException();
		}

		public int indexOf(final Object o) {
			return list.indexOf(o);
		}

		public int lastIndexOf(final Object o) {
			return list.lastIndexOf(o);
		}

	}

	/**
	 * Returns an unmodifiable type-specific list backed by the given type-specific
	 * list.
	 *
	 * @param l the list to be wrapped in an unmodifiable list.
	 * @return an unmodifiable view of the specified list.
	 * @see java.util.Collections#unmodifiableList(List)
	 */
	public static DoubleList unmodifiable(final DoubleList l) {
		return new UnmodifiableList(l);
	}
}