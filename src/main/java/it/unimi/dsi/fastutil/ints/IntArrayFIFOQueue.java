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
 * Copyright (C) 2010-2013 Sebastiano Vigna 
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
package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Arrays;
import java.util.NoSuchElementException;

/**
 * A type-specific array-based FIFO queue, supporting also deque operations.
 *
 * <P>
 * Instances of this class represent a FIFO queue using a backing array in a
 * circular way. The array is enlarged and shrunk as needed. You can use the
 * {@link #trim()} method to reduce its memory usage, if necessary.
 *
 * <P>
 * This class provides additional methods that implement a <em>deque</em>
 * (double-ended queue).
 */
public class IntArrayFIFOQueue extends AbstractIntPriorityQueue {
	/** The standard initial capacity of a queue. */
	public final static int INITIAL_CAPACITY = 4;
	/** The backing array. */
	protected int array[] = IntArrays.EMPTY_ARRAY;
	/** The current (cached) length of {@link #array}. */
	protected int length;
	/**
	 * The start position in {@link #array}. It is always strictly smaller than
	 * {@link #length}.
	 */
	protected int start;
	/**
	 * The end position in {@link #array}. It is always strictly smaller than
	 * {@link #length}. Might be actually smaller than {@link #start} because
	 * {@link #array} is used cyclically.
	 */
	protected int end;

	/**
	 * Creates a new empty queue with given capacity.
	 *
	 * @param capacity the initial capacity of this queue.
	 */
	public IntArrayFIFOQueue(final int capacity) {
		if (capacity < 0)
			throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
		array = new int[capacity];
		length = capacity;
	}

	/**
	 * Creates a new empty queue with standard {@linkplain #INITIAL_CAPACITY initial
	 * capacity}.
	 */
	public IntArrayFIFOQueue() {
		this(INITIAL_CAPACITY);
	}

	/**
	 * Returns <code>null</code> (FIFO queues have no comparator).
	 * 
	 * @return <code>null</code>.
	 */
	@Override
	public IntComparator comparator() {
		return null;
	}

	@Override
	public int dequeueInt() {
		if (start == end)
			throw new NoSuchElementException();
		final int t = array[start];
		if (++start == length)
			start = 0;
		reduce();
		return t;
	}

	/**
	 * Dequeues the {@linkplain #last() last} element from the queue.
	 *
	 * @return the dequeued element.
	 * @throws NoSuchElementException if the queue is empty.
	 */
	public int dequeueLastInt() {
		if (start == end)
			throw new NoSuchElementException();
		if (end == 0)
			end = length;
		final int t = array[--end];

		reduce();
		return t;
	}

	private final void resize(final int size, final int newLength) {
		final int[] newArray = new int[newLength];
		if (start >= end) {
			if (size != 0) {
				System.arraycopy(array, start, newArray, 0, length - start);
				System.arraycopy(array, 0, newArray, length - start, end);
			}
		} else
			System.arraycopy(array, start, newArray, 0, end - start);
		start = 0;
		end = size;
		array = newArray;
		length = newLength;
	}

	private final void expand() {
		resize(length, (int) Math.min(Arrays.MAX_ARRAY_SIZE, 2L * length));
	}

	private final void reduce() {
		final int size = size();
		if (length > INITIAL_CAPACITY && size <= length / 4)
			resize(size, (int) ((length + 1L) / 2)); // This turns Integer.MAX_VALUE into 2 << 30.
	}

	@Override
	public void enqueue(int x) {
		array[end++] = x;
		if (end == length)
			end = 0;
		if (end == start)
			expand();
	}

	/**
	 * Enqueues a new element as the {@linkplain #first() first} element (in
	 * dequeuing order) of the queue.
	 */
	public void enqueueFirst(int x) {
		if (start == 0)
			start = length;
		array[--start] = x;
		if (end == start)
			expand();
	}

	/**
	 * Returns the first element of the queue.
	 * 
	 * @return the first element of the queue.
	 */
	public int firstInt() {
		if (start == end)
			throw new NoSuchElementException();
		return array[start];
	}

	/**
	 * Returns the last element of the queue.
	 * 
	 * @return the last element of the queue.
	 */
	public int lastInt() {
		if (start == end)
			throw new NoSuchElementException();
		return array[(end == 0 ? length : end) - 1];
	}

	@Override
	public void clear() {

		start = end = 0;
	}

	/** Trims the queue to the smallest possible size. */
	public void trim() {
		final int size = size();
		final int[] newArray =

				new int[size + 1];

		if (start <= end)
			System.arraycopy(array, start, newArray, 0, end - start);
		else {
			System.arraycopy(array, start, newArray, 0, length - start);
			System.arraycopy(array, 0, newArray, length - start, end);
		}
		start = 0;
		length = (end = size) + 1;
		array = newArray;
	}

	@Override
	public int size() {
		final int apparentLength = end - start;
		return apparentLength >= 0 ? apparentLength : length + apparentLength;
	}
}