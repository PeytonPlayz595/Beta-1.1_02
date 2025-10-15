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
package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.NoSuchElementException;

/**
 * A type-specific AVL tree map with a fast, small-footprint implementation.
 *
 * <P>
 * The iterators provided by the views of this class are type-specific
 * {@linkplain it.unimi.dsi.fastutil.BidirectionalIterator bidirectional
 * iterators}. Moreover, the iterator returned by <code>iterator()</code> can be
 * safely cast to a type-specific {@linkplain java.util.ListIterator list
 * iterator}.
 */
public class Int2ObjectAVLTreeMap<V> extends AbstractInt2ObjectSortedMap<V> implements java.io.Serializable, Cloneable {
	/** A reference to the root entry. */
	protected transient Entry<V> tree;
	/** Number of entries in this map. */
	protected int count;
	/** The first key in this map. */
	protected transient Entry<V> firstEntry;
	/** The last key in this map. */
	protected transient Entry<V> lastEntry;
	/** Cached set of entries. */
	protected transient volatile ObjectSortedSet<Int2ObjectMap.Entry<V>> entries;
	/** Cached set of keys. */
	protected transient volatile IntSortedSet keys;
	/** Cached collection of values. */
	protected transient volatile ObjectCollection<V> values;
	/**
	 * The value of this variable remembers, after a <code>put()</code> or a
	 * <code>remove()</code>, whether the <em>domain</em> of the map has been
	 * modified.
	 */
	protected transient boolean modified;
	/** This map's comparator, as provided in the constructor. */
	protected Comparator<? super Integer> storedComparator;
	/**
	 * This map's actual comparator; it may differ from {@link #storedComparator}
	 * because it is always a type-specific comparator, so it could be derived from
	 * the former by wrapping.
	 */
	protected transient IntComparator actualComparator;
	private static final long serialVersionUID = -7046029254386353129L;
	private static final boolean ASSERTS = false;
	{
		allocatePaths();
	}

	/**
	 * Creates a new empty tree map.
	 */
	public Int2ObjectAVLTreeMap() {
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
		if (storedComparator == null || storedComparator instanceof IntComparator)
			actualComparator = (IntComparator) storedComparator;
		else
			actualComparator = new IntComparator() {
				public int compare(int k1, int k2) {
					return storedComparator.compare((Integer.valueOf(k1)), (Integer.valueOf(k2)));
				}

				public int compare(Integer ok1, Integer ok2) {
					return storedComparator.compare(ok1, ok2);
				}
			};

	}

	/**
	 * Creates a new empty tree map with the given comparator.
	 *
	 * @param c a (possibly type-specific) comparator.
	 */

	public Int2ObjectAVLTreeMap(final Comparator<? super Integer> c) {
		this();
		storedComparator = c;
		setActualComparator();
	}

	/**
	 * Creates a new tree map copying a given map.
	 *
	 * @param m a {@link Map} to be copied into the new tree map.
	 */

	public Int2ObjectAVLTreeMap(final Map<? extends Integer, ? extends V> m) {
		this();
		putAll(m);
	}

	/**
	 * Creates a new tree map copying a given sorted map (and its
	 * {@link Comparator}).
	 *
	 * @param m a {@link SortedMap} to be copied into the new tree map.
	 */

	public Int2ObjectAVLTreeMap(final SortedMap<Integer, V> m) {
		this(m.comparator());
		putAll(m);
	}

	/**
	 * Creates a new tree map copying a given map.
	 *
	 * @param m a type-specific map to be copied into the new tree map.
	 */

	public Int2ObjectAVLTreeMap(final Int2ObjectMap<? extends V> m) {
		this();
		putAll(m);
	}

	/**
	 * Creates a new tree map copying a given sorted map (and its
	 * {@link Comparator}).
	 *
	 * @param m a type-specific sorted map to be copied into the new tree map.
	 */

	public Int2ObjectAVLTreeMap(final Int2ObjectSortedMap<V> m) {
		this(m.comparator());
		putAll(m);
	}

	/**
	 * Creates a new tree map using the elements of two parallel arrays and the
	 * given comparator.
	 *
	 * @param k the array of keys of the new tree map.
	 * @param v the array of corresponding values in the new tree map.
	 * @param c a (possibly type-specific) comparator.
	 * @throws IllegalArgumentException if <code>k</code> and <code>v</code> have
	 *                                  different lengths.
	 */

	public Int2ObjectAVLTreeMap(final int[] k, final V v[], final Comparator<? super Integer> c) {
		this(c);
		if (k.length != v.length)
			throw new IllegalArgumentException(
					"The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
		for (int i = 0; i < k.length; i++)
			this.put(k[i], v[i]);
	}

	/**
	 * Creates a new tree map using the elements of two parallel arrays.
	 *
	 * @param k the array of keys of the new tree map.
	 * @param v the array of corresponding values in the new tree map.
	 * @throws IllegalArgumentException if <code>k</code> and <code>v</code> have
	 *                                  different lengths.
	 */

	public Int2ObjectAVLTreeMap(final int[] k, final V v[]) {
		this(k, v, null);
	}

	/*
	 * The following methods implements some basic building blocks used by all
	 * accessors. They are (and should be maintained) identical to those used in
	 * AVLTreeSet.drv.
	 *
	 * The put()/remove() code is derived from Ben Pfaff's GNU libavl
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

	final int compare(final int k1, final int k2) {
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

	final Entry<V> findKey(final int k) {
		Entry<V> e = tree;
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

	final Entry<V> locateKey(final int k) {
		Entry<V> e = tree, last = tree;
		int cmp = 0;

		while (e != null && (cmp = compare(k, e.key)) != 0) {
			last = e;
			e = cmp < 0 ? e.left() : e.right();
		}

		return cmp == 0 ? e : last;
	}

	/**
	 * This vector remembers the directions followed during the current insertion.
	 * It suffices for about 2<sup>32</sup> entries.
	 */
	private transient boolean dirPath[];

	private void allocatePaths() {
		dirPath = new boolean[48];
	}

	/*
	 * After execution of this method, modified is true iff a new entry has been
	 * inserted.
	 */

	public V put(final int k, final V v) {
		modified = false;

		if (tree == null) { // The case of the empty tree is treated separately.
			count++;
			tree = lastEntry = firstEntry = new Entry<V>(k, v);
			modified = true;
		} else {
			Entry<V> p = tree, q = null, y = tree, z = null, e = null, w = null;
			int cmp, i = 0;

			while (true) {
				if ((cmp = compare(k, p.key)) == 0) {
					final V oldValue = p.value;
					p.value = v;
					return oldValue;
				}

				if (p.balance() != 0) {
					i = 0;
					z = q;
					y = p;
				}

				if (dirPath[i++] = cmp > 0) {
					if (p.succ()) {
						count++;
						e = new Entry<V>(k, v);

						modified = true;
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
						e = new Entry<V>(k, v);

						modified = true;
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
				Entry<V> x = y.left;

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
				Entry<V> x = y.right;

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
				return defRetValue;

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
		return defRetValue;
	}

	/**
	 * Finds the parent of an entry.
	 *
	 * @param e a node of the tree.
	 * @return the parent of the given node, or <code>null</code> for the root.
	 */

	private Entry<V> parent(final Entry<V> e) {
		if (e == tree)
			return null;

		Entry<V> x, y, p;
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

	/*
	 * After execution of this method, {@link #modified} is true iff an entry has
	 * been deleted.
	 */

	public V remove(final int k) {
		modified = false;

		if (tree == null)
			return defRetValue;

		int cmp;
		Entry<V> p = tree, q = null;
		boolean dir = false;
		final int kk = k;

		while (true) {
			if ((cmp = compare(kk, p.key)) == 0)
				break;
			else if (dir = cmp > 0) {
				q = p;
				if ((p = p.right()) == null)
					return defRetValue;
			} else {
				q = p;
				if ((p = p.left()) == null)
					return defRetValue;
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
			Entry<V> r = p.right;

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
				Entry<V> s;

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

		Entry<V> y;

		while (q != null) {
			y = q;
			q = parent(y);

			if (!dir) {
				dir = q != null && q.left != y;
				y.incBalance();

				if (y.balance() == 1)
					break;
				else if (y.balance() == 2) {

					Entry<V> x = y.right;
					if (ASSERTS)
						assert x != null;

					if (x.balance() == -1) {
						Entry<V> w;

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

					Entry<V> x = y.left;
					if (ASSERTS)
						assert x != null;

					if (x.balance() == 1) {
						Entry<V> w;

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

		modified = true;
		count--;
		if (ASSERTS)
			checkTree(tree);
		return p.value;
	}

	public V put(final Integer ok, final V ov) {
		final V oldValue = put(((ok).intValue()), (ov));
		return modified ? (this.defRetValue) : (oldValue);
	}

	public V remove(final Object ok) {
		final V oldValue = remove(((((Integer) (ok)).intValue())));
		return modified ? (oldValue) : (this.defRetValue);
	}

	public boolean containsValue(final Object v) {
		final ValueIterator i = new ValueIterator();
		V ev;

		int j = count;
		while (j-- != 0) {
			ev = i.next();
			if (((ev) == null ? (v) == null : (ev).equals(v)))
				return true;
		}

		return false;
	}

	public void clear() {
		count = 0;
		tree = null;
		entries = null;
		values = null;
		keys = null;
		firstEntry = lastEntry = null;
	}

	/**
	 * This class represent an entry in a tree map.
	 *
	 * <P>
	 * We use the only "metadata", i.e., {@link Entry#info}, to store information
	 * about balance, predecessor status and successor status.
	 *
	 * <P>
	 * Note that since the class is recursive, it can be considered equivalently a
	 * tree.
	 */

	private static final class Entry<V> implements Cloneable, Int2ObjectMap.Entry<V> {
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
		int key;
		/** The value of this entry. */
		V value;
		/** The pointers to the left and right subtrees. */
		Entry<V> left, right;
		/**
		 * This integers holds different information in different bits (see
		 * {@link #SUCC_MASK}, {@link #PRED_MASK} and {@link #BALANCE_MASK}).
		 */
		int info;

		Entry() {
		}

		/**
		 * Creates a new entry with the given key and value.
		 *
		 * @param k a key.
		 * @param v a value.
		 */
		Entry(final int k, final V v) {
			this.key = k;
			this.value = v;
			info = SUCC_MASK | PRED_MASK;
		}

		/**
		 * Returns the left subtree.
		 *
		 * @return the left subtree (<code>null</code> if the left subtree is empty).
		 */
		Entry<V> left() {
			return (info & PRED_MASK) != 0 ? null : left;
		}

		/**
		 * Returns the right subtree.
		 *
		 * @return the right subtree (<code>null</code> if the right subtree is empty).
		 */
		Entry<V> right() {
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
		void pred(final Entry<V> pred) {
			info |= PRED_MASK;
			left = pred;
		}

		/**
		 * Sets the right pointer to a successor.
		 * 
		 * @param succ the successor.
		 */
		void succ(final Entry<V> succ) {
			info |= SUCC_MASK;
			right = succ;
		}

		/**
		 * Sets the left pointer to the given subtree.
		 * 
		 * @param left the new left subtree.
		 */
		void left(final Entry<V> left) {
			info &= ~PRED_MASK;
			this.left = left;
		}

		/**
		 * Sets the right pointer to the given subtree.
		 * 
		 * @param right the new right subtree.
		 */
		void right(final Entry<V> right) {
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

		Entry<V> next() {
			Entry<V> next = this.right;
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

		Entry<V> prev() {
			Entry<V> prev = this.left;
			if ((info & PRED_MASK) == 0)
				while ((prev.info & SUCC_MASK) == 0)
					prev = prev.right;
			return prev;
		}

		public Integer getKey() {
			return (Integer.valueOf(key));
		}

		public int getIntKey() {
			return key;
		}

		public V getValue() {
			return (value);
		}

		public V setValue(final V value) {
			final V oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		@SuppressWarnings("unchecked")
		public Entry<V> clone() {
			Entry<V> c;
			try {
				c = (Entry<V>) super.clone();
			} catch (CloneNotSupportedException cantHappen) {
				throw new InternalError();
			}
			c.key = key;
			c.value = value;
			c.info = info;
			return c;
		}

		@SuppressWarnings("unchecked")
		public boolean equals(final Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry<Integer, V> e = (Map.Entry<Integer, V>) o;
			return ((key) == (((e.getKey()).intValue())))
					&& ((value) == null ? ((e.getValue())) == null : (value).equals((e.getValue())));
		}

		public int hashCode() {
			return (key) ^ ((value) == null ? 0 : (value).hashCode());
		}

		public String toString() {
			return key + "=>" + value;
		}
		/*
		 * public void prettyPrint() { prettyPrint(0); }
		 * 
		 * public void prettyPrint(int level) { if ( pred() ) { for (int i = 0; i <
		 * level; i++) System.err.print("  "); System.err.println("pred: " + left ); }
		 * else if (left != null) left.prettyPrint(level +1 ); for (int i = 0; i <
		 * level; i++) System.err.print("  "); System.err.println(key + "=" + value +
		 * " (" + balance() + ")"); if ( succ() ) { for (int i = 0; i < level; i++)
		 * System.err.print("  "); System.err.println("succ: " + right ); } else if
		 * (right != null) right.prettyPrint(level + 1); }
		 */
	}

	/*
	 * public void prettyPrint() { System.err.println("size: " + count); if (tree !=
	 * null) tree.prettyPrint(); }
	 */
	public boolean containsKey(final int k) {
		return findKey(k) != null;
	}

	public int size() {
		return count;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	public V get(final int k) {
		final Entry<V> e = findKey(k);
		return e == null ? defRetValue : e.value;
	}

	public int firstIntKey() {
		if (tree == null)
			throw new NoSuchElementException();
		return firstEntry.key;
	}

	public int lastIntKey() {
		if (tree == null)
			throw new NoSuchElementException();
		return lastEntry.key;
	}

	/**
	 * An abstract iterator on the whole range.
	 *
	 * <P>
	 * This class can iterate in both directions on a threaded tree.
	 */
	private class TreeIterator {
		/**
		 * The entry that will be returned by the next call to
		 * {@link java.util.ListIterator#previous()} (or <code>null</code> if no
		 * previous entry exists).
		 */
		Entry<V> prev;
		/**
		 * The entry that will be returned by the next call to
		 * {@link java.util.ListIterator#next()} (or <code>null</code> if no next entry
		 * exists).
		 */
		Entry<V> next;
		/**
		 * The last entry that was returned (or <code>null</code> if we did not iterate
		 * or used {@link #remove()}).
		 */
		Entry<V> curr;
		/**
		 * The current index (in the sense of a {@link java.util.ListIterator}). Note
		 * that this value is not meaningful when this {@link TreeIterator} has been
		 * created using the nonempty constructor.
		 */
		int index = 0;

		TreeIterator() {
			next = firstEntry;
		}

		TreeIterator(final int k) {
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

		Entry<V> nextEntry() {
			if (!hasNext())
				throw new NoSuchElementException();
			curr = prev = next;
			index++;
			updateNext();
			return curr;
		}

		void updatePrevious() {
			prev = prev.prev();
		}

		Entry<V> previousEntry() {
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
			Int2ObjectAVLTreeMap.this.remove(curr.key);
			curr = null;
		}

		public int skip(final int n) {
			int i = n;
			while (i-- != 0 && hasNext())
				nextEntry();
			return n - i - 1;
		}

		public int back(final int n) {
			int i = n;
			while (i-- != 0 && hasPrevious())
				previousEntry();
			return n - i - 1;
		}
	}

	/**
	 * An iterator on the whole range.
	 *
	 * <P>
	 * This class can iterate in both directions on a threaded tree.
	 */
	private class EntryIterator extends TreeIterator implements ObjectListIterator<Int2ObjectMap.Entry<V>> {
		EntryIterator() {
		}

		EntryIterator(final int k) {
			super(k);
		}

		public Int2ObjectMap.Entry<V> next() {
			return nextEntry();
		}

		public Int2ObjectMap.Entry<V> previous() {
			return previousEntry();
		}

		public void set(Int2ObjectMap.Entry<V> ok) {
			throw new UnsupportedOperationException();
		}

		public void add(Int2ObjectMap.Entry<V> ok) {
			throw new UnsupportedOperationException();
		}
	}

	public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
		if (entries == null)
			entries = new AbstractObjectSortedSet<Int2ObjectMap.Entry<V>>() {
				final Comparator<? super Int2ObjectMap.Entry<V>> comparator = new Comparator<Int2ObjectMap.Entry<V>>() {
					public int compare(final Int2ObjectMap.Entry<V> x, final Int2ObjectMap.Entry<V> y) {
						return Int2ObjectAVLTreeMap.this.storedComparator.compare(x.getKey(), y.getKey());
					}
				};

				public Comparator<? super Int2ObjectMap.Entry<V>> comparator() {
					return comparator;
				}

				public ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> iterator() {
					return new EntryIterator();
				}

				public ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> iterator(final Int2ObjectMap.Entry<V> from) {
					return new EntryIterator(((from.getKey()).intValue()));
				}

				@SuppressWarnings("unchecked")
				public boolean contains(final Object o) {
					if (!(o instanceof Map.Entry))
						return false;
					final Map.Entry<Integer, V> e = (Map.Entry<Integer, V>) o;
					final Entry<V> f = findKey(((e.getKey()).intValue()));
					return e.equals(f);
				}

				@SuppressWarnings("unchecked")
				public boolean remove(final Object o) {
					if (!(o instanceof Map.Entry))
						return false;
					final Map.Entry<Integer, V> e = (Map.Entry<Integer, V>) o;
					final Entry<V> f = findKey(((e.getKey()).intValue()));
					if (f != null)
						Int2ObjectAVLTreeMap.this.remove(f.key);
					return f != null;
				}

				public int size() {
					return count;
				}

				public void clear() {
					Int2ObjectAVLTreeMap.this.clear();
				}

				public Int2ObjectMap.Entry<V> first() {
					return firstEntry;
				}

				public Int2ObjectMap.Entry<V> last() {
					return lastEntry;
				}

				public ObjectSortedSet<Int2ObjectMap.Entry<V>> subSet(Int2ObjectMap.Entry<V> from,
						Int2ObjectMap.Entry<V> to) {
					return subMap(from.getKey(), to.getKey()).int2ObjectEntrySet();
				}

				public ObjectSortedSet<Int2ObjectMap.Entry<V>> headSet(Int2ObjectMap.Entry<V> to) {
					return headMap(to.getKey()).int2ObjectEntrySet();
				}

				public ObjectSortedSet<Int2ObjectMap.Entry<V>> tailSet(Int2ObjectMap.Entry<V> from) {
					return tailMap(from.getKey()).int2ObjectEntrySet();
				}
			};
		return entries;
	}

	/**
	 * An iterator on the whole range of keys.
	 *
	 * <P>
	 * This class can iterate in both directions on the keys of a threaded tree. We
	 * simply override the
	 * {@link java.util.ListIterator#next()}/{@link java.util.ListIterator#previous()}
	 * methods (and possibly their type-specific counterparts) so that they return
	 * keys instead of entries.
	 */
	private final class KeyIterator extends TreeIterator implements IntListIterator {
		public KeyIterator() {
		}

		public KeyIterator(final int k) {
			super(k);
		}

		public int nextInt() {
			return nextEntry().key;
		}

		public int previousInt() {
			return previousEntry().key;
		}

		public void set(int k) {
			throw new UnsupportedOperationException();
		}

		public void add(int k) {
			throw new UnsupportedOperationException();
		}

		public Integer next() {
			return (Integer.valueOf(nextEntry().key));
		}

		public Integer previous() {
			return (Integer.valueOf(previousEntry().key));
		}

		public void set(Integer ok) {
			throw new UnsupportedOperationException();
		}

		public void add(Integer ok) {
			throw new UnsupportedOperationException();
		}
	};

	/** A keyset implementation using a more direct implementation for iterators. */
	private class KeySet extends AbstractInt2ObjectSortedMap<V>.KeySet {
		public IntBidirectionalIterator iterator() {
			return new KeyIterator();
		}

		public IntBidirectionalIterator iterator(final int from) {
			return new KeyIterator(from);
		}
	}

	/**
	 * Returns a type-specific sorted set view of the keys contained in this map.
	 *
	 * <P>
	 * In addition to the semantics of {@link java.util.Map#keySet()}, you can
	 * safely cast the set returned by this call to a type-specific sorted set
	 * interface.
	 *
	 * @return a type-specific sorted set view of the keys contained in this map.
	 */
	public IntSortedSet keySet() {
		if (keys == null)
			keys = new KeySet();
		return keys;
	}

	/**
	 * An iterator on the whole range of values.
	 *
	 * <P>
	 * This class can iterate in both directions on the values of a threaded tree.
	 * We simply override the
	 * {@link java.util.ListIterator#next()}/{@link java.util.ListIterator#previous()}
	 * methods (and possibly their type-specific counterparts) so that they return
	 * values instead of entries.
	 */
	private final class ValueIterator extends TreeIterator implements ObjectListIterator<V> {
		public V next() {
			return nextEntry().value;
		}

		public V previous() {
			return previousEntry().value;
		}

		public void set(V v) {
			throw new UnsupportedOperationException();
		}

		public void add(V v) {
			throw new UnsupportedOperationException();
		}
	};

	/**
	 * Returns a type-specific collection view of the values contained in this map.
	 *
	 * <P>
	 * In addition to the semantics of {@link java.util.Map#values()}, you can
	 * safely cast the collection returned by this call to a type-specific
	 * collection interface.
	 *
	 * @return a type-specific collection view of the values contained in this map.
	 */
	public ObjectCollection<V> values() {
		if (values == null)
			values = new AbstractObjectCollection<V>() {
				public ObjectIterator<V> iterator() {
					return new ValueIterator();
				}

				public boolean contains(final Object k) {
					return containsValue(k);
				}

				public int size() {
					return count;
				}

				public void clear() {
					Int2ObjectAVLTreeMap.this.clear();
				}
			};
		return values;
	}

	public IntComparator comparator() {
		return actualComparator;
	}

	public Int2ObjectSortedMap<V> headMap(int to) {
		return new Submap(((int) 0), true, to, false);
	}

	public Int2ObjectSortedMap<V> tailMap(int from) {
		return new Submap(from, false, ((int) 0), true);
	}

	public Int2ObjectSortedMap<V> subMap(int from, int to) {
		return new Submap(from, false, to, false);
	}

	/**
	 * A submap with given range.
	 *
	 * <P>
	 * This class represents a submap. One has to specify the left/right limits
	 * (which can be set to -&infin; or &infin;). Since the submap is a view on the
	 * map, at a given moment it could happen that the limits of the range are not
	 * any longer in the main map. Thus, things such as
	 * {@link java.util.SortedMap#firstKey()} or {@link java.util.Collection#size()}
	 * must be always computed on-the-fly.
	 */
	private final class Submap extends AbstractInt2ObjectSortedMap<V> implements java.io.Serializable {
		private static final long serialVersionUID = -7046029254386353129L;
		/** The start of the submap range, unless {@link #bottom} is true. */
		int from;
		/** The end of the submap range, unless {@link #top} is true. */
		int to;
		/** If true, the submap range starts from -&infin;. */
		boolean bottom;
		/** If true, the submap range goes to &infin;. */
		boolean top;
		/** Cached set of entries. */
		@SuppressWarnings("hiding")
		protected transient volatile ObjectSortedSet<Int2ObjectMap.Entry<V>> entries;
		/** Cached set of keys. */
		@SuppressWarnings("hiding")
		protected transient volatile IntSortedSet keys;
		/** Cached collection of values. */
		@SuppressWarnings("hiding")
		protected transient volatile ObjectCollection<V> values;

		/**
		 * Creates a new submap with given key range.
		 *
		 * @param from   the start of the submap range.
		 * @param bottom if true, the first parameter is ignored and the range starts
		 *               from -&infin;.
		 * @param to     the end of the submap range.
		 * @param top    if true, the third parameter is ignored and the range goes to
		 *               &infin;.
		 */
		public Submap(final int from, final boolean bottom, final int to, final boolean top) {
			if (!bottom && !top && Int2ObjectAVLTreeMap.this.compare(from, to) > 0)
				throw new IllegalArgumentException("Start key (" + from + ") is larger than end key (" + to + ")");
			this.from = from;
			this.bottom = bottom;
			this.to = to;
			this.top = top;
			this.defRetValue = Int2ObjectAVLTreeMap.this.defRetValue;
		}

		public void clear() {
			final SubmapIterator i = new SubmapIterator();
			while (i.hasNext()) {
				i.nextEntry();
				i.remove();
			}
		}

		/**
		 * Checks whether a key is in the submap range.
		 * 
		 * @param k a key.
		 * @return true if is the key is in the submap range.
		 */
		final boolean in(final int k) {
			return (bottom || Int2ObjectAVLTreeMap.this.compare(k, from) >= 0)
					&& (top || Int2ObjectAVLTreeMap.this.compare(k, to) < 0);
		}

		public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
			if (entries == null)
				entries = new AbstractObjectSortedSet<Int2ObjectMap.Entry<V>>() {
					public ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> iterator() {
						return new SubmapEntryIterator();
					}

					public ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> iterator(
							final Int2ObjectMap.Entry<V> from) {
						return new SubmapEntryIterator(((from.getKey()).intValue()));
					}

					public Comparator<? super Int2ObjectMap.Entry<V>> comparator() {
						return Int2ObjectAVLTreeMap.this.entrySet().comparator();
					}

					@SuppressWarnings("unchecked")
					public boolean contains(final Object o) {
						if (!(o instanceof Map.Entry))
							return false;
						final Map.Entry<Integer, V> e = (Map.Entry<Integer, V>) o;
						final Int2ObjectAVLTreeMap.Entry<V> f = findKey(((e.getKey()).intValue()));
						return f != null && in(f.key) && e.equals(f);
					}

					@SuppressWarnings("unchecked")
					public boolean remove(final Object o) {
						if (!(o instanceof Map.Entry))
							return false;
						final Map.Entry<Integer, V> e = (Map.Entry<Integer, V>) o;
						final Int2ObjectAVLTreeMap.Entry<V> f = findKey(((e.getKey()).intValue()));
						if (f != null && in(f.key))
							Submap.this.remove(f.key);
						return f != null;
					}

					public int size() {
						int c = 0;
						for (Iterator<?> i = iterator(); i.hasNext(); i.next())
							c++;
						return c;
					}

					public boolean isEmpty() {
						return !new SubmapIterator().hasNext();
					}

					public void clear() {
						Submap.this.clear();
					}

					public Int2ObjectMap.Entry<V> first() {
						return firstEntry();
					}

					public Int2ObjectMap.Entry<V> last() {
						return lastEntry();
					}

					public ObjectSortedSet<Int2ObjectMap.Entry<V>> subSet(Int2ObjectMap.Entry<V> from,
							Int2ObjectMap.Entry<V> to) {
						return subMap(from.getKey(), to.getKey()).int2ObjectEntrySet();
					}

					public ObjectSortedSet<Int2ObjectMap.Entry<V>> headSet(Int2ObjectMap.Entry<V> to) {
						return headMap(to.getKey()).int2ObjectEntrySet();
					}

					public ObjectSortedSet<Int2ObjectMap.Entry<V>> tailSet(Int2ObjectMap.Entry<V> from) {
						return tailMap(from.getKey()).int2ObjectEntrySet();
					}
				};
			return entries;
		}

		private class KeySet extends AbstractInt2ObjectSortedMap<V>.KeySet {
			public IntBidirectionalIterator iterator() {
				return new SubmapKeyIterator();
			}

			public IntBidirectionalIterator iterator(final int from) {
				return new SubmapKeyIterator(from);
			}
		}

		public IntSortedSet keySet() {
			if (keys == null)
				keys = new KeySet();
			return keys;
		}

		public ObjectCollection<V> values() {
			if (values == null)
				values = new AbstractObjectCollection<V>() {
					public ObjectIterator<V> iterator() {
						return new SubmapValueIterator();
					}

					public boolean contains(final Object k) {
						return containsValue(k);
					}

					public int size() {
						return Submap.this.size();
					}

					public void clear() {
						Submap.this.clear();
					}
				};
			return values;
		}

		public boolean containsKey(final int k) {
			return in(k) && Int2ObjectAVLTreeMap.this.containsKey(k);
		}

		public boolean containsValue(final Object v) {
			final SubmapIterator i = new SubmapIterator();
			Object ev;
			while (i.hasNext()) {
				ev = i.nextEntry().value;
				if (((ev) == null ? (v) == null : (ev).equals(v)))
					return true;
			}
			return false;
		}

		public V get(final int k) {
			final Int2ObjectAVLTreeMap.Entry<V> e;
			final int kk = k;
			return in(kk) && (e = findKey(kk)) != null ? e.value : this.defRetValue;
		}

		public V put(final int k, final V v) {
			modified = false;
			if (!in(k))
				throw new IllegalArgumentException("Key (" + k + ") out of range ["
						+ (bottom ? "-" : String.valueOf(from)) + ", " + (top ? "-" : String.valueOf(to)) + ")");
			final V oldValue = Int2ObjectAVLTreeMap.this.put(k, v);
			return modified ? this.defRetValue : oldValue;
		}

		public V put(final Integer ok, final V ov) {
			final V oldValue = put(((ok).intValue()), (ov));
			return modified ? (this.defRetValue) : (oldValue);
		}

		public V remove(final int k) {
			modified = false;
			if (!in(k))
				return this.defRetValue;
			final V oldValue = Int2ObjectAVLTreeMap.this.remove(k);
			return modified ? oldValue : this.defRetValue;
		}

		public V remove(final Object ok) {
			final V oldValue = remove(((((Integer) (ok)).intValue())));
			return modified ? (oldValue) : (this.defRetValue);
		}

		public int size() {
			final SubmapIterator i = new SubmapIterator();
			int n = 0;
			while (i.hasNext()) {
				n++;
				i.nextEntry();
			}
			return n;
		}

		public boolean isEmpty() {
			return !new SubmapIterator().hasNext();
		}

		public IntComparator comparator() {
			return actualComparator;
		}

		public Int2ObjectSortedMap<V> headMap(final int to) {
			if (top)
				return new Submap(from, bottom, to, false);
			return compare(to, this.to) < 0 ? new Submap(from, bottom, to, false) : this;
		}

		public Int2ObjectSortedMap<V> tailMap(final int from) {
			if (bottom)
				return new Submap(from, false, to, top);
			return compare(from, this.from) > 0 ? new Submap(from, false, to, top) : this;
		}

		public Int2ObjectSortedMap<V> subMap(int from, int to) {
			if (top && bottom)
				return new Submap(from, false, to, false);
			if (!top)
				to = compare(to, this.to) < 0 ? to : this.to;
			if (!bottom)
				from = compare(from, this.from) > 0 ? from : this.from;
			if (!top && !bottom && from == this.from && to == this.to)
				return this;
			return new Submap(from, false, to, false);
		}

		/**
		 * Locates the first entry.
		 *
		 * @return the first entry of this submap, or <code>null</code> if the submap is
		 *         empty.
		 */
		public Int2ObjectAVLTreeMap.Entry<V> firstEntry() {
			if (tree == null)
				return null;
			// If this submap goes to -infinity, we return the main map first entry;
			// otherwise, we locate the start of the map.
			Int2ObjectAVLTreeMap.Entry<V> e;
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
		 * @return the last entry of this submap, or <code>null</code> if the submap is
		 *         empty.
		 */
		public Int2ObjectAVLTreeMap.Entry<V> lastEntry() {
			if (tree == null)
				return null;
			// If this submap goes to infinity, we return the main map last entry;
			// otherwise, we locate the end of the map.
			Int2ObjectAVLTreeMap.Entry<V> e;
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

		public int firstIntKey() {
			Int2ObjectAVLTreeMap.Entry<V> e = firstEntry();
			if (e == null)
				throw new NoSuchElementException();
			return e.key;
		}

		public int lastIntKey() {
			Int2ObjectAVLTreeMap.Entry<V> e = lastEntry();
			if (e == null)
				throw new NoSuchElementException();
			return e.key;
		}

		public Integer firstKey() {
			Int2ObjectAVLTreeMap.Entry<V> e = firstEntry();
			if (e == null)
				throw new NoSuchElementException();
			return e.getKey();
		}

		public Integer lastKey() {
			Int2ObjectAVLTreeMap.Entry<V> e = lastEntry();
			if (e == null)
				throw new NoSuchElementException();
			return e.getKey();
		}

		/**
		 * An iterator for subranges.
		 * 
		 * <P>
		 * This class inherits from {@link TreeIterator}, but overrides the methods that
		 * update the pointer after a {@link java.util.ListIterator#next()} or
		 * {@link java.util.ListIterator#previous()}. If we would move out of the range
		 * of the submap we just overwrite the next or previous entry with
		 * <code>null</code>.
		 */
		private class SubmapIterator extends TreeIterator {
			SubmapIterator() {
				next = firstEntry();
			}

			SubmapIterator(final int k) {
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
				if (!bottom && prev != null && Int2ObjectAVLTreeMap.this.compare(prev.key, from) < 0)
					prev = null;
			}

			void updateNext() {
				next = next.next();
				if (!top && next != null && Int2ObjectAVLTreeMap.this.compare(next.key, to) >= 0)
					next = null;
			}
		}

		private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Int2ObjectMap.Entry<V>> {
			SubmapEntryIterator() {
			}

			SubmapEntryIterator(final int k) {
				super(k);
			}

			public Int2ObjectMap.Entry<V> next() {
				return nextEntry();
			}

			public Int2ObjectMap.Entry<V> previous() {
				return previousEntry();
			}

			public void set(Int2ObjectMap.Entry<V> ok) {
				throw new UnsupportedOperationException();
			}

			public void add(Int2ObjectMap.Entry<V> ok) {
				throw new UnsupportedOperationException();
			}
		}

		/**
		 * An iterator on a subrange of keys.
		 *
		 * <P>
		 * This class can iterate in both directions on a subrange of the keys of a
		 * threaded tree. We simply override the
		 * {@link java.util.ListIterator#next()}/{@link java.util.ListIterator#previous()}
		 * methods (and possibly their type-specific counterparts) so that they return
		 * keys instead of entries.
		 */
		private final class SubmapKeyIterator extends SubmapIterator implements IntListIterator {
			public SubmapKeyIterator() {
				super();
			}

			public SubmapKeyIterator(int from) {
				super(from);
			}

			public int nextInt() {
				return nextEntry().key;
			}

			public int previousInt() {
				return previousEntry().key;
			}

			public void set(int k) {
				throw new UnsupportedOperationException();
			}

			public void add(int k) {
				throw new UnsupportedOperationException();
			}

			public Integer next() {
				return (Integer.valueOf(nextEntry().key));
			}

			public Integer previous() {
				return (Integer.valueOf(previousEntry().key));
			}

			public void set(Integer ok) {
				throw new UnsupportedOperationException();
			}

			public void add(Integer ok) {
				throw new UnsupportedOperationException();
			}
		};

		/**
		 * An iterator on a subrange of values.
		 *
		 * <P>
		 * This class can iterate in both directions on the values of a subrange of the
		 * keys of a threaded tree. We simply override the
		 * {@link java.util.ListIterator#next()}/{@link java.util.ListIterator#previous()}
		 * methods (and possibly their type-specific counterparts) so that they return
		 * values instead of entries.
		 */
		private final class SubmapValueIterator extends SubmapIterator implements ObjectListIterator<V> {
			public V next() {
				return nextEntry().value;
			}

			public V previous() {
				return previousEntry().value;
			}

			public void set(V v) {
				throw new UnsupportedOperationException();
			}

			public void add(V v) {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Returns a deep copy of this tree map.
	 *
	 * <P>
	 * This method performs a deep copy of this tree map; the data stored in the
	 * set, however, is not cloned. Note that this makes a difference only for
	 * object keys.
	 *
	 * @return a deep copy of this tree map.
	 */
	@SuppressWarnings("unchecked")
	public Int2ObjectAVLTreeMap<V> clone() {
		Int2ObjectAVLTreeMap<V> c;
		try {
			c = (Int2ObjectAVLTreeMap<V>) super.clone();
		} catch (CloneNotSupportedException cantHappen) {
			throw new InternalError();
		}
		c.keys = null;
		c.values = null;
		c.entries = null;
		c.allocatePaths();
		if (count != 0) {
			// Also this apparently unfathomable code is derived from GNU libavl.
			Entry<V> e, p, q, rp = new Entry<V>(), rq = new Entry<V>();
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
		EntryIterator i = new EntryIterator();
		Entry<V> e;
		s.defaultWriteObject();
		while (n-- != 0) {
			e = i.nextEntry();
			s.writeInt(e.key);
			s.writeObject(e.value);
		}
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
	@SuppressWarnings("unchecked")
	private Entry<V> readTree(final java.io.ObjectInputStream s, final int n, final Entry<V> pred, final Entry<V> succ)
			throws java.io.IOException, ClassNotFoundException {
		if (n == 1) {
			final Entry<V> top = new Entry<V>(s.readInt(), (V) s.readObject());
			top.pred(pred);
			top.succ(succ);
			return top;
		}
		if (n == 2) {
			/*
			 * We handle separately this case so that recursion will always* be on nonempty
			 * subtrees.
			 */
			final Entry<V> top = new Entry<V>(s.readInt(), (V) s.readObject());
			top.right(new Entry<V>(s.readInt(), (V) s.readObject()));
			top.right.pred(top);
			top.balance(1);
			top.pred(pred);
			top.right.succ(succ);
			return top;
		}
		// The right subtree is the largest one.
		final int rightN = n / 2, leftN = n - rightN - 1;
		final Entry<V> top = new Entry<V>();
		top.left(readTree(s, leftN, pred, top));
		top.key = s.readInt();
		top.value = (V) s.readObject();
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
			Entry<V> e;
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

	@SuppressWarnings("rawtypes")
	private static int checkTree(@SuppressWarnings("unused") Entry e) {
		return 0;
	}
}