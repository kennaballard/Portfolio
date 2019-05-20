/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company.collections.map;

/**
 * Created by ian on 15-03-09.
 */
public class Entry<K, V> {
    private K key;
    private V value;

    public Entry(K key) {
        this.key = key;
    }

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
