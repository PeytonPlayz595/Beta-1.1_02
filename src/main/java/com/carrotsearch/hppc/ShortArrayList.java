package com.carrotsearch.hppc;

import static com.carrotsearch.hppc.Containers.*;

import com.carrotsearch.hppc.cursors.*;
import com.carrotsearch.hppc.predicates.ShortPredicate;
import com.carrotsearch.hppc.procedures.*;
import java.util.*;

/** An array-backed list of shorts. */
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypeArrayList.java")
public class ShortArrayList extends AbstractShortCollection
		implements ShortIndexedContainer, Preallocable, Cloneable, Accountable {
	/** An immutable empty buffer (array). */
	public static final short[] EMPTY_ARRAY = new short[0];

	;

	/** Reuse the same strategy instance. */
	private static final BoundedProportionalArraySizingStrategy DEFAULT_SIZING_STRATEGY = BoundedProportionalArraySizingStrategy.DEFAULT_INSTANCE;

	/**
	 * Internal array for storing the list. The array may be larger than the current
	 * size ({@link #size()}).
	 */
	public short[] buffer = EMPTY_ARRAY;

	/** Current number of elements stored in {@link #buffer}. */
	public int elementsCount;

	/** Buffer resizing strategy. */
	protected final ArraySizingStrategy resizer;

	/** New instance with sane defaults. */
	public ShortArrayList() {
		this(DEFAULT_EXPECTED_ELEMENTS);
	}

	/**
	 * New instance with sane defaults.
	 *
	 * @param expectedElements The expected number of elements guaranteed not to
	 *                         cause buffer expansion (inclusive).
	 */
	public ShortArrayList(int expectedElements) {
		this(expectedElements, DEFAULT_SIZING_STRATEGY);
	}

	/**
	 * New instance with sane defaults.
	 *
	 * @param expectedElements The expected number of elements guaranteed not to
	 *                         cause buffer expansion (inclusive).
	 * @param resizer          Underlying buffer sizing strategy.
	 */
	public ShortArrayList(int expectedElements, ArraySizingStrategy resizer) {
		assert resizer != null;
		this.resizer = resizer;
		buffer = Arrays.copyOf(buffer, expectedElements);
	}

	/**
	 * Creates a new list from the elements of another container in its iteration
	 * order.
	 */
	public ShortArrayList(ShortContainer container) {
		this(container.size());
		addAll(container);
	}

	/** {@inheritDoc} */
	@Override
	public void add(short e1) {
		ensureBufferSpace(1);
		buffer[elementsCount++] = e1;
	}

	/**
	 * Appends two elements at the end of the list. To add more than two elements,
	 * use <code>add
	 * </code> (vararg-version) or access the buffer directly (tight loop).
	 */
	public void add(short e1, short e2) {
		ensureBufferSpace(2);
		buffer[elementsCount++] = e1;
		buffer[elementsCount++] = e2;
	}

	/** Add all elements from a range of given array to the list. */
	public void add(short[] elements, int start, int length) {
		assert length >= 0 : "Length must be >= 0";

		ensureBufferSpace(length);
		System.arraycopy(elements, start, buffer, elementsCount, length);
		elementsCount += length;
	}

	/**
	 * Vararg-signature method for adding elements at the end of the list.
	 *
	 * <p>
	 * <b>This method is handy, but costly if used in tight loops (anonymous array
	 * passing)</b>
	 */
	public final void add(short... elements) {
		add(elements, 0, elements.length);
	}

	/** Adds all elements from another container. */
	public int addAll(ShortContainer container) {
		final int size = container.size();
		ensureBufferSpace(size);

		for (ShortCursor cursor : container) {
			add(cursor.value);
		}

		return size;
	}

	/** Adds all elements from another iterable. */
	public int addAll(Iterable<? extends ShortCursor> iterable) {
		int size = 0;
		for (ShortCursor cursor : iterable) {
			add(cursor.value);
			size++;
		}
		return size;
	}

	/** {@inheritDoc} */
	@Override
	public void insert(int index, short e1) {
		assert (index >= 0 && index <= size()) : "Index " + index + " out of bounds [" + 0 + ", " + size() + "].";

		ensureBufferSpace(1);
		System.arraycopy(buffer, index, buffer, index + 1, elementsCount - index);
		buffer[index] = e1;
		elementsCount++;
	}

	/** {@inheritDoc} */
	@Override
	public short get(int index) {
		assert (index >= 0 && index < size()) : "Index " + index + " out of bounds [" + 0 + ", " + size() + ").";

		return buffer[index];
	}

	/** {@inheritDoc} */
	@Override
	public short set(int index, short e1) {
		assert (index >= 0 && index < size()) : "Index " + index + " out of bounds [" + 0 + ", " + size() + ").";

		final short v = buffer[index];
		buffer[index] = e1;
		return v;
	}

	/** {@inheritDoc} */
	@Override
	public short removeAt(int index) {
		assert (index >= 0 && index < size()) : "Index " + index + " out of bounds [" + 0 + ", " + size() + ").";

		final short v = buffer[index];
		System.arraycopy(buffer, index + 1, buffer, index, --elementsCount - index);

		return v;
	}

	/** {@inheritDoc} */
	@Override
	public short removeLast() {
		assert elementsCount > 0;

		final short v = buffer[--elementsCount];

		return v;
	}

	/** {@inheritDoc} */
	@Override
	public void removeRange(int fromIndex, int toIndex) {
		assert (fromIndex >= 0 && fromIndex <= size())
				: "Index " + fromIndex + " out of bounds [" + 0 + ", " + size() + ").";
		assert (toIndex >= 0 && toIndex <= size()) : "Index " + toIndex + " out of bounds [" + 0 + ", " + size() + "].";
		assert fromIndex <= toIndex : "fromIndex must be <= toIndex: " + fromIndex + ", " + toIndex;

		System.arraycopy(buffer, toIndex, buffer, fromIndex, elementsCount - toIndex);
		final int count = toIndex - fromIndex;
		elementsCount -= count;
	}

	/** {@inheritDoc} */
	@Override
	public boolean removeElement(short e1) {
		return removeFirst(e1) != -1;
	}

	/** {@inheritDoc} */
	@Override
	public int removeFirst(short e1) {
		final int index = indexOf(e1);
		if (index >= 0)
			removeAt(index);
		return index;
	}

	/** {@inheritDoc} */
	@Override
	public int removeLast(short e1) {
		final int index = lastIndexOf(e1);
		if (index >= 0)
			removeAt(index);
		return index;
	}

	/** {@inheritDoc} */
	@Override
	public int removeAll(short e1) {
		int to = 0;
		for (int from = 0; from < elementsCount; from++) {
			if (((e1) == (buffer[from]))) {
				continue;
			}
			if (to != from) {
				buffer[to] = buffer[from];
			}
			to++;
		}
		final int deleted = elementsCount - to;
		this.elementsCount = to;

		return deleted;
	}

	/** {@inheritDoc} */
	@Override
	public boolean contains(short e1) {
		return indexOf(e1) >= 0;
	}

	/** {@inheritDoc} */
	@Override
	public int indexOf(short e1) {
		for (int i = 0; i < elementsCount; i++) {
			if (((e1) == (buffer[i]))) {
				return i;
			}
		}

		return -1;
	}

	/** {@inheritDoc} */
	@Override
	public int lastIndexOf(short e1) {
		for (int i = elementsCount - 1; i >= 0; i--) {
			if (((e1) == (buffer[i]))) {
				return i;
			}
		}

		return -1;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return elementsCount == 0;
	}

	/**
	 * Ensure this container can hold at least the given number of elements without
	 * resizing its buffers.
	 *
	 * @param expectedElements The total number of elements, inclusive.
	 */
	@Override
	public void ensureCapacity(int expectedElements) {
		final int bufferLen = (buffer == null ? 0 : buffer.length);
		if (expectedElements > bufferLen) {
			ensureBufferSpace(expectedElements - size());
		}
	}

	/**
	 * Ensures the internal buffer has enough free slots to store
	 * <code>expectedAdditions</code>. Increases internal buffer size if needed.
	 */
	protected void ensureBufferSpace(int expectedAdditions) {
		final int bufferLen = (buffer == null ? 0 : buffer.length);
		if (elementsCount + expectedAdditions > bufferLen) {
			final int newSize = resizer.grow(bufferLen, elementsCount, expectedAdditions);
			assert newSize >= elementsCount + expectedAdditions : "Resizer failed to" + " return sensible new size: "
					+ newSize + " <= " + (elementsCount + expectedAdditions);

			this.buffer = Arrays.copyOf(buffer, newSize);
		}
	}

	/**
	 * Truncate or expand the list to the new size. If the list is truncated, the
	 * buffer will not be reallocated (use {@link #trimToSize()} if you need a
	 * truncated buffer), but the truncated values will be reset to the default
	 * value (zero). If the list is expanded, the elements beyond the current size
	 * are initialized with JVM-defaults (zero or <code>null</code> values).
	 */
	public void resize(int newSize) {
		if (newSize <= buffer.length) {
			if (newSize < elementsCount) {
				Arrays.fill(buffer, newSize, elementsCount, ((short) 0));
			} else {
				Arrays.fill(buffer, elementsCount, newSize, ((short) 0));
			}
		} else {
			ensureCapacity(newSize);
		}
		this.elementsCount = newSize;
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return elementsCount;
	}

	/** Trim the internal buffer to the current size. */
	public void trimToSize() {
		if (size() != this.buffer.length) {
			this.buffer = toArray();
		}
	}

	/**
	 * Sets the number of stored elements to zero. Releases and initializes the
	 * internal storage array to default values. To clear the list without cleaning
	 * the buffer, simply set the {@link #elementsCount} field to zero.
	 */
	@Override
	public void clear() {
		Arrays.fill(buffer, 0, elementsCount, ((short) 0));
		this.elementsCount = 0;
	}

	/**
	 * Sets the number of stored elements to zero and releases the internal storage
	 * array.
	 */
	@Override
	public void release() {
		this.buffer = EMPTY_ARRAY;
		this.elementsCount = 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The returned array is sized to match exactly the number of elements of the
	 * stack.
	 */
	@Override
	public short[] toArray() {

		return Arrays.copyOf(buffer, elementsCount);
	}

	/** {@inheritDoc} */
	@Override
	public ShortIndexedContainer sort() {
		Arrays.sort(buffer, 0, elementsCount);
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public ShortIndexedContainer reverse() {
		for (int i = 0, mid = elementsCount >> 1, j = elementsCount - 1; i < mid; i++, j--) {
			short tmp = buffer[i];
			buffer[i] = buffer[j];
			buffer[j] = tmp;
		}
		return this;
	}

	/**
	 * Clone this object. The returned clone will reuse the same hash function and
	 * array resizing strategy.
	 */
	@Override
	public ShortArrayList clone() {
		try {

			final ShortArrayList cloned = (ShortArrayList) super.clone();
			cloned.buffer = buffer.clone();
			return cloned;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		int h = 1, max = elementsCount;
		for (int i = 0; i < max; i++) {
			h = 31 * h + BitMixer.mix(this.buffer[i]);
		}
		return h;
	}

	/**
	 * Returns <code>true</code> only if the other object is an instance of the same
	 * class and with the same elements.
	 */
	@Override
	public boolean equals(Object obj) {
		return (this == obj) || (obj != null && getClass() == obj.getClass() && equalElements(getClass().cast(obj)));
	}

	/**
	 * Compare index-aligned elements against another {@link ShortIndexedContainer}.
	 */
	protected boolean equalElements(ShortArrayList other) {
		int max = size();
		if (other.size() != max) {
			return false;
		}

		for (int i = 0; i < max; i++) {
			if (!((get(i)) == (other.get(i)))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public long ramBytesAllocated() {
		// int: elementsCount
		return RamUsageEstimator.NUM_BYTES_OBJECT_HEADER + Integer.BYTES + resizer.ramBytesAllocated()
				+ RamUsageEstimator.shallowSizeOfArray(buffer);
	}

	@Override
	public long ramBytesUsed() {
		// int: elementsCount
		return RamUsageEstimator.NUM_BYTES_OBJECT_HEADER + Integer.BYTES + resizer.ramBytesUsed()
				+ RamUsageEstimator.shallowUsedSizeOfArray(buffer, elementsCount);
	}

	/** An iterator implementation for {@link ShortArrayList#iterator}. */
	static final class ValueIterator extends AbstractIterator<ShortCursor> {
		private final ShortCursor cursor;

		private final short[] buffer;
		private final int size;

		public ValueIterator(short[] buffer, int size) {
			this.cursor = new ShortCursor();
			this.cursor.index = -1;
			this.size = size;
			this.buffer = buffer;
		}

		@Override
		protected ShortCursor fetch() {
			if (cursor.index + 1 == size)
				return done();

			cursor.value = buffer[++cursor.index];
			return cursor;
		}
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<ShortCursor> iterator() {
		return new ValueIterator(buffer, size());
	}

	/** {@inheritDoc} */
	@Override
	public <T extends ShortProcedure> T forEach(T procedure) {
		return forEach(procedure, 0, size());
	}

	/**
	 * Applies <code>procedure</code> to a slice of the list,
	 * <code>fromIndex</code>, inclusive, to <code>toIndex</code>, exclusive.
	 */
	public <T extends ShortProcedure> T forEach(T procedure, int fromIndex, final int toIndex) {
		assert (fromIndex >= 0 && fromIndex <= size())
				: "Index " + fromIndex + " out of bounds [" + 0 + ", " + size() + ").";

		assert (toIndex >= 0 && toIndex <= size()) : "Index " + toIndex + " out of bounds [" + 0 + ", " + size() + "].";

		assert fromIndex <= toIndex : "fromIndex must be <= toIndex: " + fromIndex + ", " + toIndex;

		final short[] buffer = this.buffer;
		for (int i = fromIndex; i < toIndex; i++) {
			procedure.apply(buffer[i]);
		}

		return procedure;
	}

	/** {@inheritDoc} */
	@Override
	public int removeAll(ShortPredicate predicate) {
		final short[] buffer = this.buffer;
		final int elementsCount = this.elementsCount;
		int to = 0;
		int from = 0;
		try {
			for (; from < elementsCount; from++) {
				if (predicate.apply(buffer[from])) {
					buffer[from] = ((short) 0);
					continue;
				}

				if (to != from) {
					buffer[to] = buffer[from];
					buffer[from] = ((short) 0);
				}
				to++;
			}
		} finally {
			// Keep the list in a consistent state, even if the predicate throws an
			// exception.
			for (; from < elementsCount; from++) {
				if (to != from) {
					buffer[to] = buffer[from];
					buffer[from] = ((short) 0);
				}
				to++;
			}

			this.elementsCount = to;
		}

		return elementsCount - to;
	}

	/** {@inheritDoc} */
	@Override
	public <T extends ShortPredicate> T forEach(T predicate) {
		return forEach(predicate, 0, size());
	}

	/**
	 * Applies <code>predicate</code> to a slice of the list,
	 * <code>fromIndex</code>, inclusive, to <code>toIndex</code>, exclusive, or
	 * until predicate returns <code>false</code>.
	 */
	public <T extends ShortPredicate> T forEach(T predicate, int fromIndex, final int toIndex) {
		assert (fromIndex >= 0 && fromIndex <= size())
				: "Index " + fromIndex + " out of bounds [" + 0 + ", " + size() + ").";
		assert (toIndex >= 0 && toIndex <= size()) : "Index " + toIndex + " out of bounds [" + 0 + ", " + size() + "].";
		assert fromIndex <= toIndex : "fromIndex must be <= toIndex: " + fromIndex + ", " + toIndex;

		final short[] buffer = this.buffer;
		for (int i = fromIndex; i < toIndex; i++) {
			if (!predicate.apply(buffer[i]))
				break;
		}

		return predicate;
	}

	/**
	 * Create a list from a variable number of arguments or an array of
	 * <code>short</code>. The elements are copied from the argument to the internal
	 * buffer.
	 */
	public static ShortArrayList from(short... elements) {
		final ShortArrayList list = new ShortArrayList(elements.length);
		list.add(elements);
		return list;
	}
}
