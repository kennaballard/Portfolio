/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.collections;

/**
 * Stack class for Data Structures (420-406)
 *
 * @author Ian Clement
 */
public class Stack<T> implements Traversable<T> {

    // Constants

    private static final int DEFAULT_CAPACITY = 100;

    // Fields

    // store the stack in the lower portion of an array
    private T[] elements;
    private int tos;

    private int current;

    // Constructors

    public Stack() {
        this(DEFAULT_CAPACITY);
    }

    public Stack(int capacity) {
        this.tos = -1;
        this.elements = (T[]) new Object[capacity];
    }

    // Methods

    /**
     * Add item to the top of the stack.
     * @param item item to be placed on the top of the stack.
     * @precondition The stack is not full.
     * @postcondition The item has been added to the top of the stack.
     */
    public void push(T item) {
        if(isFull())  // check precondition
            throw new StackOverflowException();
        elements[++tos] = item;
    }

    /**
     * Remove and return the item from the top of the stack.
     * @return the item that was at the top of the stack.
     * @precondition The stack is not empty.
     * @postcondition The topmost item has been removed from the stack.
     */
    public T pop() {
        if(isEmpty()) // check precondition
            throw new StackUnderflowException();

        // pop the element and nullify the position in the array.
        T element = elements[tos];
        elements[tos--] = null;

        return element;
    }

    /**
     * Get the item from the top of the stack.
     * @return the item at the top of the stack.
     * @precondition The stack is not empty.
    */
    public T top() {
        if(isEmpty()) // check precondition
            throw new StackUnderflowException();
        return elements[tos];
    }

    /**
     * Test whether the stack is empty.
     * @return true if the stack is empty, false otherwise.
     * @precondition None.
     */
    public boolean isEmpty() {
        return tos == -1;
    }

    /**
     * Test whether the stack is full.
     * @return true if the stack is full, false otherwise.
     * @precondition None.
     */
    public boolean isFull() {
        return tos == elements.length - 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for(int i = 0; i <= tos; i++) {
            if (first)
                first = false;
            else
                sb.append(", ");
            sb.append(elements[i]);
        }
        sb.append("] <- TOP");
        return sb.toString();
    }

    @Override
    public void reset() {
        current = tos;
    }

    @Override
    public T next() {
        return elements[current--];
    }

    @Override
    public boolean hasNext() {
        return current >= 0;
    }
}
