package com.carrotsearch.hppc;

import com.carrotsearch.hppc.comparators.*;
import com.carrotsearch.hppc.cursors.*;
import com.carrotsearch.hppc.predicates.*;
import com.carrotsearch.hppc.procedures.*;
import com.carrotsearch.hppc.sorting.QuickSort;
import java.util.Iterator;
import java.util.function.IntBinaryOperator;

/**
 * Read-only view with sorted iteration order on a delegate
 * {@link IntCharHashMap}.
 *
 * <p>
 * In its constructor, this view creates its own iteration order array and sorts
 * it, which is in O(n.log(n)) of the size of the delegate map. Afterward, calls
 * to any method have the same performance as the delegate map.
 *
 * <p>
 * This view is read-only. In addition, the delegate map must not be modified
 * while the view is used, otherwise the iteration is undefined.
 *
 * <p>
 * Since this view provides a fixed iteration order, it must not be used to add
 * entries to another {@link IntCharHashMap} as this may result in a runtime
 * deadlock. See
 * <a href="https://github.com/carrotsearch/hppc/issues/146">HPPC-103</a> and
 * <a href="https://github.com/carrotsearch/hppc/issues/228">HPPC-186</a> for
 * more information.
 */
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "SortedIterationKTypeVTypeHashMap.java")
public class SortedIterationIntCharHashMap implements IntCharMap {
	public final IntCharHashMap delegate;
	public final int[] iterationOrder;

	/**
	 * Creates a read-only view with sorted iteration order on the given delegate
	 * map. The ordering is based on the provided comparator on the keys.
	 */
	public SortedIterationIntCharHashMap(IntCharHashMap delegate, IntComparator comparator) {
		this.delegate = delegate;
		this.iterationOrder = sortIterationOrder(createEntryIndexes(), comparator);
	}

	/**
	 * Creates a read-only view with sorted iteration order on the given delegate
	 * map. The ordering is based on the provided comparator on keys and values.
	 */
	public SortedIterationIntCharHashMap(IntCharHashMap delegate, IntCharComparator comparator) {
		this.delegate = delegate;
		this.iterationOrder = sortIterationOrder(createEntryIndexes(), comparator);
	}

	private int[] createEntryIndexes() {
		final int[] keys = delegate.keys;
		final int size = delegate.size();
		int[] entryIndexes = new int[size];
		int entry = 0;
		if (delegate.hasEmptyKey) {
			entryIndexes[entry++] = delegate.mask + 1;
		}
		for (int keyIndex = 0; entry < size; keyIndex++) {
			if (!((keys[keyIndex]) == 0)) {
				entryIndexes[entry++] = keyIndex;
			}
		}
		return entryIndexes;
	}

	/**
	 * Sort the iteration order array based on the provided comparator on the keys.
	 */
	protected int[] sortIterationOrder(int[] entryIndexes, IntComparator comparator) {
		QuickSort.sort(entryIndexes, (i, j) -> {
			int[] keys = delegate.keys;
			return comparator.compare(keys[entryIndexes[i]], keys[entryIndexes[j]]);
		});
		return entryIndexes;
	}

	/**
	 * Sort the iteration order array based on the provided comparator on keys and
	 * values.
	 */
	protected int[] sortIterationOrder(int[] entryIndexes, IntCharComparator comparator) {
		QuickSort.sort(entryIndexes, new IntBinaryOperator() {
			final int[] keys = delegate.keys;
			final char[] values = delegate.values;

			@Override
			public int applyAsInt(int i, int j) {
				int index1 = entryIndexes[i];
				int index2 = entryIndexes[j];
				return comparator.compare(keys[index1], values[index1], keys[index2], values[index2]);
			}
		});
		return entryIndexes;
	}

	@Override
	public Iterator<IntCharCursor> iterator() {
		assert checkUnmodified();
		return new EntryIterator();
	}

	@Override
	public boolean containsKey(int key) {
		return delegate.containsKey(key);
	}

	@Override
	public int size() {
		assert checkUnmodified();
		return delegate.size();
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public int removeAll(IntContainer container) {
		throw readOnlyException();
	}

	@Override
	public int removeAll(IntPredicate predicate) {
		throw readOnlyException();
	}

	@Override
	public int removeAll(IntCharPredicate predicate) {
		throw readOnlyException();
	}

	@Override
	public <T extends IntCharProcedure> T forEach(T procedure) {
		assert checkUnmodified();
		final int[] iterationOrder = this.iterationOrder;
		final int[] keys = delegate.keys;
		final char[] values = delegate.values;
		for (int i = 0, size = size(); i < size; i++) {
			int slot = iterationOrder[i];
			procedure.apply(keys[slot], values[slot]);
		}
		return procedure;
	}

	@Override
	public <T extends IntCharPredicate> T forEach(T predicate) {
		assert checkUnmodified();
		final int[] iterationOrder = this.iterationOrder;
		final int[] keys = delegate.keys;
		final char[] values = delegate.values;
		for (int i = 0, size = size(); i < size; i++) {
			int slot = iterationOrder[i];
			if (!predicate.apply(keys[slot], values[slot])) {
				break;
			}
		}
		return predicate;
	}

	@Override
	public IntCollection keys() {
		assert checkUnmodified();
		return new KeysContainer();
	}

	@Override
	public CharContainer values() {
		assert checkUnmodified();
		return new ValuesContainer();
	}

	@Override
	public char get(int key) {
		return delegate.get(key);
	}

	@Override
	public char getOrDefault(int key, char defaultValue) {
		return delegate.getOrDefault(key, defaultValue);
	}

	@Override
	public char put(int key, char value) {
		throw readOnlyException();
	}

	@Override
	public int putAll(IntCharAssociativeContainer container) {
		throw readOnlyException();
	}

	@Override
	public int putAll(Iterable<? extends IntCharCursor> iterable) {
		throw readOnlyException();
	}

	@Override
	public char putOrAdd(int key, char putValue, char incrementValue) {
		throw readOnlyException();
	}

	@Override
	public char addTo(int key, char additionValue) {
		throw readOnlyException();
	}

	@Override
	public char remove(int key) {
		throw readOnlyException();
	}

	@Override
	public int indexOf(int key) {
		return delegate.indexOf(key);
	}

	@Override
	public boolean indexExists(int index) {
		return delegate.indexExists(index);
	}

	@Override
	public char indexGet(int index) {
		return delegate.indexGet(index);
	}

	@Override
	public char indexReplace(int index, char newValue) {
		throw readOnlyException();
	}

	@Override
	public void indexInsert(int index, int key, char value) {
		throw readOnlyException();
	}

	@Override
	public char indexRemove(int index) {
		throw readOnlyException();
	}

	@Override
	public void clear() {
		throw readOnlyException();
	}

	@Override
	public void release() {
		throw readOnlyException();
	}

	@Override
	public String visualizeKeyDistribution(int characters) {
		return delegate.visualizeKeyDistribution(characters);
	}

	private static RuntimeException readOnlyException() {
		throw new UnsupportedOperationException("Read-only view cannot be modified");
	}

	private boolean checkUnmodified() {
		// Cheap size comparison.
		// We could also check the hashcode, but this is heavy for a frequent check.
		assert delegate.size() == iterationOrder.length
				: "The delegate map changed; this is not supported by this read-only view";
		return true;
	}

	/** An iterator implementation for {@link #iterator}. */
	private final class EntryIterator extends AbstractIterator<IntCharCursor> {
		private final IntCharCursor cursor = new IntCharCursor();
		private int index;

		@Override
		protected IntCharCursor fetch() {
			if (index < iterationOrder.length) {
				int slot = iterationOrder[index++];
				cursor.index = slot;
				cursor.key = delegate.keys[slot];
				cursor.value = delegate.values[slot];
				return cursor;
			}
			return done();
		}
	}

	/** A view of the keys in sorted order. */
	private final class KeysContainer extends AbstractIntCollection implements IntLookupContainer {
		private final SortedIterationIntCharHashMap owner = SortedIterationIntCharHashMap.this;

		@Override
		public boolean contains(int e) {
			return owner.containsKey(e);
		}

		@Override
		public <T extends IntProcedure> T forEach(final T procedure) {
			owner.forEach((IntCharProcedure) (k, v) -> procedure.apply(k));
			return procedure;
		}

		@Override
		public <T extends IntPredicate> T forEach(final T predicate) {
			owner.forEach((IntCharPredicate) (key, value) -> predicate.apply(key));
			return predicate;
		}

		@Override
		public boolean isEmpty() {
			return owner.isEmpty();
		}

		@Override
		public Iterator<IntCursor> iterator() {
			return new KeysIterator();
		}

		@Override
		public int size() {
			return owner.size();
		}

		@Override
		public void clear() {
			throw readOnlyException();
		}

		@Override
		public void release() {
			throw readOnlyException();
		}

		@Override
		public int removeAll(IntPredicate predicate) {
			throw readOnlyException();
		}

		@Override
		public int removeAll(final int e) {
			throw readOnlyException();
		}
	}

	/** A sorted iterator over the set of assigned keys. */
	private final class KeysIterator extends AbstractIterator<IntCursor> {
		private final IntCursor cursor = new IntCursor();
		private int index;

		@Override
		protected IntCursor fetch() {
			if (index < iterationOrder.length) {
				int slot = iterationOrder[index++];
				cursor.index = slot;
				cursor.value = delegate.keys[slot];
				return cursor;
			}
			return done();
		}
	}

	/** A view of the values in sorted order. */
	private final class ValuesContainer extends AbstractCharCollection {
		private final SortedIterationIntCharHashMap owner = SortedIterationIntCharHashMap.this;

		@Override
		public int size() {
			return owner.size();
		}

		@Override
		public boolean isEmpty() {
			return owner.isEmpty();
		}

		@Override
		public boolean contains(char value) {
			for (IntCharCursor c : owner) {
				if (((value) == (c.value))) {
					return true;
				}
			}
			return false;
		}

		@Override
		public <T extends CharProcedure> T forEach(T procedure) {
			owner.forEach((IntCharProcedure) (k, v) -> procedure.apply(v));
			return procedure;
		}

		@Override
		public <T extends CharPredicate> T forEach(T predicate) {
			owner.forEach((IntCharPredicate) (k, v) -> predicate.apply(v));
			return predicate;
		}

		@Override
		public Iterator<CharCursor> iterator() {
			return new ValuesIterator();
		}

		@Override
		public int removeAll(final char e) {
			throw readOnlyException();
		}

		@Override
		public int removeAll(final CharPredicate predicate) {
			throw readOnlyException();
		}

		@Override
		public void clear() {
			throw readOnlyException();
		}

		@Override
		public void release() {
			throw readOnlyException();
		}
	}

	/** A sorted iterator over the set of assigned values. */
	private final class ValuesIterator extends AbstractIterator<CharCursor> {
		private final CharCursor cursor = new CharCursor();
		private int index;

		@Override
		protected CharCursor fetch() {
			if (index < iterationOrder.length) {
				int slot = iterationOrder[index++];
				cursor.index = slot;
				cursor.value = delegate.values[slot];
				return cursor;
			}
			return done();
		}
	}
}
