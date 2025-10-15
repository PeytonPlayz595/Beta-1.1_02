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
package it.unimi.dsi.fastutil.floats;

import java.util.Collection;
import java.util.Set;

/**
 * A class providing static methods and objects that do useful things with
 * type-specific sets.
 *
 * @see java.util.Collections
 */
public class FloatSets {
	private FloatSets() {
	}

	/**
	 * An immutable class representing the empty set and implementing a
	 * type-specific set interface.
	 *
	 * <P>
	 * This class may be useful to implement your own in case you subclass a
	 * type-specific set.
	 */
	public static class EmptySet extends FloatCollections.EmptyCollection
			implements FloatSet, java.io.Serializable, Cloneable {
		private static final long serialVersionUID = -7046029254386353129L;

		protected EmptySet() {
		}

		public boolean remove(float ok) {
			throw new UnsupportedOperationException();
		}

		public Object clone() {
			return EMPTY_SET;
		}

		private Object readResolve() {
			return EMPTY_SET;
		}
	}

	/**
	 * An empty set (immutable). It is serializable and cloneable.
	 *
	 * <P>
	 * The class of this objects represent an abstract empty set that is a subset of
	 * a (sorted) type-specific set.
	 */
	public static final EmptySet EMPTY_SET = new EmptySet();

	/**
	 * An immutable class representing a type-specific singleton set.
	 *
	 * <P>
	 * This class may be useful to implement your own in case you subclass a
	 * type-specific set.
	 */
	public static class Singleton extends AbstractFloatSet implements java.io.Serializable, Cloneable {
		private static final long serialVersionUID = -7046029254386353129L;
		protected final float element;

		protected Singleton(final float element) {
			this.element = element;
		}

		public boolean add(final float k) {
			throw new UnsupportedOperationException();
		}

		public boolean contains(final float k) {
			return ((k) == (element));
		}

		public boolean addAll(final Collection<? extends Float> c) {
			throw new UnsupportedOperationException();
		}

		public boolean removeAll(final Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(final Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		/* Slightly optimized w.r.t. the one in ABSTRACT_SET. */
		public float[] toFloatArray() {
			float a[] = new float[1];
			a[0] = element;
			return a;
		}

		public boolean addAll(final FloatCollection c) {
			throw new UnsupportedOperationException();
		}

		public boolean removeAll(final FloatCollection c) {
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(final FloatCollection c) {
			throw new UnsupportedOperationException();
		}

		public FloatListIterator iterator() {
			return FloatIterators.singleton(element);
		}

		public int size() {
			return 1;
		}

		public Object clone() {
			return this;
		}
	}

	/**
	 * Returns a type-specific immutable set containing only the specified element.
	 * The returned set is serializable and cloneable.
	 *
	 * @param element the only element of the returned set.
	 * @return a type-specific immutable set containing just <code>element</code>.
	 */

	public static FloatSet singleton(final float element) {
		return new Singleton(element);
	}

	/**
	 * Returns a type-specific immutable set containing only the specified element.
	 * The returned set is serializable and cloneable.
	 *
	 * @param element the only element of the returned set.
	 * @return a type-specific immutable set containing just <code>element</code>.
	 */

	public static FloatSet singleton(final Float element) {
		return new Singleton(((element).floatValue()));
	}

	/** A synchronized wrapper class for sets. */

	public static class SynchronizedSet extends FloatCollections.SynchronizedCollection
			implements FloatSet, java.io.Serializable {

		private static final long serialVersionUID = -7046029254386353129L;

		protected SynchronizedSet(final FloatSet s, final Object sync) {
			super(s, sync);
		}

		protected SynchronizedSet(final FloatSet s) {
			super(s);
		}

		public boolean remove(final float k) {
			synchronized (sync) {
				return collection.remove((Float.valueOf(k)));
			}
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
	}

	/**
	 * Returns a synchronized type-specific set backed by the given type-specific
	 * set.
	 *
	 * @param s the set to be wrapped in a synchronized set.
	 * @return a synchronized view of the specified set.
	 * @see java.util.Collections#synchronizedSet(Set)
	 */
	public static FloatSet synchronize(final FloatSet s) {
		return new SynchronizedSet(s);
	}

	/**
	 * Returns a synchronized type-specific set backed by the given type-specific
	 * set, using an assigned object to synchronize.
	 *
	 * @param s    the set to be wrapped in a synchronized set.
	 * @param sync an object that will be used to synchronize the access to the set.
	 * @return a synchronized view of the specified set.
	 * @see java.util.Collections#synchronizedSet(Set)
	 */

	public static FloatSet synchronize(final FloatSet s, final Object sync) {
		return new SynchronizedSet(s, sync);
	}

	/** An unmodifiable wrapper class for sets. */

	public static class UnmodifiableSet extends FloatCollections.UnmodifiableCollection
			implements FloatSet, java.io.Serializable {

		private static final long serialVersionUID = -7046029254386353129L;

		protected UnmodifiableSet(final FloatSet s) {
			super(s);
		}

		public boolean remove(final float k) {
			throw new UnsupportedOperationException();
		}

		public boolean equals(final Object o) {
			return collection.equals(o);
		}

		public int hashCode() {
			return collection.hashCode();
		}
	}

	/**
	 * Returns an unmodifiable type-specific set backed by the given type-specific
	 * set.
	 *
	 * @param s the set to be wrapped in an unmodifiable set.
	 * @return an unmodifiable view of the specified set.
	 * @see java.util.Collections#unmodifiableSet(Set)
	 */
	public static FloatSet unmodifiable(final FloatSet s) {
		return new UnmodifiableSet(s);
	}
}