/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company.serialization;


import com.company.collections.Either;
import com.company.collections.map.Entry;
import com.company.collections.map.SHashMap;
import com.company.collections.set.STreeSet;
import com.company.serialization.util.*;
import com.company.collections.list.SLinkedList;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Serializer class.
 *
 * - Sent objects to be serialized.
 * - Retrieves deserialized objects.
 * - Manage the interaction with the input source and output destination.
 *
 */
public class Serializer {
    // optimization, use a static ByteBuffer to avoid extra allocations on each (de)serialize operation.
    private static final ByteBuffer buffer;

    // initialize a static field in a static initialization block.
    static {
        byte[] bytes = new byte[Integer.BYTES];
        buffer = ByteBuffer.wrap(bytes);
    }

    private static final byte ALIAS_MARKER = (byte) 0xff;
    private static final byte ORIGINAL_MARKER = 0x00;
    private static final byte SERIAL_NULL_ID = 0x00;

    private Source source;
    private Destination destination;

    private boolean optimizeReferences;
    private Map<Object, Integer> refs;
    private Map<Integer, Object> refsInv;

    public Serializer(Source source, Destination destination) {
        this(source, destination, true);
    }

    public Serializer(Source source, Destination destination, boolean optimizeReferences) {
        this.source = source;
        this.destination = destination;
        this.optimizeReferences = optimizeReferences;
        if(optimizeReferences) {
            refs = new IdentityHashMap<>();
            refsInv = new HashMap<>();
        }
    }

    /**
     * Write a NULL record to the output destination.
     * @throws IOException
     */
    public void serializeNull() throws IOException {
        destination.write(SERIAL_NULL_ID);
    }

    /**
     * Write a record to the output destination for the serializable.
     * @param s the object to serialize
     * @throws IOException
     */
    public void serialize(Serializable s) throws IOException {
       if(optimizeReferences)
           serializeOp(s);
       else
           serializeNoOp(s);
    }

    /**
     * Normal Serialization
     * @param s
     * @throws IOException
     */
    public void serializeNoOp(Serializable s) throws IOException {
        // write id
        destination.write(s.getSerialId());
        s.writeTo(this);
    }


    /**
     * Serialize with reference optimization
     * @param s
     * @throws IOException
     */
    public void serializeOp(Serializable s) throws IOException {
        // get position it will be stored at
        Integer index = destination.getPosition();

        // must check if it should be an alias
        boolean contains = false;
        if (contain(s)){
            // need to fix comparison
            // add entry to refs
           addRefs(s, getKeyFromValue(s));
            contains=true;
        }
        else{
            // add entry to refs and refsInv
            addBoth(s, index);
        }


        // write id
        destination.write(s.getSerialId());

        // write flag
        if (contains) {
            buffer.clear();
            destination.write(ALIAS_MARKER);
            // write index
            buffer.putInt(getKeyFromValue(s));
            this.write(buffer.array());
        }
        else{
            // serialize as normal
            destination.write(ORIGINAL_MARKER);
            s.writeTo(this);
        }

    }

    /**
     * Checks if refsInv contains object with same value as o
     * @param o
     * @return
     */
    private boolean contain(Object o){
        for(Map.Entry<Integer, Object> entry : refsInv.entrySet()){
            if(entry.getValue().equals(o))
                return true;
        }
        return false;
    }

    /**
     * Gets the Key in refsInv for a given value Object
     * @param s
     * @return
     */
    private Integer getKeyFromValue(Object s){
        for (Map.Entry<Integer, Object> entry : refsInv.entrySet()){
            if (entry.getValue().equals(s))
                return entry.getKey();
        }

        return -1;
    }

    /**
     * Adds Serializable and Integer to refs & refsInv
     * @param s
     * @param n
     */
    private void addBoth(Serializable s, Integer n){
        addRefs(s, n);
        addRefsInv(n, s);
    }

    /**
     * Adds Serializable and Integer to refs
     * @param s
     * @param n
     */
    private void addRefs(Serializable s, Integer n){
        refs.put(s, n);
    }

    /**
     * Adds Serializable and Integer to refsInv
     * @param n
     * @param s
     */
    private void addRefsInv(Integer n, Serializable s){
        refsInv.put(n, s);
    }

    /**
     * Read a record from the input source and return the object it represents.
     * @return the deserialized object.
     * @throws IOException
     * @throws SerializationException
     */
    public Serializable deserialize() throws IOException, SerializationException {
        if(optimizeReferences)
            return deserializeOp();
        else
            return deserializeNoOp();
    }

    /**
     * Deserialize with reference optimization
     * @return
     * @throws IOException
     * @throws SerializationException
     */
    private Serializable deserializeOp() throws IOException, SerializationException {
        // get current position in buffer
        Integer pos = source.getPosition();
        // read id
        byte serialId = source.read();
        // check if alias or original
        byte flag = source.read();

        Serializable s = getSerializableById(serialId);

        if(flag == ALIAS_MARKER){
            buffer.clear();
            // read integer
            byte[] bytes = buffer.array();
            source.read(bytes, Integer.BYTES);
            Integer index = buffer.getInt();
            // retrieve correct reference
            return (Serializable) refsInv.get(index);
        }
        else {
            if(s != null)
                s.readFrom(this);
            if (refsInv.containsValue(s)){
                // add entry to refs
                addRefs(s, getKeyFromValue(s));
            }
            else{
                // add entry to refs and refsInv
                addBoth(s, pos);
            }
            return s;
        }
    }

    /**
     * Normal Deserialization
     * @return
     * @throws SerializationException
     * @throws IOException
     */
    public Serializable deserializeNoOp() throws SerializationException, IOException {
        byte serialId = source.read();
        Serializable s = getSerializableById(serialId);
        if(s != null)
            s.readFrom(this);
        return s;
    }

    /**
     * Write bytes to the output destination.
     * @param bytes the bytes to write.
     * @throws IOException
     */
    public void write(byte[] bytes) throws IOException {
        destination.write(bytes);
    }

    /**
     * Write byte to the output destination.
     * @param b the byte to write.
     * @throws IOException
     */
    public void write(byte b) throws IOException {
        destination.write(b);
    }


    /**
     * Read a length `n` from the source (as an int),
     * and use this length as the number of bytes to read and return
     * @return the bytes read.
     * @throws IOException
     */
    public byte[] readNext() throws IOException {
        byte[] bytes = new byte[Integer.BYTES];
        source.read(bytes, Integer.BYTES);
        int n = ByteBuffer.wrap(bytes).getInt();
        bytes = new byte[n];
        source.read(bytes, n);
        return bytes;
    }

    /**
     * Read the next byte from the input source.
     * @return the bytes read.
     * @throws IOException
     */
    public byte read() throws IOException {
        return source.read();
    }

    public int read(byte[] bytes) throws IOException {
        return read(bytes, bytes.length);
    }

    public int read(byte[] bytes, int length) throws IOException {
        return source.read(bytes, length);
    }


    /**
     * Create an instance of the object given by the provided serial ID.
     * @param id the serial ID.
     * @return the object.
     * @throws SerializationException
     */
    public static Serializable getSerializableById(byte id) throws SerializationException {

        // TODO: add a case to this switch for each new serializable.

        switch (id) {
            case SERIAL_NULL_ID:
                return null;
            case SInteger.SERIAL_ID:
                return new SInteger();
            case SString.SERIAL_ID:
                return new SString();
            case Box.SERIAL_ID:
                return new Box();
            case SDate.SERIAL_ID:
                return new SDate();
            case Grade.SERIAL_ID:
                return new Grade();
            case Either.LeftEither.SERIAL_ID:
                return new Either.LeftEither<>();
            case Either.RightEither.SERIAL_ID:
                return new Either.RightEither<>();
            case SLinkedList.SERIAL_ID:
                return new SLinkedList<>();
            case SHashMap.SERIAL_ID:
                return new SHashMap<>();
            case STreeSet.SERIAL_ID:
                return new STreeSet<>();
            default:
                throw new SerializationException("Unknown serial ID.");
        }
    }

    public Source getSource() {
        return source;
    }

    public Destination getDestination() {
        return destination;
    }

    public void close() throws IOException {
        if(source != null)
            source.close();
        if(destination != null)
            destination.close();
    }

}
