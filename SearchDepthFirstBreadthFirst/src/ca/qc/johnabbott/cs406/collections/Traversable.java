/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.collections;

/**
 * Traversable interface
 * - operations of a Traversable ADT
 * @author Ian Clement
 */
public interface Traversable<T> {
    /**
     * Initialize a traversal
     * @preconditions None.
     * @postconditions If there are elements, the traversal cursor is positioned on the first element. Otherwise, the traversal is complete (trivially).
     */
    void reset();

    /**
     * Determine if a traversal can continue.
     * @preconditions The traversal has been initialized and no modifications (ex: `add` or `remove` operations) have been performed since the initialization.
     * @return `true` is there are elements left in the traversal, `false` otherwise.
     */
    boolean hasNext();

    /**
     * Return the current element in the traversal, and then advance the traversal cursor to the next element.
     * @preconditions The traversal has been initialized and no modifications (`add` or `remove` operations) have been performed
     *                since the initialization. The traversal still has at least one element left.
     * @postconditions If there is a next element, the traversal cursor has advanced to it and it is returned.
     *                 At the end of the traversal cursor is "undefined", meaning that is no longer refers to an element.
     * @return the next element in the traversal.
     */
    T next();
}
