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
package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntCollections;
import it.unimi.dsi.fastutil.ints.IntSets;
import java.util.Map;

/**
 * A class providing static methods and objects that do useful things with
 * type-specific maps.
 *
 * @see it.unimi.dsi.fastutil.Maps
 * @see java.util.Collections
 */
public class Long2IntMaps {
	private Long2IntMaps() {
	}

	/**
	 * An immutable class representing an empty type-specific map.
	 *
	 * <P>
	 * This class may be useful to implement your own in case you subclass a
	 * type-specific map.
	 */
	public static class EmptyMap extends Long2IntFunctions.EmptyFunction
			implements Long2IntMap, java.io.Serializable, Cloneable {
		private static final long serialVersionUID = -7046029254386353129L;

		protected EmptyMap() {
		}

		public boolean containsValue(final int v) {
			return false;
		}

		public void putAll(final Map<? extends Long, ? extends Integer> m) {
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("unchecked")
		public ObjectSet<Long2IntMap.Entry> long2IntEntrySet() {
			return ObjectSets.EMPTY_SET;
		}

		public LongSet keySet() {
			return LongSets.EMPTY_SET;
		}

		public IntCollection values() {
			return IntSets.EMPTY_SET;
		}

		public boolean containsValue(final Object ov) {
			return false;
		}

		private Object readResolve() {
			return EMPTY_MAP;
		}

		public Object clone() {
			return EMPTY_MAP;
		}

		public boolean isEmpty() {
			return true;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public ObjectSet<Map.Entry<Long, Integer>> entrySet() {
			return (ObjectSet) long2IntEntrySet();
		}

		public int hashCode() {
			return 0;
		}

		public boolean equals(final Object o) {
			if (!(o instanceof Map))
				return false;

			return ((Map<?, ?>) o).isEmpty();
		}

		public String toString() {
			return "{}";
		}
	}

	/** An empty type-specific map (immutable). It is serializable and cloneable. */

	public static final EmptyMap EMPTY_MAP = new EmptyMap();

	/**
	 * An immutable class representing a type-specific singleton map.
	 *
	 * <P>
	 * This class may be useful to implement your own in case you subclass a
	 * type-specific map.
	 */

	public static class Singleton extends Long2IntFunctions.Singleton
			implements Long2IntMap, java.io.Serializable, Cloneable {

		private static final long serialVersionUID = -7046029254386353129L;

		protected transient volatile ObjectSet<Long2IntMap.Entry> entries;
		protected transient volatile LongSet keys;
		protected transient volatile IntCollection values;

		protected Singleton(final long key, final int value) {
			super(key, value);
		}

		public boolean containsValue(final int v) {
			return ((value) == (v));
		}

		public boolean containsValue(final Object ov) {
			return ((((((Integer) (ov)).intValue()))) == (value));
		}

		public void putAll(final Map<? extends Long, ? extends Integer> m) {
			throw new UnsupportedOperationException();
		}

		public ObjectSet<Long2IntMap.Entry> long2IntEntrySet() {
			if (entries == null)
				entries = ObjectSets.singleton((Long2IntMap.Entry) new SingletonEntry());
			return entries;
		}

		public LongSet keySet() {
			if (keys == null)
				keys = LongSets.singleton(key);
			return keys;
		}

		public IntCollection values() {
			if (values == null)
				values = IntSets.singleton(value);
			return values;
		}

		protected class SingletonEntry implements Long2IntMap.Entry, Map.Entry<Long, Integer> {
			public Long getKey() {
				return (Long.valueOf(Singleton.this.key));
			}

			public Integer getValue() {
				return (Integer.valueOf(Singleton.this.value));
			}

			public long getLongKey() {
				return Singleton.this.key;
			}

			public int getIntValue() {
				return Singleton.this.value;
			}

			public int setValue(final int value) {
				throw new UnsupportedOperationException();
			}

			public Integer setValue(final Integer value) {
				throw new UnsupportedOperationException();
			}

			public boolean equals(final Object o) {
				if (!(o instanceof Map.Entry))
					return false;
				Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;

				return ((Singleton.this.key) == (((((Long) (e.getKey())).longValue()))))
						&& ((Singleton.this.value) == (((((Integer) (e.getValue())).intValue()))));
			}

			public int hashCode() {
				return it.unimi.dsi.fastutil.HashCommon.long2int(Singleton.this.key) ^ (Singleton.this.value);
			}

			public String toString() {
				return Singleton.this.key + "->" + Singleton.this.value;
			}
		}

		public boolean isEmpty() {
			return false;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public ObjectSet<Map.Entry<Long, Integer>> entrySet() {
			return (ObjectSet) long2IntEntrySet();
		}

		public int hashCode() {
			return it.unimi.dsi.fastutil.HashCommon.long2int(key) ^ (value);
		}

		public boolean equals(final Object o) {
			if (o == this)
				return true;
			if (!(o instanceof Map))
				return false;

			Map<?, ?> m = (Map<?, ?>) o;
			if (m.size() != 1)
				return false;
			return entrySet().iterator().next().equals(m.entrySet().iterator().next());
		}

		public String toString() {
			return "{" + key + "=>" + value + "}";
		}
	}

	/**
	 * Returns a type-specific immutable map containing only the specified pair. The
	 * returned map is serializable and cloneable.
	 *
	 * <P>
	 * Note that albeit the returned map is immutable, its default return value may
	 * be changed.
	 *
	 * @param key   the only key of the returned map.
	 * @param value the only value of the returned map.
	 * @return a type-specific immutable map containing just the pair
	 *         <code>&lt;key,value></code>.
	 */

	public static Long2IntMap singleton(final long key, int value) {
		return new Singleton(key, value);
	}

	/**
	 * Returns a type-specific immutable map containing only the specified pair. The
	 * returned map is serializable and cloneable.
	 *
	 * <P>
	 * Note that albeit the returned map is immutable, its default return value may
	 * be changed.
	 *
	 * @param key   the only key of the returned map.
	 * @param value the only value of the returned map.
	 * @return a type-specific immutable map containing just the pair
	 *         <code>&lt;key,value></code>.
	 */

	public static Long2IntMap singleton(final Long key, final Integer value) {
		return new Singleton(((key).longValue()), ((value).intValue()));
	}

	/** A synchronized wrapper class for maps. */

	public static class SynchronizedMap extends Long2IntFunctions.SynchronizedFunction
			implements Long2IntMap, java.io.Serializable {

		private static final long serialVersionUID = -7046029254386353129L;

		protected final Long2IntMap map;

		protected transient volatile ObjectSet<Long2IntMap.Entry> entries;
		protected transient volatile LongSet keys;
		protected transient volatile IntCollection values;

		protected SynchronizedMap(final Long2IntMap m, final Object sync) {
			super(m, sync);
			this.map = m;
		}

		protected SynchronizedMap(final Long2IntMap m) {
			super(m);
			this.map = m;
		}

		public int size() {
			synchronized (sync) {
				return map.size();
			}
		}

		public boolean containsKey(final long k) {
			synchronized (sync) {
				return map.containsKey(k);
			}
		}

		public boolean containsValue(final int v) {
			synchronized (sync) {
				return map.containsValue(v);
			}
		}

		public int defaultReturnValue() {
			synchronized (sync) {
				return map.defaultReturnValue();
			}
		}

		public void defaultReturnValue(final int defRetValue) {
			synchronized (sync) {
				map.defaultReturnValue(defRetValue);
			}
		}

		public int put(final long k, final int v) {
			synchronized (sync) {
				return map.put(k, v);
			}
		}

		// public void putAll( final MAP KEY_VALUE_EXTENDS_GENERIC c ) { synchronized(
		// sync ) { map.putAll( c ); } }
		public void putAll(final Map<? extends Long, ? extends Integer> m) {
			synchronized (sync) {
				map.putAll(m);
			}
		}

		public ObjectSet<Long2IntMap.Entry> long2IntEntrySet() {
			if (entries == null)
				entries = ObjectSets.synchronize(map.long2IntEntrySet(), sync);
			return entries;
		}

		public LongSet keySet() {
			if (keys == null)
				keys = LongSets.synchronize(map.keySet(), sync);
			return keys;
		}

		public IntCollection values() {
			if (values == null)
				return IntCollections.synchronize(map.values(), sync);
			return values;
		}

		public void clear() {
			synchronized (sync) {
				map.clear();
			}
		}

		public String toString() {
			synchronized (sync) {
				return map.toString();
			}
		}

		public Integer put(final Long k, final Integer v) {
			synchronized (sync) {
				return map.put(k, v);
			}
		}

		public int remove(final long k) {
			synchronized (sync) {
				return map.remove(k);
			}
		}

		public int get(final long k) {
			synchronized (sync) {
				return map.get(k);
			}
		}

		public boolean containsKey(final Object ok) {
			synchronized (sync) {
				return map.containsKey(ok);
			}
		}

		public boolean containsValue(final Object ov) {
			synchronized (sync) {
				return map.containsValue(ov);
			}
		}

		public boolean isEmpty() {
			synchronized (sync) {
				return map.isEmpty();
			}
		}

		public ObjectSet<Map.Entry<Long, Integer>> entrySet() {
			synchronized (sync) {
				return map.entrySet();
			}
		}

		public int hashCode() {
			synchronized (sync) {
				return map.hashCode();
			}
		}

		public boolean equals(final Object o) {
			synchronized (sync) {
				return map.equals(o);
			}
		}
	}

	/**
	 * Returns a synchronized type-specific map backed by the given type-specific
	 * map.
	 *
	 * @param m the map to be wrapped in a synchronized map.
	 * @return a synchronized view of the specified map.
	 * @see java.util.Collections#synchronizedMap(Map)
	 */
	public static Long2IntMap synchronize(final Long2IntMap m) {
		return new SynchronizedMap(m);
	}

	/**
	 * Returns a synchronized type-specific map backed by the given type-specific
	 * map, using an assigned object to synchronize.
	 *
	 * @param m    the map to be wrapped in a synchronized map.
	 * @param sync an object that will be used to synchronize the access to the map.
	 * @return a synchronized view of the specified map.
	 * @see java.util.Collections#synchronizedMap(Map)
	 */

	public static Long2IntMap synchronize(final Long2IntMap m, final Object sync) {
		return new SynchronizedMap(m, sync);
	}

	/** An unmodifiable wrapper class for maps. */

	public static class UnmodifiableMap extends Long2IntFunctions.UnmodifiableFunction
			implements Long2IntMap, java.io.Serializable {

		private static final long serialVersionUID = -7046029254386353129L;

		protected final Long2IntMap map;

		protected transient volatile ObjectSet<Long2IntMap.Entry> entries;
		protected transient volatile LongSet keys;
		protected transient volatile IntCollection values;

		protected UnmodifiableMap(final Long2IntMap m) {
			super(m);
			this.map = m;
		}

		public int size() {
			return map.size();
		}

		public boolean containsKey(final long k) {
			return map.containsKey(k);
		}

		public boolean containsValue(final int v) {
			return map.containsValue(v);
		}

		public int defaultReturnValue() {
			throw new UnsupportedOperationException();
		}

		public void defaultReturnValue(final int defRetValue) {
			throw new UnsupportedOperationException();
		}

		public int put(final long k, final int v) {
			throw new UnsupportedOperationException();
		}

		// public void putAll( final MAP KEY_VALUE_EXTENDS_GENERIC c ) { throw new
		// UnsupportedOperationException(); }
		public void putAll(final Map<? extends Long, ? extends Integer> m) {
			throw new UnsupportedOperationException();
		}

		public ObjectSet<Long2IntMap.Entry> long2IntEntrySet() {
			if (entries == null)
				entries = ObjectSets.unmodifiable(map.long2IntEntrySet());
			return entries;
		}

		public LongSet keySet() {
			if (keys == null)
				keys = LongSets.unmodifiable(map.keySet());
			return keys;
		}

		public IntCollection values() {
			if (values == null)
				return IntCollections.unmodifiable(map.values());
			return values;
		}

		public void clear() {
			throw new UnsupportedOperationException();
		}

		public String toString() {
			return map.toString();
		}

		public Integer put(final Long k, final Integer v) {
			throw new UnsupportedOperationException();
		}

		public int remove(final long k) {
			throw new UnsupportedOperationException();
		}

		public int get(final long k) {
			return map.get(k);
		}

		public boolean containsKey(final Object ok) {
			return map.containsKey(ok);
		}

		public boolean containsValue(final Object ov) {
			return map.containsValue(ov);
		}

		public boolean isEmpty() {
			return map.isEmpty();
		}

		public ObjectSet<Map.Entry<Long, Integer>> entrySet() {
			return ObjectSets.unmodifiable(map.entrySet());
		}
	}

	/**
	 * Returns an unmodifiable type-specific map backed by the given type-specific
	 * map.
	 *
	 * @param m the map to be wrapped in an unmodifiable map.
	 * @return an unmodifiable view of the specified map.
	 * @see java.util.Collections#unmodifiableMap(Map)
	 */
	public static Long2IntMap unmodifiable(final Long2IntMap m) {
		return new UnmodifiableMap(m);
	}

}