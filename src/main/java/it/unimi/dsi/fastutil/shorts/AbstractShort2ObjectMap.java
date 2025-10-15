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
package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
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
public abstract class AbstractShort2ObjectMap<V> extends AbstractShort2ObjectFunction<V>
		implements Short2ObjectMap<V>, java.io.Serializable {
	private static final long serialVersionUID = -4940583368468432370L;

	protected AbstractShort2ObjectMap() {
	}

	/** Checks whether the given value is contained in {@link #values()}. */
	public boolean containsValue(Object v) {
		return values().contains(v);
	}

	/** Checks whether the given value is contained in {@link #keySet()}. */
	public boolean containsKey(short k) {
		return keySet().contains(k);
	}

	/**
	 * Puts all pairs in the given map. If the map implements the interface of this
	 * map, it uses the faster iterators.
	 *
	 * @param m a map.
	 */
	@SuppressWarnings("unchecked")
	public void putAll(Map<? extends Short, ? extends V> m) {
		int n = m.size();
		final Iterator<? extends Map.Entry<? extends Short, ? extends V>> i = m.entrySet().iterator();
		if (m instanceof Short2ObjectMap) {
			Short2ObjectMap.Entry<? extends V> e;
			while (n-- != 0) {
				e = (Short2ObjectMap.Entry<? extends V>) i.next();
				put(e.getShortKey(), e.getValue());
			}
		} else {
			Map.Entry<? extends Short, ? extends V> e;
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
	public static class BasicEntry<V> implements Short2ObjectMap.Entry<V> {
		protected short key;
		protected V value;

		public BasicEntry(final Short key, final V value) {
			this.key = ((key).shortValue());
			this.value = (value);
		}

		public BasicEntry(final short key, final V value) {
			this.key = key;
			this.value = value;
		}

		public Short getKey() {
			return (Short.valueOf(key));
		}

		public short getShortKey() {
			return key;
		}

		public V getValue() {
			return (value);
		}

		public V setValue(final V value) {
			throw new UnsupportedOperationException();
		}

		public boolean equals(final Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
			return ((key) == (((((Short) (e.getKey())).shortValue()))))
					&& ((value) == null ? ((e.getValue())) == null : (value).equals((e.getValue())));
		}

		public int hashCode() {
			return (key) ^ ((value) == null ? 0 : (value).hashCode());
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
	public ShortSet keySet() {
		return new AbstractShortSet() {
			public boolean contains(final short k) {
				return containsKey(k);
			}

			public int size() {
				return AbstractShort2ObjectMap.this.size();
			}

			public void clear() {
				AbstractShort2ObjectMap.this.clear();
			}

			public ShortIterator iterator() {
				return new AbstractShortIterator() {
					final ObjectIterator<Map.Entry<Short, V>> i = entrySet().iterator();

					public short nextShort() {
						return ((Short2ObjectMap.Entry<V>) i.next()).getShortKey();
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
	public ObjectCollection<V> values() {
		return new AbstractObjectCollection<V>() {
			public boolean contains(final Object k) {
				return containsValue(k);
			}

			public int size() {
				return AbstractShort2ObjectMap.this.size();
			}

			public void clear() {
				AbstractShort2ObjectMap.this.clear();
			}

			public ObjectIterator<V> iterator() {
				return new AbstractObjectIterator<V>() {
					final ObjectIterator<Map.Entry<Short, V>> i = entrySet().iterator();

					public V next() {
						return ((Short2ObjectMap.Entry<V>) i.next()).getValue();
					};

					public boolean hasNext() {
						return i.hasNext();
					}
				};
			}
		};
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ObjectSet<Map.Entry<Short, V>> entrySet() {
		return (ObjectSet) short2ObjectEntrySet();
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
		final ObjectIterator<? extends Map.Entry<Short, V>> i = entrySet().iterator();
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
		final ObjectIterator<? extends Map.Entry<Short, V>> i = entrySet().iterator();
		int n = size();
		Short2ObjectMap.Entry<V> e;
		boolean first = true;
		s.append("{");
		while (n-- != 0) {
			if (first)
				first = false;
			else
				s.append(", ");
			e = (Short2ObjectMap.Entry<V>) i.next();
			s.append(String.valueOf(e.getShortKey()));
			s.append("=>");
			if (this == e.getValue())
				s.append("(this map)");
			else
				s.append(String.valueOf(e.getValue()));
		}
		s.append("}");
		return s.toString();
	}
}