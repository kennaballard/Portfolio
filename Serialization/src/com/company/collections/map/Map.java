/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company.collections.map;

import com.company.collections.list.List;

/**
 * Created by ian on 2016-03-18.
 */
public interface Map<K, V> extends Iterable<Entry<K, V>> {

    /**
     * Associates the specified value with the specified key in this map.
     * @param key the key of the entry.
     * @param value the value of the entry.
     * @precondition None.
     * @postcondition The value to which key is mapped to is put to value. If the map previously contained a entry
     *                for key (i.e.: contains key(key) is true), the old value is replaced by value.
     */
    void put(K key, V value);

    /**
     * Retrieve the value mapped to by the specified key.
     * @param key the key to find in the map.
     * @return the value associated with key.
     * @precondition The map contains a entry for key.
     * @postcondition  Returns the value to which the specified key is mapped.
     */
    V get(K key);

    /**
     * From this map, remove the entry for the specified key, if it is present.
     * @param key the key of the entry to remove
     * @return the removed value.
     * @precondition None.
     * @postcondition If there is a entry for key, then it is removed, otherwise the map remains the same.
     */
    V remove(K key);

    /**
     * Removes all of the entrys from this map.
     * @precondition None.
     * @postcondition All entrys are removed from this map.
     */
    void clear();

    /**
     * Determine is a map contains a entry for the specified key.
     * @param key the key to find in the map.
     * @return true if this map contains a entry for the specified key, false otherwise.
     * @precondition None.
     * @postcondition Returns true if this map contains a entry for the specified key, false otherwise.
     */
    boolean containsKey(K key);

    /**
     * Get all the keys contained in this map.
     * @return a list of the keys contained in this map.
     * @precondition None.
     * @postcondition Returns a list of the keys contained in this map.
     */
    List<K> keys();

    /**
     * Determine if the map is empty: it contains no key-value entries.
     * @return true if this map contains no key-value entrys, false otherwise.
     * @precondition None.
     * @postcondition Returns true if this map contains no key-value entrys, false otherwise.
     */
    boolean isEmpty();
    /**
     * Determine the number of key-value entries in this map.
     * @return the number of key-value entries in this map.
     * @precondition None.
     * @postcondition Returns true if this map contains no key-value entrys, false otherwise.
     */
    int size();


}
