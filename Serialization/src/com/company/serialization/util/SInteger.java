/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */


package com.company.serialization.util;

import com.company.serialization.Serializable;
import com.company.serialization.SerializationException;
import com.company.serialization.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * SInteger class
 *
 * - Serializable wrapper class for the Integer class.
 *
 * Format:
 *
 * 1.  Write integer as 4 bytes.
 *
 */
public class SInteger implements Serializable, Comparable<SInteger> {

    public static final byte SERIAL_ID = 0x01;

    // optimization, use a static ByteBuffer to avoid extra allocations on each (de)serialize operation.
    private static final ByteBuffer buffer;

    // ininitalize a static field in a static initialization block.
    static {
        byte[] bytes = new byte[Integer.BYTES];
        buffer = ByteBuffer.wrap(bytes);
    }

    // store the wrapped integer value.
    private int value;

    /**
     * Serializable 0 value.
     */
    public SInteger() {
        value = 0;
    }

    /**
     * Custom serializable value.
     * @param value
     */
    public SInteger(int value) {
        this.value = value;
    }

    /**
     * Get the value of the serializable integer.
     * @return
     */
    public int get() {
        return value;
    }

    @Override
    public byte getSerialId() {
        return SERIAL_ID;
    }

    @Override
    public void writeTo(Serializer s) throws IOException {
        // clear the buffer in case of previous use.
        buffer.clear();

        // place the integer
        buffer.putInt(value);
        s.write(buffer.array());
    }

    @Override
    public void readFrom(Serializer s) throws IOException, SerializationException {

        // clear the buffer in case of previous use.
        buffer.clear();

        // extract the backing byte[] and read into it.
        byte[] bytes = buffer.array();
        s.read(bytes);

        // read the integer value from the ByteBuffer,
        // since the backing byte[] now has the data.
        value = buffer.getInt();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(SInteger rhs) {
        return rhs.value - this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SInteger sInteger = (SInteger) o;
        return value == sInteger.value;
    }

    @Override
    public int hashCode() {

        return Objects.hash(value);
    }
}
