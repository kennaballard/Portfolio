/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company.collections.list;

/**
 * List interface. Based on Java's list interface.
 *
 * @author Ian Clement
 */
public interface List<T> extends Iterable<T> {

    /**
     * Appends the specified element to the end of this list.
     * @param element the element to add to the list.
     * @precondition   None.
     * @postcondition  `element` is the last element of the list. The size of the list is increased by 1.
     */
    void add(T element);

    /**
     * Inserts the specified element at the specified position in this list.
     * @param position position in the list to add the element.
     * @param element element to add.
     * @precondition  position is between 0 and list size (inclusive).
     * @postcondition   The element is added to the list at position. If position is list size, it is appended to the end of the list. The size of the list is increased by 1.
     */
    void add(int position, T element);

    /**
     * Removes the element at the specified position in this list.
     * @param position position in the list to remove.
     * @return element removed.
     * @precondition position is between 0 and list size (exclusive).
     * @postcondition   The element at `position` is removed from the list. The size of the list is decreased by 1. The removed element is returned.
     */
    T remove(int position);

    /**
     * Removes all of the elements from this list.
     * @precondition None.
     * @postcondition All elements are removed from the list and the size is put to 0.
     */
    void clear();

    /**
     * Retrieve the element at the specified position in this list.
     * @param position position in the list to add the element.
     * @return the element at the position.
     * @precondition   position is between 0 and list size (exclusive).
     * @postcondition  Returns the element at position in the list.
     */
    T get(int position);

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * @param position position in the list to add the element.
     * @param element element to put at position.
     * @return the previous value in this position.
     * @precondition   position is between 0 and list size (exclusive).
     * @postcondition  Sets a new value for the element at location position in the list.
     */
    T set(int position, T element);


    /**
     * Determine if this list contains no elements.
     * @return Returns true if this list contains no elements, false otherwise.
     * @precondition  None.
     * @postcondition Returns true if this list contains no elements, false otherwise.
     */
    boolean isEmpty();

    /**
     * Determine the number of elements in this list.
     * @return the number of elements in this list.
     * @precondition  None.
     * @postcondition  Returns the number of elements in this list.
     */
    int size();

    /**
     * Determine if the provided element is in the list.
     * @param element
     * @return true if this list contains no elements false otherwise.
     * @precondition  None.
     * @postcondition Returns true if this list contains no elements false otherwise.
     */
    boolean contains(T element);

    /**
     * Returns a view of the list between `from` (inclusive) and`to` (exclusive). The sublist
     * is backed by the current list, so that all modifying operations to the sublist will
     * also affect the current list.
     * @param from the index of the current list that is the start of the sublist.
     * @param to the index of the current list that is the end of the sublist.
     * @return A sublist, backed by the current list.
     * @precondition The parameters `from` and `to` are between 0 and list size (exclusive), and `from <= to`.
     * @postcondition A sublist, backed by the current list, is created and returned.
     */
    List<T> subList(int from, int to);

    /**
     * Removes the element from the list, if it is in the list.
     * @param element the element to remove.
     * @return true if the element is removed, false otherwise.
     * @precondition: None.
     * @postcondition: if the element is in the list it is removed.
     */
    boolean remove(T element);
}
