/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company.collections.list;

import com.company.serialization.Serializable;
import com.company.serialization.SerializationException;
import com.company.serialization.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * An implementation of the List interface using unidirectional links, forming a "chain".
 *
 * @author Ian Clement
 */
public class SLinkedList<T extends Serializable> implements Serializable  {
    public static final byte SERIAL_ID = 0x08;

    public LinkedList<T> list;

    @Override
    public byte getSerialId() {
        return SERIAL_ID;
    }

    /**
     * Serializes LinkedList
     * @param s serializer
     * @throws IOException
     */
    @Override
    public void writeTo(Serializer s) throws IOException {
        // store list size
        byte[] bytes = new byte[Integer.BYTES];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.putInt(list.size());
        s.write(bytes);

        // Iterate over list, store every element
        Iterator<SLinkedList<T>> it = (Iterator<SLinkedList<T>>) list.iterator();
        while(it.hasNext()){
            s.serialize(it.next());
        }
    }

    /**
     * Deserialize LinkedList
     * @param s serializer
     * @throws IOException
     * @throws SerializationException
     */
    @Override
    public void readFrom(Serializer s) throws IOException, SerializationException {
        // get length of chain
        byte[] bytes = new byte[Integer.BYTES];
        s.read(bytes);
        int n = ByteBuffer.wrap(bytes).getInt();

        // deserialize each element
        for(int i=0; i<n; i++) {
            // add a new link at the end of the list, put last accordingly
            list.last.next = new Link<>((T) s.deserialize());
            list.last = list.last.next;
            list.size++;
        }
    }

    /* private inner class for link "chains" */
    private static class Link<T> {
        T element;
        Link<T> next;
        public Link(T element) {
            this.element = element;
        }
        public Link() {}
    }

    /* private inner class for linked list
    *  --> to make SLinkedList Serializable
    *  --> private link class becomes same one for SLinkedList and LinkedList */
    /**
     * An implementation of the List interface using unidirectional links, forming a "chain".
     *
     * @author Ian Clement
     */
    private class LinkedList<T> implements List<T> {

        /**
         * Move a reference `i` links along the chain. Returns null if `i` exceeds chain length.
         * @param i number of links to move.
         * @return the reference to the link after `i` moves.
         */
        private Link<T> move(int i) {
            // move traversal forward i times.
            Link<T> current = head;
            for(int j=0; j<i && current != null; j++)
                current = current.next;
            return current;
        }

        /* a list is represented with a head "dummy" node to simplify the
         * add/remove operation implementation. */
        public Link<T> head;

        /* a last reference is used to make list append operations
         *     add(x),
         *     add(size(), x)
         * more efficient */
        public Link<T> last;

        private int size;

        public LinkedList() {
            // create a "dummy" link, representing an empty list
            head = new Link<T>();
            last = head;
            size = 0;
        }

        @Override
        public void add(T element) {
            // add a new link at the end of the list, put last accordingly
            last.next = new Link<>(element);
            last = last.next;
            size++;
        }

        @Override
        public void add(int position, T element) {
            if(position < 0 || position > size)
                throw new ListBoundsException();

            // when "appending" call the add(x) method
            if(position == size) {
                add(element);
                return;
            }

            // move a link reference to the desired position (point to link "position")
            Link<T> current = move(position);

            // place new link between "position" and "position + 1"
            Link<T> tmp = new Link<T>(element);
            tmp.next = current.next;
            current.next = tmp;

            size++;
        }

        @Override
        public T remove(int position) {
            if(position < 0 || position >= size)
                throw new ListBoundsException();

            // move a link pointer to the desired position (point to link "position")
            Link<T> current = move(position);

            T element = current.next.element;
            current.next = current.next.next;

            // reset the last if we're removing the last link
            if(current.next == null)
                last = current;

            size--;

            return element;
        }

        @Override
        public void clear() {
            head.next = null; // remove all the links
            last = head; // empty list
            size = 0;
        }

        @Override
        public T get(int position){
            if(position < 0 || position >= size)
                throw new ListBoundsException();

            // move a link pointer to the desired position (point to link "position")
            Link<T> link = move(position + 1);
            return link.element;
        }

        @Override
        public T set(int position, T element){
            if(position < 0 || position >= size)
                throw new ListBoundsException();

            // move a link pointer to the desired position (point to link "position")
            Link<T> current = move(position + 1);
            T ret = current.element;
            current.element = element;
            return ret;
        }

        @Override
        public boolean isEmpty(){
            return size() == 0;
        }

        @Override
        public int size(){
            return size;
        }

        @Override
        public boolean contains(T element){
            // simple linear search
            Link<T> current = head.next;
            while(current != null) {
                if(current.element.equals(element))
                    return true;
                current = current.next;
            }
            return false;
        }

        @Override
        public Iterator<T> iterator() {
            return new ListIterator();
        }

        @Override
        public List<T> subList(int from, int to) {
            throw new RuntimeException("Not implemented.");
            //return new SubList(from, to);
        }

        @Override
        public boolean remove(T element) {
            for(Link<T> current = head; current != null; current = current.next)
                if(current.next.element.equals(element)) {
                    current.next = current.next.next;
                    size--;
                    return true;
                }
            return false;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for(Link<T> current = head.next; current != null; current = current.next) {
                sb.append(current.element);
                if(current.next != null)
                    sb.append(", ");
            }
            sb.append(']');
            return sb.toString();
        }

        /**
         * An iterator class for the link chain
         */
        private class ListIterator implements Iterator<T> {

            // stores the current link in the traversal.
            private Link<T> traversal;

            public ListIterator() {
                traversal = head.next;
            }

            @Override
            public boolean hasNext() {
                return traversal != null;
            }

            @Override
            public T next() {
                T tmp = traversal.element;
                traversal = traversal.next;
                return tmp;
            }

            @Override
            public void remove() {
                throw new RuntimeException("Not implemented");
            }
        }

    }


    /**
     * Move a reference `i` links along the chain. Returns null if `i` exceeds chain length.
     * @param i number of links to move.
     * @return the reference to the link after `i` moves.
     */
    private Link<T> move(int i) {
        // move traversal forward i times.
        Link<T> current = head;
        for(int j=0; j<i && current != null; j++)
            current = current.next;
        return current;
    }

    /* a list is represented with a head "dummy" node to simplify the
     * add/remove operation implementation. */
    private Link<T> head;

    /* a last reference is used to make list append operations
     *     add(x),
     *     add(size(), x)
     * more efficient */
    private Link<T> last;

    private int size;

    public SLinkedList() {
        // create a "dummy" link, representing an empty list
        list = new LinkedList<>();
    }


    public void add(T element) {
        list.add(element);
    }

    public void add(int position, T element) {
       list.add(position, element);
    }


    public T remove(int position) {
        return list.remove(position);
    }

    public void clear() {
       list.clear();
    }

    public T get(int position){
        return list.get(position);
    }

    public T set(int position, T element){
        return list.set(position, element);
    }

    public int size(){
        return list.size();
    }

    public boolean contains(T element){
        return list.contains(element);
    }
    @Override
    public String toString() {
        return list.toString();
    }

    /**
     * An iterator class for the link chain
     */
    private class ListIterator implements Iterator<T> {

        // stores the current link in the traversal.
        private Link<T> traversal;

        public ListIterator() {
            traversal = head.next;
        }

        @Override
        public boolean hasNext() {
            return traversal != null;
        }

        @Override
        public T next() {
            T tmp = traversal.element;
            traversal = traversal.next;
            return tmp;
        }

        @Override
        public void remove() {
            throw new RuntimeException("Not implemented");
        }
    }

}


     
      
