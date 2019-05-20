/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company.collections.set;

import com.company.serialization.Serializable;
import com.company.serialization.SerializationException;
import com.company.serialization.Serializer;
import com.company.serialization.util.SString;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * An implementation of the Set API using a binary search tree.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2017-04-11
 */
public class STreeSet<T extends Serializable & Comparable<T>>  implements Set<T>, Serializable {
    public static final byte SERIAL_ID = 0x10;

    // Character to be serialized when a null has been reached
    public static final SString HAS_NULL = new SString("/");

    @Override
    public byte getSerialId() {
        return SERIAL_ID;
    }

    /**
     * Preorder traversal to serialize treeSet
     * @param s serializer
     * @throws IOException
     */
    @Override
    public void writeTo(Serializer s) throws IOException {
        // store size
        byte[] bytes = new byte[Integer.BYTES];

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.putInt(size);
        s.write(bytes);

        // Preorder traversal --> serialize
        write(s, root);
    }

    /**
     * * Recursive helper method to serialize
     * - if root is null --> serialize "null" character
     * - otherwise serialize element
     * @param s
     * @param root
     * @throws IOException
     */
    public void write(Serializer s, Node<T> root) throws IOException {
        if(root == null) {
            // store null character
            s.serialize(HAS_NULL);
            return;
        }
        // serialize current node element
        s.serialize(root.element);

        // preorder traverse left and right node
        write(s, root.left);
        write(s, root.right);
    }

    /**
     * Preorder traversal to deserialize treeSet
     * @param s serializer
     * @throws IOException
     * @throws SerializationException
     */
    @Override
    public void readFrom(Serializer s) throws IOException, SerializationException {
        // get size
        byte[] bytes = new byte[Integer.BYTES];
        s.read(bytes);
        this.size = ByteBuffer.wrap(bytes).getInt();
        // get tree structure
        root = new Node<>();
        read(s, root);
    }

    /**
     * Recursive helper method to deserialize
     * - if root is null --> reached leaf node
     * @param s
     * @param root
     * @throws IOException
     * @throws SerializationException
     */
    public void read(Serializer s, Node<T> root) throws IOException, SerializationException {
        T character = (T) s.deserialize();
        // has reached leaf node
        if(character.equals(HAS_NULL)) return;

        // store element
        root.element = character;

        // preorder traverse left and right node
        root.left = new Node<>();
        root.right = new Node<>();
        read(s, root.left);
        read(s, root.right);
    }

    private class Node<T> {
        public T element;
        public Node<T> left;
        public Node<T> right;

        public Node(T element) {
            this.element = element;
        }
        public Node(){
            this.element = null;
        }
    }

    // fields: store the root of ths bst and the size.
    private Node<T> root;
    private int size;


    @Override
    public boolean add(T elem) {
        if(root == null) {
            root = new Node<>(elem);
            size++;
            return true;
        }
        else
            return add(root, elem);
    }

    /**
     * Recursive helper method to add elem as a leaf in the BST.
     * - precondition: current != null
     * @param current
     * @param elem
     * @return true if elem is added, false if it is already in the tree.
     */
    private boolean add(Node<T> current, T elem) {
        int cmp = elem.compareTo(current.element);
        if(cmp == 0)
            return false;
        if(cmp > 0) {  // elem is in left subtree
            if(current.left == null) {
                current.left = new Node<>(elem);
                size++;
                return true;
            }
            else
               return add(current.left, elem);
        }
        else { // elem is in right subtree
            if(current.right == null) {
                current.right = new Node<>(elem);
                size++;
                return true;
            }
            else
                return add(current.right, elem);
        }
    }


    public boolean contains(T elem) {
        return contains(root, elem);
    }

    /**
     * Recursively search the tree to check for the element.
     * - uses BST property to optimize the search: check only the subtree that
     *   can possibly have the element.
     * @param current
     * @param elem
     * @return
     */
    private boolean contains(Node<T> current, T elem) {
        if(current == null)
            return false;

        int cmp = elem.compareTo(current.element);
        if(cmp == 0)
            return true;
        if(cmp < 0)
            return contains(current.left, elem);
        else
            return contains(current.right, elem);
    }


    public boolean containsAll(Set<T> rhs) {
        return false;
    }

    @Override
    public T floor(T value) {
        return floor(root, value);
    }

    private T floor(Node<T> current, T value) {
        if (current == null)
            return null;
        int c = value.compareTo(current.element);
        if (c == 0)
            return current.element;
        if (c > 0) {
            T elem = floor(current.right, value);
            if(elem == null)
                return current.element;
            else
                return elem;
        }
        else {
            T elem = floor(current.left, value);
            if(elem == null)
                return null;
            else
                return elem;
        }
    }


    public boolean remove(T elem) {
        return removeHelper(root, null, elem);
    }

    /**
     * Recurive helper method to remove an element.
     * @param current the current node.
     * @param parent the parent of the current node (null implies current == root)
     * @param elem the element t
     * @return the value removed
     */
    private boolean removeHelper(Node<T> current, Node<T> parent, T elem) {

        // binary search is unsuccessful
        if(current == null)
            return false;

        int cmp = elem.compareTo(current.element);

        // node found:
        if(cmp == 0) {

            // if we need to remove an internal node with two children
            // find the successor in the left subtree and replace the current entry
            // with the successor's entry.
            if(current.left != null && current.right != null) {

                Node<T> tmp = current; // store the current node to replace it's entry

                // Ensure that `current` is the successor and that `parent` is it's parent
                // so that we remove this node below
                current = current.left;
                while(current.right != null) {
                    parent = current;
                    current = current.right;
                }
                tmp.element = current.element;
            }

            removeNode(current, parent);
            size--;
            return true;
        }

        // descend into the subtree that could contain the node
        if(cmp < 0)
            return removeHelper(current.left, current, elem);
        else
            return removeHelper(current.right, current, elem);
    }

    private void removeNode(Node<T> current, Node<T> parent) {

        // case 1: root
        if(current == root) {
            if(current.left == null)
                root = current.right;
            else
                root = current.left;
        }

        // case 2: left subtree of parent
        else if(current == parent.left) {
            if(current.left == null)
                parent.left = current.right;
            else
                parent.left = current.left;
        }

        // case 3: right subtree of parent
        else {
            if(current.right == null)
                parent.right = current.left;
            else
                parent.right = current.right;
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    public boolean isFull() {
        return false;
    }

    @Override
    public T[] toArray() {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        throw new RuntimeException("Not implemented.");
    }

}
