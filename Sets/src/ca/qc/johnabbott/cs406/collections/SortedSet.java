package ca.qc.johnabbott.cs406.collections;

import java.util.Arrays;

/**
 * TODO
 */
public class SortedSet<T extends Comparable<T>> implements Set<T> {

    private static final int DEFAULT_CAPACITY = 100;

    private T[] elements;
    private int size;

    // for traversable
    private int current = 0;

    public SortedSet() {
        this(DEFAULT_CAPACITY);
    }

    public SortedSet(int capacity) {
        this.size = 0;
        this.elements = (T[]) new Comparable[capacity];

    }

    public T[] getElements(){
        return this.elements;
    }

    @Override
    public boolean contains(T elem) {
        // elements is sorted, so we can binary search for the element.
        return Arrays.binarySearch(elements, 0, size, elem) >= 0;
    }

    @Override
    public boolean containsAll(Set<T> rhs) {
        T[] rhsElements = rhs.getElements();

        for (int i = 0; i < rhs.size(); i++){
            // As soon as one element isn't found --> isn't subset
            if(!this.contains(rhsElements[i]))
                return false;
        }

       return true;
    }

    @Override
    public boolean add(T elem) {
        if(isFull())
            throw new FullSetException();

        // Cannot add in middle of traversal
        if(current != 0)
            throw new TraversalException();

        int place = size;

        for(int i = 0; i < size; i++){
            // element is a duplicate
            if(elem.equals(elements[i]))
                return false;

            // check for right spot
            if(elements[i].compareTo(elem) > 0){
                place = i;
                // shift other values right
                for (int j = size; j > place; j--){
                    elements[j] = elements[j-1];
                }
                break;
            }
        }

        // insert new element in correct place
        elements[place] = elem;
        size++;
        return true;
    }

    @Override
    public boolean remove(T elem) {
        if(isEmpty())
            return false;

        // Cannot remove in middle of traversal
        if(current != 0)
            throw new TraversalException();

        int position = Arrays.binarySearch(elements, 0, size, elem);

        if(position == -1)
            return false;

        for(int i = position; i < size - 1; i++){
            elements[i] = elements[i+1];
        }
        size--;

        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        if(size == 0)
            return true;
        return false;
    }

    /**
     * @return elements[0]
     */
    public T min() {
        if(isEmpty())
            throw new EmptySetException();
        return elements[0];
    }

    /**
     * @return elements[size-1]
     */
    public T max() {
        if(isEmpty())
            throw new EmptySetException();
        return elements[size-1];
    }

    /**
     * @param first lowest value in subset
     * @param last limit for subset values
     * @return sub
     */
    public SortedSet<T> subset(T first, T last) {
        // check valid range
        if(first.compareTo(last) > 0)
            throw new IllegalArgumentException();

        SortedSet<T> sub = new SortedSet<>();

        // If set is empty --> subset will be empty
        if(this.isEmpty())
            return sub;

        int firstPos;
        int lastPos;

        // Determine start point
        if(first.compareTo(min()) < 0 )
            firstPos = 0;
        else
            firstPos = Arrays.binarySearch(elements, 0, size, first);

        // Determine end point
        if(last.compareTo(max()) > 0)
            lastPos = size;
        else
            lastPos = Arrays.binarySearch(elements, 0, size, last);

        if(firstPos >= 0 && lastPos >= 0){
            // find values
            for(int i = firstPos; i < lastPos; i++){
                sub.add(elements[i]);
            }
        }

        return sub;
    }


    @Override
    public boolean isFull() {
        if(elements.length == size)
            return true;
        return false;
    }

    @Override
    public String toString() {
        String set = "{";
        for(int i = 0; i < size; i++){
            set = set + elements[i].toString();
            if(i != size-1)
                set = set + ", ";
        }
        set = set + "}";
        return set;
    }

    @Override
    public void reset() {
        current = 0;
    }

    @Override
    public T next() {
        if(!hasNext())
            throw new TraversalException();
        return elements[current++];
    }

    @Override
    public boolean hasNext() {
        if(current == size  || isEmpty())
            return false;
        return true;
    }
}
