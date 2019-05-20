/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.collections;

/**
 * Queue class for Data Structures (420-406)
 *
 * @author Ian Clement
 */
public class Queue<T> implements Traversable<T> {

    // Constants
    private static final int DEFAULT_CAPACITY = 100;

    // Fields

    // store the queue in a "circular" array between front and rear
    private T[] elements;
    private int front;
    private int rear;

    // track the empty queue with a flag (set to true when dequeue empties the queue)
    private boolean empty;

    // traversal fields
    private int current;

    private boolean start;     // flag used to indicate that a traversal has just started
                               //  since front == rear at start and end of traversal)

    private boolean modified;  // flag used to indicate that a modification has happened
                               //  since the last traversal

    // Constructors

    public Queue() {
        this(DEFAULT_CAPACITY);
    }

    public Queue(int capacity) {
        this.front = this.rear = -1;
        this.empty = true;
        this.elements = (T[]) new Object[capacity];
    }

    // Methods

    /**
     * Add an item to the rear of the queue.
     * @param item item to be added to queue.
     * @precondition The queue is not full.
     * @postcondition The item has been added to the rear of the queue.
     */
    public void enqueue(T item) {
        if(isFull())  // check precondition
            throw new QueueOverflowException();

        // enqueue the element
        rear = mod(rear + 1, elements.length);
        elements[rear] = item;

        // enqueue means the queue is not empty
        empty = false;

        // queue has been modified
        modified = true;
    }

    /**
     * Remove and return the item from the front of a queue.
     * @return the item that was at the front of the queue.
     * @precondition The queue is not empty.
     * @postcondition The front item has been removed from the queue.
     */
    public T dequeue() {
        if(isEmpty())  // check precondition
            throw new QueueUnderflowException();

        // step the "front" index, dequeue the element and nullify the position in the array.
        front = mod(front + 1, elements.length);
        T element = elements[front];
        elements[front] = null;

        // dequeue can make the queue empty
        if(front == rear)
            empty = true;

        // queue has been modified
        modified = true;

        return element;
    }

    /**
     * Return the item from the front of a queue.
     * @return the item that was at the front of the queue.
     * @precondition The queue is not empty.
     */
    public T front() {
        if(isEmpty())  // check precondition
            throw new QueueUnderflowException();
        return elements[front];
    }

    /**
     * Test whether the queue is empty.
     * @return true if the queue is empty, false otherwise.
     * @precondition None.
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Test whether the queue is full.
     * @return true if the queue is full, false otherwise.
     * @precondition None.
     */
    public boolean isFull() {
        return mod(front, elements.length) == mod(rear, elements.length) && !empty;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FRONT -> [");

        if(!empty) {
            for (int i = mod(front + 1, elements.length);
                 i != mod(rear, elements.length);
                 i = mod(i + 1, elements.length))
            {
                sb.append(elements[i]);
                sb.append(", ");
            }
            sb.append(elements[rear]);
        }

        sb.append("] <- REAR");
        return sb.toString();
    }

    @Override
    public void reset() {
        // start the traversal at the start of the queue
        current = front;

        // the start flag is true unless there is nothing to traversal
        start = !isEmpty();

        // reset the modified flag
        modified = false;
    }

    @Override
    public T next() {
        if(!hasNext() || modified)
            throw new TraversalException();
        start = false; // proceeeding into traversal
        current = mod(current + 1, elements.length);
        return elements[current];
    }

    @Override
    public boolean hasNext() {
        return start || current != rear;
    }

    /**
     * Return the remainder of dividing `x` by `q`. Returns a positive value regardless of `x`.
     * @param x
     * @param q
     * @return the remainder of dividing `x` by `q`.
     */
    private static int mod(int x, int q) {
        if(x >= 0)
            return x % q;
        else
            return q + (x % q);
    }
}
