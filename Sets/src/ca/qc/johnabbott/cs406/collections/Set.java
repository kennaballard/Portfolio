package ca.qc.johnabbott.cs406.collections;

/**
 * Set interface
 * - operations of the Set ADT
 * @author Ian Clement
 */
public interface Set<T> extends Traversable<T> {

    /**
     * Determine if the set contains a specific element.
     * @param elem the element to find in the set.
     * @preconditions None.
     * @return true if the element is in the set, false otherwise.
     */
    boolean contains(T elem);

    /**
     * Determine if the set contains all the elements of the provided set, or, that the provided
     * set is a subset of the current set.
     * @param rhs the set of elements to find in the current set.
     * @preconditions None.
     * @return true if the elements are all in the set, false otherwise.
     */
    boolean containsAll(Set<T> rhs);

    /**
     * Add an element into the set.
     * @param elem the element to add to the set.
     * @preconditions The  set is not full.
     * @postconditions If there is no element in the  set that equals `element`, then the element is inserted
     * into the  set, otherwise the set is unchanged
     * @return The method returns `true` if the element is added to the set, and `false` otherwise.
     */
    boolean add(T elem);

    /**
     * Remove an element from the set.
     * @param elem the element to remove from the set.
     * @preconditions None.
     * @postconditions If there is an element in the  set equal to `element`, then it is removed from the set.
     * Otherwise, the  set is unchanged.
     * @return The method returns `true` if the element is added to the set, and `false` otherwise.
     */
    boolean remove(T elem);

    /**
     * Determine the number of elements in the  set.
     * @preconditions None.
     * @return the number of elements in the set.
     */
    int size();

    /**
     * Determine if the  set is empty.
     * @preconditions None.
     * @return `true` if the  set is empty, `false` otherwise.
     */
    boolean isEmpty();

    /**
     * Determines if the set is full.
     * @preconditions None.
     * @return `true` if the  set is full, `false` otherwise.
     */
    boolean isFull();

    T[] getElements();
}
