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
package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleIterator;
import java.util.Iterator;
import java.util.Map;

/**
 * An abstract class providing basic methods for maps implementing a
 * type-specific interface.
 *
 * <P>
 * Optional operations just throw an {@link UnsupportedOperationException}.
 * Generic versions of accessors delegate to the corresponding type-specific
 * counterparts following the interface rules (they take care of returning
 * <code>null</code> on a missing key).
 *
 * <P>
 * As a further help, this class provides a {@link BasicEntry BasicEntry} inner
 * class that implements a type-specific version of {@link java.util.Map.Entry};
 * it is particularly useful for those classes that do not implement their own
 * entries (e.g., most immutable maps).
 */
public abstract class AbstractObject2DoubleMap<K> extends AbstractObject2DoubleFunction<K>
		implements Object2DoubleMap<K>, java.io.Serializable {
	private static final long serialVersionUID = -4940583368468432370L;

	protected AbstractObject2DoubleMap() {
	}

	public boolean containsValue(Object ov) {
		return containsValue(((((Double) (ov)).doubleValue())));
	}

	/** Checks whether the given value is contained in {@link #values()}. */
	public boolean containsValue(double v) {
		return values().contains(v);
	}

	/** Checks whether the given value is contained in {@link #keySet()}. */
	public boolean containsKey(Object k) {
		return keySet().contains(k);
	}

	/**
	 * Puts all pairs in the given map. If the map implements the interface of this
	 * map, it uses the faster iterators.
	 *
	 * @param m a map.
	 */
	@SuppressWarnings("unchecked")
	public void putAll(Map<? extends K, ? extends Double> m) {
		int n = m.size();
		final Iterator<? extends Map.Entry<? extends K, ? extends Double>> i = m.entrySet().iterator();
		if (m instanceof Object2DoubleMap) {
			Object2DoubleMap.Entry<? extends K> e;
			while (n-- != 0) {
				e = (Object2DoubleMap.Entry<? extends K>) i.next();
				put(e.getKey(), e.getDoubleValue());
			}
		} else {
			Map.Entry<? extends K, ? extends Double> e;
			while (n-- != 0) {
				e = i.next();
				put(e.getKey(), e.getValue());
			}
		}
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * This class provides a basic but complete type-specific entry class for all
	 * those maps implementations that do not have entries on their own (e.g., most
	 * immutable maps).
	 *
	 * <P>
	 * This class does not implement {@link java.util.Map.Entry#setValue(Object)
	 * setValue()}, as the modification would not be reflected in the base map.
	 */
	public static class BasicEntry<K> implements Object2DoubleMap.Entry<K> {
		protected K key;
		protected double value;

		public BasicEntry(final K key, final Double value) {
			this.key = (key);
			this.value = ((value).doubleValue());
		}

		public BasicEntry(final K key, final double value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return (key);
		}

		public Double getValue() {
			return (Double.valueOf(value));
		}

		public double getDoubleValue() {
			return value;
		}

		public double setValue(final double value) {
			throw new UnsupportedOperationException();
		}

		public Double setValue(final Double value) {
			return Double.valueOf(setValue(value.doubleValue()));
		}

		public boolean equals(final Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;

			return ((key) == null ? ((e.getKey())) == null : (key).equals((e.getKey())))
					&& ((value) == (((((Double) (e.getValue())).doubleValue()))));
		}

		public int hashCode() {
			return ((key) == null ? 0 : (key).hashCode()) ^ it.unimi.dsi.fastutil.HashCommon.double2int(value);
		}

		public String toString() {
			return key + "->" + value;
		}
	}

	/**
	 * Returns a type-specific-set view of the keys of this map.
	 *
	 * <P>
	 * The view is backed by the set returned by {@link #entrySet()}. Note that
	 * <em>no attempt is made at caching the result of this method</em>, as this
	 * would require adding some attributes that lightweight implementations would
	 * not need. Subclasses may easily override this policy by calling this method
	 * and caching the result, but implementors are encouraged to write more
	 * efficient ad-hoc implementations.
	 *
	 * @return a set view of the keys of this map; it may be safely cast to a
	 *         type-specific interface.
	 */

	public ObjectSet<K> keySet() {
		return new AbstractObjectSet<K>() {

			public boolean contains(final Object k) {
				return containsKey(k);
			}

			public int size() {
				return AbstractObject2DoubleMap.this.size();
			}

			public void clear() {
				AbstractObject2DoubleMap.this.clear();
			}

			public ObjectIterator<K> iterator() {
				return new AbstractObjectIterator<K>() {
					final ObjectIterator<Map.Entry<K, Double>> i = entrySet().iterator();

					public K next() {
						return ((Object2DoubleMap.Entry<K>) i.next()).getKey();
					};

					public boolean hasNext() {
						return i.hasNext();
					}
				};
			}
		};
	}

	/**
	 * Returns a type-specific-set view of the values of this map.
	 *
	 * <P>
	 * The view is backed by the set returned by {@link #entrySet()}. Note that
	 * <em>no attempt is made at caching the result of this method</em>, as this
	 * would require adding some attributes that lightweight implementations would
	 * not need. Subclasses may easily override this policy by calling this method
	 * and caching the result, but implementors are encouraged to write more
	 * efficient ad-hoc implementations.
	 *
	 * @return a set view of the values of this map; it may be safely cast to a
	 *         type-specific interface.
	 */

	public DoubleCollection values() {
		return new AbstractDoubleCollection() {

			public boolean contains(final double k) {
				return containsValue(k);
			}

			public int size() {
				return AbstractObject2DoubleMap.this.size();
			}

			public void clear() {
				AbstractObject2DoubleMap.this.clear();
			}

			public DoubleIterator iterator() {
				return new AbstractDoubleIterator() {
					final ObjectIterator<Map.Entry<K, Double>> i = entrySet().iterator();

					public double nextDouble() {
						return ((Object2DoubleMap.Entry<K>) i.next()).getDoubleValue();
					};

					public boolean hasNext() {
						return i.hasNext();
					}
				};
			}
		};
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ObjectSet<Map.Entry<K, Double>> entrySet() {
		return (ObjectSet) object2DoubleEntrySet();
	}

	/**
	 * Returns a hash code for this map.
	 *
	 * The hash code of a map is computed by summing the hash codes of its entries.
	 *
	 * @return a hash code for this map.
	 */

	public int hashCode() {
		int h = 0, n = size();
		final ObjectIterator<? extends Map.Entry<K, Double>> i = entrySet().iterator();

		while (n-- != 0)
			h += i.next().hashCode();
		return h;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Map))
			return false;

		Map<?, ?> m = (Map<?, ?>) o;
		if (m.size() != size())
			return false;
		return entrySet().containsAll(m.entrySet());
	}

	@SuppressWarnings("unchecked")
	public String toString() {
		final StringBuilder s = new StringBuilder();
		final ObjectIterator<? extends Map.Entry<K, Double>> i = entrySet().iterator();
		int n = size();
		Object2DoubleMap.Entry<K> e;
		boolean first = true;

		s.append("{");

		while (n-- != 0) {
			if (first)
				first = false;
			else
				s.append(", ");

			e = (Object2DoubleMap.Entry<K>) i.next();

			if (this == e.getKey())
				s.append("(this map)");
			else

				s.append(String.valueOf(e.getKey()));
			s.append("=>");

			s.append(String.valueOf(e.getDoubleValue()));
		}

		s.append("}");
		return s.toString();
	}

}