package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.*;
import com.carrotsearch.hppc.predicates.*;
import com.carrotsearch.hppc.procedures.*;
import java.util.Iterator;

/**
 * An associative container from keys to (one or possibly more) values.
 *
 * @see ShortContainer
 */
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypeVTypeAssociativeContainer.java")
public interface ShortObjectAssociativeContainer<VType> extends Iterable<ShortObjectCursor<VType>> {
	/**
	 * Returns a cursor over the entries (key-value pairs) in this map. The iterator
	 * is implemented as a cursor and it returns <b>the same cursor instance</b> on
	 * every call to {@link Iterator#next()}. To read the current key and value use
	 * the cursor's public fields. An example is shown below.
	 *
	 * <pre>
	 * for (IntShortCursor c : intShortMap) {
	 * 	System.out.println(&quot;index=&quot; + c.index + &quot; key=&quot; + c.key + &quot; value=&quot; + c.value);
	 * }
	 * </pre>
	 *
	 * <p>
	 * The <code>index</code> field inside the cursor gives the internal index
	 * inside the container's implementation. The interpretation of this index
	 * depends on to the container.
	 */
	@Override
	public Iterator<ShortObjectCursor<VType>> iterator();

	/**
	 * Returns <code>true</code> if this container has an association to a value for
	 * the given key.
	 */
	public boolean containsKey(short key);

	/**
	 * @return Returns the current size (number of assigned keys) in the container.
	 */
	public int size();

	/**
	 * @return Return <code>true</code> if this hash map contains no assigned keys.
	 */
	public boolean isEmpty();

	/**
	 * Removes all keys (and associated values) present in a given container. An
	 * alias to:
	 *
	 * <pre>
	 * keys().removeAll(container)
	 * </pre>
	 *
	 * but with no additional overhead.
	 *
	 * @return Returns the number of elements actually removed as a result of this
	 *         call.
	 */
	public int removeAll(ShortContainer container);

	/**
	 * Removes all keys (and associated values) for which the predicate returns
	 * <code>true</code>.
	 *
	 * @return Returns the number of elements actually removed as a result of this
	 *         call.
	 */
	public int removeAll(ShortPredicate predicate);

	/**
	 * Removes all keys (and associated values) for which the predicate returns
	 * <code>true</code>.
	 *
	 * @return Returns the number of elements actually removed as a result of this
	 *         call.
	 */
	public int removeAll(ShortObjectPredicate<? super VType> predicate);

	/**
	 * Applies a given procedure to all keys-value pairs in this container. Returns
	 * the argument (any subclass of {@link ShortObjectProcedure}. This lets the
	 * caller call methods of the argument by chaining the call (even if the
	 * argument is an anonymous type) to retrieve computed values.
	 */
	public <T extends ShortObjectProcedure<? super VType>> T forEach(T procedure);

	/**
	 * Applies a given predicate to all keys-value pairs in this container. Returns
	 * the argument (any subclass of {@link ShortObjectPredicate}. This lets the
	 * caller call methods of the argument by chaining the call (even if the
	 * argument is an anonymous type) to retrieve computed values.
	 *
	 * <p>
	 * The iteration is continued as long as the predicate returns
	 * <code>true</code>.
	 */
	public <T extends ShortObjectPredicate<? super VType>> T forEach(T predicate);

	/**
	 * Returns a collection of keys of this container. The returned collection is a
	 * view over the key set and any modifications (if allowed) introduced to the
	 * collection will propagate to the associative container immediately.
	 */
	public ShortCollection keys();

	/**
	 * Returns a container view of all values present in this container. The
	 * returned collection is a view over the key set and any modifications (if
	 * allowed) introduced to the collection will propagate to the associative
	 * container immediately.
	 */
	public ObjectContainer<VType> values();
}
