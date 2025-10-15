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
package it.unimi.dsi.fastutil.longs;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.NoSuchElementException;

/**
 * A type-specific AVL tree set with a fast, small-footprint implementation.
 *
 * <P>
 * The iterators provided by this class are type-specific
 * {@link it.unimi.dsi.fastutil.BidirectionalIterator bidirectional iterators}.
 * Moreover, the iterator returned by <code>iterator()</code> can be safely cast
 * to a type-specific {@linkplain java.util.ListIterator list iterator}.
 */
public class LongAVLTreeSet extends AbstractLongSortedSet implements java.io.Serializable, Cloneable, LongSortedSet {
	/** A reference to the root entry. */
	protected transient Entry tree;
	/** Number of elements in this set. */
	protected int count;
	/** The entry of the first element of this set. */
	protected transient Entry firstEntry;
	/** The entry of the last element of this set. */
	protected transient Entry lastEntry;
	/** This set's comparator, as provided in the constructor. */
	protected Comparator<? super Long> storedComparator;
	/**
	 * This set's actual comparator; it may differ from {@link #storedComparator}
	 * because it is always a type-specific comparator, so it could be derived from
	 * the former by wrapping.
	 */
	protected transient LongComparator actualComparator;
	private static final long serialVersionUID = -7046029254386353130L;
	private static final boolean ASSERTS = false;
	{
		allocatePaths();
	}

	/**
	 * Creates a new empty tree set.
	 */
	public LongAVLTreeSet() {
		tree = null;
		count = 0;
	}

	/**
	 * Generates the comparator that will be actually used.
	 *
	 * <P>
	 * When a specific {@link Comparator} is specified and stored in
	 * {@link #storedComparator}, we must check whether it is type-specific. If it
	 * is so, we can used directly, and we store it in {@link #actualComparator}.
	 * Otherwise, we generate on-the-fly an anonymous class that wraps the
	 * non-specific {@link Comparator} and makes it into a type-specific one.
	 */
	private void setActualComparator() {
		/*
		 * If the provided comparator is already type-specific, we use it. Otherwise, we
		 * use a wrapper anonymous class to fake that it is type-specific.
		 */
		if (storedComparator == null || storedComparator instanceof LongComparator)
			actualComparator = (LongComparator) storedComparator;
		else
			actualComparator = new LongComparator() {
				public int compare(long k1, long k2) {
					return storedComparator.compare((Long.valueOf(k1)), (Long.valueOf(k2)));
				}

				public int compare(Long ok1, Long ok2) {
					return storedComparator.compare(ok1, ok2);
				}
			};
	}

	/**
	 * Creates a new empty tree set with the given comparator.
	 *
	 * @param c a {@link Comparator} (even better, a type-specific comparator).
	 */
	public LongAVLTreeSet(final Comparator<? super Long> c) {
		this();
		storedComparator = c;
		setActualComparator();
	}

	/**
	 * Creates a new tree set copying a given set.
	 *
	 * @param c a collection to be copied into the new tree set.
	 */

	public LongAVLTreeSet(final Collection<? extends Long> c) {
		this();
		addAll(c);
	}

	/**
	 * Creates a new tree set copying a given sorted set (and its
	 * {@link Comparator}).
	 *
	 * @param s a {@link SortedSet} to be copied into the new tree set.
	 */

	public LongAVLTreeSet(final SortedSet<Long> s) {
		this(s.comparator());
		addAll(s);
	}

	/**
	 * Creates a new tree set copying a given type-specific collection.
	 *
	 * @param c a type-specific collection to be copied into the new tree set.
	 */

	public LongAVLTreeSet(final LongCollection c) {
		this();
		addAll(c);
	}

	/**
	 * Creates a new tree set copying a given type-specific sorted set (and its
	 * {@link Comparator}).
	 *
	 * @param s a type-specific sorted set to be copied into the new tree set.
	 */

	public LongAVLTreeSet(final LongSortedSet s) {
		this(s.comparator());
		addAll(s);
	}

	/**
	 * Creates a new tree set using elements provided by a type-specific iterator.
	 *
	 * @param i a type-specific iterator whose elements will fill the set.
	 */

	public LongAVLTreeSet(final LongIterator i) {
		while (i.hasNext())
			add(i.nextLong());
	}

	/**
	 * Creates a new tree set using elements provided by an iterator.
	 *
	 * @param i an iterator whose elements will fill the set.
	 */

	public LongAVLTreeSet(final Iterator<? extends Long> i) {
		this(LongIterators.asLongIterator(i));
	}

	/**
	 * Creates a new tree set and fills it with the elements of a given array using
	 * a given {@link Comparator}.
	 *
	 * @param a      an array whose elements will be used to fill the set.
	 * @param offset the first element to use.
	 * @param length the number of elements to use.
	 * @param c      a {@link Comparator} (even better, a type-specific comparator).
	 */

	public LongAVLTreeSet(final long[] a, final int offset, final int length, final Comparator<? super Long> c) {
		this(c);
		LongArrays.ensureOffsetLength(a, offset, length);
		for (int i = 0; i < length; i++)
			add(a[offset + i]);
	}

	/**
	 * Creates a new tree set and fills it with the elements of a given array.
	 *
	 * @param a      an array whose elements will be used to fill the set.
	 * @param offset the first element to use.
	 * @param length the number of elements to use.
	 */

	public LongAVLTreeSet(final long[] a, final int offset, final int length) {
		this(a, offset, length, null);
	}

	/**
	 * Creates a new tree set copying the elements of an array.
	 *
	 * @param a an array to be copied into the new tree set.
	 */

	public LongAVLTreeSet(final long[] a) {
		this();
		int i = a.length;
		while (i-- != 0)
			add(a[i]);
	}

	/**
	 * Creates a new tree set copying the elements of an array using a given
	 * {@link Comparator}.
	 *
	 * @param a an array to be copied into the new tree set.
	 * @param c a {@link Comparator} (even better, a type-specific comparator).
	 */

	public LongAVLTreeSet(final long[] a, final Comparator<? super Long> c) {
		this(c);
		int i = a.length;
		while (i-- != 0)
			add(a[i]);
	}

	/*
	 * The following methods implements some basic building blocks used by all
	 * accessors. They are (and should be maintained) identical to those used in
	 * AVLTreeMap.drv.
	 *
	 * The add()/remove() code is derived from Ben Pfaff's GNU libavl
	 * (http://www.msu.edu/~pfaffben/avl/). If you want to understand what's going
	 * on, you should have a look at the literate code contained therein first.
	 */

	/**
	 * Compares two keys in the right way.
	 *
	 * <P>
	 * This method uses the {@link #actualComparator} if it is
	 * non-<code>null</code>. Otherwise, it resorts to primitive type comparisons or
	 * to {@link Comparable#compareTo(Object) compareTo()}.
	 *
	 * @param k1 the first key.
	 * @param k2 the second key.
	 * @return a number smaller than, equal to or greater than 0, as usual (i.e.,
	 *         when k1 &lt; k2, k1 = k2 or k1 &gt; k2, respectively).
	 */

	final int compare(final long k1, final long k2) {
		return actualComparator == null ? ((k1) < (k2) ? -1 : ((k1) == (k2) ? 0 : 1))
				: actualComparator.compare(k1, k2);
	}

	/**
	 * Returns the entry corresponding to the given key, if it is in the tree;
	 * <code>null</code>, otherwise.
	 *
	 * @param k the key to search for.
	 * @return the corresponding entry, or <code>null</code> if no entry with the
	 *         given key exists.
	 */

	private Entry findKey(final long k) {
		Entry e = tree;
		int cmp;

		while (e != null && (cmp = compare(k, e.key)) != 0)
			e = cmp < 0 ? e.left() : e.right();

		return e;
	}

	/**
	 * Locates a key.
	 *
	 * @param k a key.
	 * @return the last entry on a search for the given key; this will be the given
	 *         key, if it present; otherwise, it will be either the smallest greater
	 *         key or the greatest smaller key.
	 */

	final Entry locateKey(final long k) {
		Entry e = tree, last = tree;
		int cmp = 0;

		while (e != null && (cmp = compare(k, e.key)) != 0) {
			last = e;
			e = cmp < 0 ? e.left() : e.right();
		}

		return cmp == 0 ? e : last;
	}

	/**
	 * This vector remembers the path followed during the current insertion. It
	 * suffices for about 2<sup>32</sup> entries.
	 */
	private transient boolean dirPath[];

	private void allocatePaths() {
		dirPath = new boolean[48];
	}

	public boolean add(final long k) {

		if (tree == null) { // The case of the empty tree is treated separately.
			count++;
			tree = lastEntry = firstEntry = new Entry(k);
		} else {
			Entry p = tree, q = null, y = tree, z = null, e = null, w = null;
			int cmp, i = 0;

			while (true) {
				if ((cmp = compare(k, p.key)) == 0)
					return false;

				if (p.balance() != 0) {
					i = 0;
					z = q;
					y = p;
				}

				if (dirPath[i++] = cmp > 0) {
					if (p.succ()) {
						count++;
						e = new Entry(k);

						if (p.right == null)
							lastEntry = e;

						e.left = p;
						e.right = p.right;

						p.right(e);

						break;
					}

					q = p;
					p = p.right;
				} else {
					if (p.pred()) {
						count++;
						e = new Entry(k);

						if (p.left == null)
							firstEntry = e;

						e.right = p;
						e.left = p.left;

						p.left(e);

						break;
					}

					q = p;
					p = p.left;
				}
			}

			p = y;
			i = 0;

			while (p != e) {
				if (dirPath[i])
					p.incBalance();
				else
					p.decBalance();

				p = dirPath[i++] ? p.right : p.left;
			}

			if (y.balance() == -2) {
				Entry x = y.left;

				if (x.balance() == -1) {
					w = x;
					if (x.succ()) {
						x.succ(false);
						y.pred(x);
					} else
						y.left = x.right;

					x.right = y;
					x.balance(0);
					y.balance(0);
				} else {
					if (ASSERTS)
						assert x.balance() == 1;

					w = x.right;
					x.right = w.left;
					w.left = x;
					y.left = w.right;
					w.right = y;
					if (w.balance() == -1) {
						x.balance(0);
						y.balance(1);
					} else if (w.balance() == 0) {
						x.balance(0);
						y.balance(0);
					} else {
						x.balance(-1);
						y.balance(0);
					}
					w.balance(0);

					if (w.pred()) {
						x.succ(w);
						w.pred(false);
					}
					if (w.succ()) {
						y.pred(w);
						w.succ(false);
					}

				}
			} else if (y.balance() == +2) {
				Entry x = y.right;

				if (x.balance() == 1) {
					w = x;
					if (x.pred()) {
						x.pred(false);
						y.succ(x);
					} else
						y.right = x.left;

					x.left = y;
					x.balance(0);
					y.balance(0);
				} else {
					if (ASSERTS)
						assert x.balance() == -1;

					w = x.left;
					x.left = w.right;
					w.right = x;
					y.right = w.left;
					w.left = y;
					if (w.balance() == 1) {
						x.balance(0);
						y.balance(-1);
					} else if (w.balance() == 0) {
						x.balance(0);
						y.balance(0);
					} else {
						x.balance(1);
						y.balance(0);
					}
					w.balance(0);

					if (w.pred()) {
						y.succ(w);
						w.pred(false);
					}
					if (w.succ()) {
						x.pred(w);
						w.succ(false);
					}

				}
			} else
				return true;

			if (z == null)
				tree = w;
			else {
				if (z.left == y)
					z.left = w;
				else
					z.right = w;
			}
		}

		if (ASSERTS)
			checkTree(tree);
		return true;
	}

	/**
	 * Finds the parent of an entry.
	 *
	 * @param e a node of the tree.
	 * @return the parent of the given node, or <code>null</code> for the root.
	 */

	private Entry parent(final Entry e) {
		if (e == tree)
			return null;

		Entry x, y, p;
		x = y = e;

		while (true) {
			if (y.succ()) {
				p = y.right;
				if (p == null || p.left != e) {
					while (!x.pred())
						x = x.left;
					p = x.left;
				}
				return p;
			} else if (x.pred()) {
				p = x.left;
				if (p == null || p.right != e) {
					while (!y.succ())
						y = y.right;
					p = y.right;
				}
				return p;
			}

			x = x.left;
			y = y.right;
		}
	}

	public boolean remove(final long k) {
		if (tree == null)
			return false;

		int cmp;
		Entry p = tree, q = null;
		boolean dir = false;
		final long kk = k;

		while (true) {
			if ((cmp = compare(kk, p.key)) == 0)
				break;
			else if (dir = cmp > 0) {
				q = p;
				if ((p = p.right()) == null)
					return false;
			} else {
				q = p;
				if ((p = p.left()) == null)
					return false;
			}
		}

		if (p.left == null)
			firstEntry = p.next();
		if (p.right == null)
			lastEntry = p.prev();

		if (p.succ()) {
			if (p.pred()) {
				if (q != null) {
					if (dir)
						q.succ(p.right);
					else
						q.pred(p.left);
				} else
					tree = dir ? p.right : p.left;
			} else {
				p.prev().right = p.right;

				if (q != null) {
					if (dir)
						q.right = p.left;
					else
						q.left = p.left;
				} else
					tree = p.left;
			}
		} else {
			Entry r = p.right;

			if (r.pred()) {
				r.left = p.left;
				r.pred(p.pred());
				if (!r.pred())
					r.prev().right = r;
				if (q != null) {
					if (dir)
						q.right = r;
					else
						q.left = r;
				} else
					tree = r;

				r.balance(p.balance());
				q = r;
				dir = true;

			} else {
				Entry s;

				while (true) {
					s = r.left;
					if (s.pred())
						break;
					r = s;
				}

				if (s.succ())
					r.pred(s);
				else
					r.left = s.right;

				s.left = p.left;

				if (!p.pred()) {
					p.prev().right = s;
					s.pred(false);
				}

				s.right = p.right;
				s.succ(false);

				if (q != null) {
					if (dir)
						q.right = s;
					else
						q.left = s;
				} else
					tree = s;

				s.balance(p.balance());
				q = r;
				dir = false;
			}
		}

		Entry y;

		while (q != null) {
			y = q;
			q = parent(y);

			if (!dir) {
				dir = q != null && q.left != y;
				y.incBalance();

				if (y.balance() == 1)
					break;
				else if (y.balance() == 2) {

					Entry x = y.right;
					if (ASSERTS)
						assert x != null;

					if (x.balance() == -1) {
						Entry w;

						if (ASSERTS)
							assert x.balance() == -1;

						w = x.left;
						x.left = w.right;
						w.right = x;
						y.right = w.left;
						w.left = y;

						if (w.balance() == 1) {
							x.balance(0);
							y.balance(-1);
						} else if (w.balance() == 0) {
							x.balance(0);
							y.balance(0);
						} else {
							if (ASSERTS)
								assert w.balance() == -1;

							x.balance(1);
							y.balance(0);
						}

						w.balance(0);

						if (w.pred()) {
							y.succ(w);
							w.pred(false);
						}
						if (w.succ()) {
							x.pred(w);
							w.succ(false);
						}

						if (q != null) {
							if (dir)
								q.right = w;
							else
								q.left = w;
						} else
							tree = w;
					} else {
						if (q != null) {
							if (dir)
								q.right = x;
							else
								q.left = x;
						} else
							tree = x;

						if (x.balance() == 0) {
							y.right = x.left;
							x.left = y;
							x.balance(-1);
							y.balance(+1);
							break;
						}

						if (ASSERTS)
							assert x.balance() == 1;

						if (x.pred()) {
							y.succ(true);
							x.pred(false);
						} else
							y.right = x.left;

						x.left = y;
						y.balance(0);
						x.balance(0);
					}
				}
			} else {
				dir = q != null && q.left != y;
				y.decBalance();

				if (y.balance() == -1)
					break;
				else if (y.balance() == -2) {

					Entry x = y.left;
					if (ASSERTS)
						assert x != null;

					if (x.balance() == 1) {
						Entry w;

						if (ASSERTS)
							assert x.balance() == 1;

						w = x.right;
						x.right = w.left;
						w.left = x;
						y.left = w.right;
						w.right = y;

						if (w.balance() == -1) {
							x.balance(0);
							y.balance(1);
						} else if (w.balance() == 0) {
							x.balance(0);
							y.balance(0);
						} else {
							if (ASSERTS)
								assert w.balance() == 1;

							x.balance(-1);
							y.balance(0);
						}

						w.balance(0);

						if (w.pred()) {
							x.succ(w);
							w.pred(false);
						}
						if (w.succ()) {
							y.pred(w);
							w.succ(false);
						}

						if (q != null) {
							if (dir)
								q.right = w;
							else
								q.left = w;
						} else
							tree = w;
					} else {
						if (q != null) {
							if (dir)
								q.right = x;
							else
								q.left = x;
						} else
							tree = x;

						if (x.balance() == 0) {
							y.left = x.right;
							x.right = y;
							x.balance(+1);
							y.balance(-1);
							break;
						}

						if (ASSERTS)
							assert x.balance() == -1;

						if (x.succ()) {
							y.pred(true);
							x.succ(false);
						} else
							y.left = x.right;

						x.right = y;
						y.balance(0);
						x.balance(0);
					}
				}
			}
		}

		count--;
		if (ASSERTS)
			checkTree(tree);
		return true;
	}

	public boolean contains(final long k) {
		return findKey(k) != null;
	}

	public void clear() {
		count = 0;
		tree = null;
		firstEntry = lastEntry = null;
	}

	/**
	 * This class represent an entry in a tree set.
	 *
	 * <P>
	 * We use the only "metadata", i.e., {@link Entry#info}, to store information
	 * about balance, predecessor status and successor status.
	 *
	 * <P>
	 * Note that since the class is recursive, it can be considered equivalently a
	 * tree.
	 */
	private static final class Entry implements Cloneable {
		/** If the bit in this mask is true, {@link #right} points to a successor. */
		private final static int SUCC_MASK = 1 << 31;
		/** If the bit in this mask is true, {@link #left} points to a predecessor. */
		private final static int PRED_MASK = 1 << 30;
		/**
		 * The bits in this mask hold the node balance info. You can get it just by
		 * casting to byte.
		 */
		private final static int BALANCE_MASK = 0xFF;
		/** The key of this entry. */
		long key;
		/** The pointers to the left and right subtrees. */
		Entry left, right;
		/**
		 * This integers holds different information in different bits (see
		 * {@link #SUCC_MASK}, {@link #PRED_MASK} and {@link #BALANCE_MASK}).
		 */
		int info;

		Entry() {
		}

		/**
		 * Creates a new entry with the given key.
		 *
		 * @param k a key.
		 */
		Entry(final long k) {
			this.key = k;
			info = SUCC_MASK | PRED_MASK;
		}

		/**
		 * Returns the left subtree.
		 *
		 * @return the left subtree (<code>null</code> if the left subtree is empty).
		 */
		Entry left() {
			return (info & PRED_MASK) != 0 ? null : left;
		}

		/**
		 * Returns the right subtree.
		 *
		 * @return the right subtree (<code>null</code> if the right subtree is empty).
		 */
		Entry right() {
			return (info & SUCC_MASK) != 0 ? null : right;
		}

		/**
		 * Checks whether the left pointer is really a predecessor.
		 * 
		 * @return true if the left pointer is a predecessor.
		 */
		boolean pred() {
			return (info & PRED_MASK) != 0;
		}

		/**
		 * Checks whether the right pointer is really a successor.
		 * 
		 * @return true if the right pointer is a successor.
		 */
		boolean succ() {
			return (info & SUCC_MASK) != 0;
		}

		/**
		 * Sets whether the left pointer is really a predecessor.
		 * 
		 * @param pred if true then the left pointer will be considered a predecessor.
		 */
		void pred(final boolean pred) {
			if (pred)
				info |= PRED_MASK;
			else
				info &= ~PRED_MASK;
		}

		/**
		 * Sets whether the right pointer is really a successor.
		 * 
		 * @param succ if true then the right pointer will be considered a successor.
		 */
		void succ(final boolean succ) {
			if (succ)
				info |= SUCC_MASK;
			else
				info &= ~SUCC_MASK;
		}

		/**
		 * Sets the left pointer to a predecessor.
		 * 
		 * @param pred the predecessr.
		 */
		void pred(final Entry pred) {
			info |= PRED_MASK;
			left = pred;
		}

		/**
		 * Sets the right pointer to a successor.
		 * 
		 * @param succ the successor.
		 */
		void succ(final Entry succ) {
			info |= SUCC_MASK;
			right = succ;
		}

		/**
		 * Sets the left pointer to the given subtree.
		 * 
		 * @param left the new left subtree.
		 */
		void left(final Entry left) {
			info &= ~PRED_MASK;
			this.left = left;
		}

		/**
		 * Sets the right pointer to the given subtree.
		 * 
		 * @param right the new right subtree.
		 */
		void right(final Entry right) {
			info &= ~SUCC_MASK;
			this.right = right;
		}

		/**
		 * Returns the current level of the node.
		 * 
		 * @return the current level of this node.
		 */
		int balance() {
			return (byte) info;
		}

		/**
		 * Sets the level of this node.
		 * 
		 * @param level the new level of this node.
		 */
		void balance(int level) {
			info &= ~BALANCE_MASK;
			info |= (level & BALANCE_MASK);
		}

		/** Increments the level of this node. */
		void incBalance() {
			info = info & ~BALANCE_MASK | ((byte) info + 1) & 0xFF;
		}

		/** Decrements the level of this node. */
		protected void decBalance() {
			info = info & ~BALANCE_MASK | ((byte) info - 1) & 0xFF;
		}

		/**
		 * Computes the next entry in the set order.
		 *
		 * @return the next entry (<code>null</code>) if this is the last entry).
		 */
		Entry next() {
			Entry next = this.right;
			if ((info & SUCC_MASK) == 0)
				while ((next.info & PRED_MASK) == 0)
					next = next.left;
			return next;
		}

		/**
		 * Computes the previous entry in the set order.
		 *
		 * @return the previous entry (<code>null</code>) if this is the first entry).
		 */
		Entry prev() {
			Entry prev = this.left;
			if ((info & PRED_MASK) == 0)
				while ((prev.info & SUCC_MASK) == 0)
					prev = prev.right;
			return prev;
		}

		public Entry clone() {
			Entry c;
			try {
				c = (Entry) super.clone();
			} catch (CloneNotSupportedException cantHappen) {
				throw new InternalError();
			}
			c.key = key;
			c.info = info;
			return c;
		}

		public boolean equals(final Object o) {
			if (!(o instanceof Entry))
				return false;
			Entry e = (Entry) o;
			return ((key) == (e.key));
		}

		public int hashCode() {
			return it.unimi.dsi.fastutil.HashCommon.long2int(key);
		}

		public String toString() {
			return String.valueOf(key);
		}
		/*
		 * public void prettyPrint() { prettyPrint(0); }
		 * 
		 * public void prettyPrint(int level) { if ( pred() ) { for (int i = 0; i <
		 * level; i++) System.err.print("  "); System.err.println("pred: " + left ); }
		 * else if (left != null) left.prettyPrint(level +1 ); for (int i = 0; i <
		 * level; i++) System.err.print("  "); System.err.println(key + " (" + level() +
		 * ")"); if ( succ() ) { for (int i = 0; i < level; i++) System.err.print("  ");
		 * System.err.println("succ: " + right ); } else if (right != null)
		 * right.prettyPrint(level + 1); }
		 */
	}

	/*
	 * public void prettyPrint() { System.err.println("size: " + count); if (tree !=
	 * null) tree.prettyPrint(); }
	 */
	public int size() {
		return count;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	public long firstLong() {
		if (tree == null)
			throw new NoSuchElementException();
		return firstEntry.key;
	}

	public long lastLong() {
		if (tree == null)
			throw new NoSuchElementException();
		return lastEntry.key;
	}

	/**
	 * An iterator on the whole range.
	 *
	 * <P>
	 * This class can iterate in both directions on a threaded tree.
	 */
	private class SetIterator extends AbstractLongListIterator {
		/**
		 * The entry that will be returned by the next call to
		 * {@link java.util.ListIterator#previous()} (or <code>null</code> if no
		 * previous entry exists).
		 */
		Entry prev;
		/**
		 * The entry that will be returned by the next call to
		 * {@link java.util.ListIterator#next()} (or <code>null</code> if no next entry
		 * exists).
		 */
		Entry next;
		/**
		 * The last entry that was returned (or <code>null</code> if we did not iterate
		 * or used {@link #remove()}).
		 */
		Entry curr;
		/**
		 * The current index (in the sense of a {@link java.util.ListIterator}). Note
		 * that this value is not meaningful when this {@link SetIterator} has been
		 * created using the nonempty constructor.
		 */
		int index = 0;

		SetIterator() {
			next = firstEntry;
		}

		SetIterator(final long k) {
			if ((next = locateKey(k)) != null) {
				if (compare(next.key, k) <= 0) {
					prev = next;
					next = next.next();
				} else
					prev = next.prev();
			}
		}

		public boolean hasNext() {
			return next != null;
		}

		public boolean hasPrevious() {
			return prev != null;
		}

		void updateNext() {
			next = next.next();
		}

		Entry nextEntry() {
			if (!hasNext())
				throw new NoSuchElementException();
			curr = prev = next;
			index++;
			updateNext();
			return curr;
		}

		public long nextLong() {
			return nextEntry().key;
		}

		public long previousLong() {
			return previousEntry().key;
		}

		void updatePrevious() {
			prev = prev.prev();
		}

		Entry previousEntry() {
			if (!hasPrevious())
				throw new NoSuchElementException();
			curr = next = prev;
			index--;
			updatePrevious();
			return curr;
		}

		public int nextIndex() {
			return index;
		}

		public int previousIndex() {
			return index - 1;
		}

		public void remove() {
			if (curr == null)
				throw new IllegalStateException();
			/*
			 * If the last operation was a next(), we are removing an entry that preceeds
			 * the current index, and thus we must decrement it.
			 */
			if (curr == prev)
				index--;
			next = prev = curr;
			updatePrevious();
			updateNext();
			LongAVLTreeSet.this.remove(curr.key);
			curr = null;
		}
	}

	public LongBidirectionalIterator iterator() {
		return new SetIterator();
	}

	public LongBidirectionalIterator iterator(final long from) {
		return new SetIterator(from);
	}

	public LongComparator comparator() {
		return actualComparator;
	}

	public LongSortedSet headSet(final long to) {
		return new Subset(((long) 0), true, to, false);
	}

	public LongSortedSet tailSet(final long from) {
		return new Subset(from, false, ((long) 0), true);
	}

	public LongSortedSet subSet(final long from, final long to) {
		return new Subset(from, false, to, false);
	}

	/**
	 * A subset with given range.
	 *
	 * <P>
	 * This class represents a subset. One has to specify the left/right limits
	 * (which can be set to -&infin; or &infin;). Since the subset is a view on the
	 * set, at a given moment it could happen that the limits of the range are not
	 * any longer in the main set. Thus, things such as
	 * {@link java.util.SortedSet#first()} or {@link java.util.SortedSet#size()}
	 * must be always computed on-the-fly.
	 */
	private final class Subset extends AbstractLongSortedSet implements java.io.Serializable, LongSortedSet {
		private static final long serialVersionUID = -7046029254386353129L;
		/** The start of the subset range, unless {@link #bottom} is true. */
		long from;
		/** The end of the subset range, unless {@link #top} is true. */
		long to;
		/** If true, the subset range starts from -&infin;. */
		boolean bottom;
		/** If true, the subset range goes to &infin;. */
		boolean top;

		/**
		 * Creates a new subset with given key range.
		 *
		 * @param from   the start of the subset range.
		 * @param bottom if true, the first parameter is ignored and the range starts
		 *               from -&infin;.
		 * @param to     the end of the subset range.
		 * @param top    if true, the third parameter is ignored and the range goes to
		 *               &infin;.
		 */
		public Subset(final long from, final boolean bottom, final long to, final boolean top) {
			if (!bottom && !top && LongAVLTreeSet.this.compare(from, to) > 0)
				throw new IllegalArgumentException(
						"Start element (" + from + ") is larger than end element (" + to + ")");
			this.from = from;
			this.bottom = bottom;
			this.to = to;
			this.top = top;
		}

		public void clear() {
			final SubsetIterator i = new SubsetIterator();
			while (i.hasNext()) {
				i.next();
				i.remove();
			}
		}

		/**
		 * Checks whether a key is in the subset range.
		 * 
		 * @param k a key.
		 * @return true if is the key is in the subset range.
		 */
		final boolean in(final long k) {
			return (bottom || LongAVLTreeSet.this.compare(k, from) >= 0)
					&& (top || LongAVLTreeSet.this.compare(k, to) < 0);
		}

		public boolean contains(final long k) {
			return in(k) && LongAVLTreeSet.this.contains(k);
		}

		public boolean add(final long k) {
			if (!in(k))
				throw new IllegalArgumentException("Element (" + k + ") out of range ["
						+ (bottom ? "-" : String.valueOf(from)) + ", " + (top ? "-" : String.valueOf(to)) + ")");
			return LongAVLTreeSet.this.add(k);
		}

		public boolean remove(final long k) {
			if (!in(k))
				return false;
			return LongAVLTreeSet.this.remove(k);
		}

		public int size() {
			final SubsetIterator i = new SubsetIterator();
			int n = 0;
			while (i.hasNext()) {
				n++;
				i.next();
			}
			return n;
		}

		public boolean isEmpty() {
			return !new SubsetIterator().hasNext();
		}

		public LongComparator comparator() {
			return actualComparator;
		}

		public LongBidirectionalIterator iterator() {
			return new SubsetIterator();
		}

		public LongBidirectionalIterator iterator(final long from) {
			return new SubsetIterator(from);
		}

		public LongSortedSet headSet(final long to) {
			if (top)
				return new Subset(from, bottom, to, false);
			return compare(to, this.to) < 0 ? new Subset(from, bottom, to, false) : this;
		}

		public LongSortedSet tailSet(final long from) {
			if (bottom)
				return new Subset(from, false, to, top);
			return compare(from, this.from) > 0 ? new Subset(from, false, to, top) : this;
		}

		public LongSortedSet subSet(long from, long to) {
			if (top && bottom)
				return new Subset(from, false, to, false);
			if (!top)
				to = compare(to, this.to) < 0 ? to : this.to;
			if (!bottom)
				from = compare(from, this.from) > 0 ? from : this.from;
			if (!top && !bottom && from == this.from && to == this.to)
				return this;
			return new Subset(from, false, to, false);
		}

		/**
		 * Locates the first entry.
		 *
		 * @return the first entry of this subset, or <code>null</code> if the subset is
		 *         empty.
		 */
		public LongAVLTreeSet.Entry firstEntry() {
			if (tree == null)
				return null;
			// If this subset goes to -infinity, we return the main set first entry;
			// otherwise, we locate the start of the set.
			LongAVLTreeSet.Entry e;
			if (bottom)
				e = firstEntry;
			else {
				e = locateKey(from);
				// If we find either the start or something greater we're OK.
				if (compare(e.key, from) < 0)
					e = e.next();
			}
			// Finally, if this subset doesn't go to infinity, we check that the resulting
			// key isn't greater than the end.
			if (e == null || !top && compare(e.key, to) >= 0)
				return null;
			return e;
		}

		/**
		 * Locates the last entry.
		 *
		 * @return the last entry of this subset, or <code>null</code> if the subset is
		 *         empty.
		 */
		public LongAVLTreeSet.Entry lastEntry() {
			if (tree == null)
				return null;
			// If this subset goes to infinity, we return the main set last entry;
			// otherwise, we locate the end of the set.
			LongAVLTreeSet.Entry e;
			if (top)
				e = lastEntry;
			else {
				e = locateKey(to);
				// If we find something smaller than the end we're OK.
				if (compare(e.key, to) >= 0)
					e = e.prev();
			}
			// Finally, if this subset doesn't go to -infinity, we check that the resulting
			// key isn't smaller than the start.
			if (e == null || !bottom && compare(e.key, from) < 0)
				return null;
			return e;
		}

		public long firstLong() {
			LongAVLTreeSet.Entry e = firstEntry();
			if (e == null)
				throw new NoSuchElementException();
			return e.key;
		}

		public long lastLong() {
			LongAVLTreeSet.Entry e = lastEntry();
			if (e == null)
				throw new NoSuchElementException();
			return e.key;
		}

		/**
		 * An iterator for subranges.
		 * 
		 * <P>
		 * This class inherits from {@link SetIterator}, but overrides the methods that
		 * update the pointer after a {@link java.util.ListIterator#next()} or
		 * {@link java.util.ListIterator#previous()}. If we would move out of the range
		 * of the subset we just overwrite the next or previous entry with
		 * <code>null</code>.
		 */
		private final class SubsetIterator extends SetIterator {
			SubsetIterator() {
				next = firstEntry();
			}

			SubsetIterator(final long k) {
				this();
				if (next != null) {
					if (!bottom && compare(k, next.key) < 0)
						prev = null;
					else if (!top && compare(k, (prev = lastEntry()).key) >= 0)
						next = null;
					else {
						next = locateKey(k);
						if (compare(next.key, k) <= 0) {
							prev = next;
							next = next.next();
						} else
							prev = next.prev();
					}
				}
			}

			void updatePrevious() {
				prev = prev.prev();
				if (!bottom && prev != null && LongAVLTreeSet.this.compare(prev.key, from) < 0)
					prev = null;
			}

			void updateNext() {
				next = next.next();
				if (!top && next != null && LongAVLTreeSet.this.compare(next.key, to) >= 0)
					next = null;
			}
		}
	}

	/**
	 * Returns a deep copy of this tree set.
	 *
	 * <P>
	 * This method performs a deep copy of this tree set; the data stored in the
	 * set, however, is not cloned. Note that this makes a difference only for
	 * object keys.
	 *
	 * @return a deep copy of this tree set.
	 */
	public Object clone() {
		LongAVLTreeSet c;
		try {
			c = (LongAVLTreeSet) super.clone();
		} catch (CloneNotSupportedException cantHappen) {
			throw new InternalError();
		}
		c.allocatePaths();
		if (count != 0) {
			// Also this apparently unfathomable code is derived from GNU libavl.
			Entry e, p, q, rp = new Entry(), rq = new Entry();
			p = rp;
			rp.left(tree);
			q = rq;
			rq.pred(null);
			while (true) {
				if (!p.pred()) {
					e = p.left.clone();
					e.pred(q.left);
					e.succ(q);
					q.left(e);
					p = p.left;
					q = q.left;
				} else {
					while (p.succ()) {
						p = p.right;
						if (p == null) {
							q.right = null;
							c.tree = rq.left;
							c.firstEntry = c.tree;
							while (c.firstEntry.left != null)
								c.firstEntry = c.firstEntry.left;
							c.lastEntry = c.tree;
							while (c.lastEntry.right != null)
								c.lastEntry = c.lastEntry.right;
							return c;
						}
						q = q.right;
					}
					p = p.right;
					q = q.right;
				}
				if (!p.succ()) {
					e = p.right.clone();
					e.succ(q.right);
					e.pred(q);
					q.right(e);
				}
			}
		}
		return c;
	}

	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
		int n = count;
		SetIterator i = new SetIterator();
		s.defaultWriteObject();
		while (n-- != 0)
			s.writeLong(i.nextLong());
	}

	/**
	 * Reads the given number of entries from the input stream, returning the
	 * corresponding tree.
	 *
	 * @param s    the input stream.
	 * @param n    the (positive) number of entries to read.
	 * @param pred the entry containing the key that preceeds the first key in the
	 *             tree.
	 * @param succ the entry containing the key that follows the last key in the
	 *             tree.
	 */
	private Entry readTree(final java.io.ObjectInputStream s, final int n, final Entry pred, final Entry succ)
			throws java.io.IOException, ClassNotFoundException {
		if (n == 1) {
			final Entry top = new Entry(s.readLong());
			top.pred(pred);
			top.succ(succ);
			return top;
		}
		if (n == 2) {
			/*
			 * We handle separately this case so that recursion will always* be on nonempty
			 * subtrees.
			 */
			final Entry top = new Entry(s.readLong());
			top.right(new Entry(s.readLong()));
			top.right.pred(top);
			top.balance(1);
			top.pred(pred);
			top.right.succ(succ);
			return top;
		}
		// The right subtree is the largest one.
		final int rightN = n / 2, leftN = n - rightN - 1;
		final Entry top = new Entry();
		top.left(readTree(s, leftN, pred, top));
		top.key = s.readLong();
		top.right(readTree(s, rightN, top, succ));
		if (n == (n & -n))
			top.balance(1); // Quick test for determining whether n is a power of 2.
		return top;
	}

	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
		s.defaultReadObject();
		/*
		 * The storedComparator is now correctly set, but we must restore on-the-fly the
		 * actualComparator.
		 */
		setActualComparator();
		allocatePaths();
		if (count != 0) {
			tree = readTree(s, count, null, null);
			Entry e;
			e = tree;
			while (e.left() != null)
				e = e.left();
			firstEntry = e;
			e = tree;
			while (e.right() != null)
				e = e.right();
			lastEntry = e;
		}
		if (ASSERTS)
			checkTree(tree);
	}

	private static int checkTree(@SuppressWarnings("unused") Entry e) {
		return 0;
	}
}