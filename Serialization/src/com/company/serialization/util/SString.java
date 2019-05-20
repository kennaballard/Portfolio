/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */


package com.company.serialization.util;

import com.company.serialization.Serializable;
import com.company.serialization.SerializationException;
import com.company.serialization.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * SSTring class
 *
 * - Serializable wrapper class for the String class
 *
 * Format:
 *
 * 1.  Length of the string.
 * 2.  Characters in the string as bytes (assumes ascii).
 *
 */
public class SString implements Serializable, Comparable<SString> {

    public static final byte SERIAL_ID = 0x02;

    private String value;

    public SString() {
        value = null;
    }

    public SString(String s) {
        value = s;
    }

    public String get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SString sString = (SString) o;

        return value != null ? value.equals(sString.value) : sString.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public byte getSerialId() {
        return SERIAL_ID;
    }

    @Override
    public void writeTo(Serializer s) throws IOException {
        // allocate enough room for the string format
        byte[] bytes = new byte[Integer.BYTES + value.length()];

        // write the length and the characters to the serializer
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.putInt(value.length());
        for(char c : value.toCharArray())
            buffer.put((byte)c);

        // write the bytes to the serializer.
        s.write(bytes);
    }

    @Override
    public void readFrom(Serializer s) throws IOException, SerializationException {
        // read the char array from the serializer.
        byte[] bytes = s.readNext();
        // create a string from the bytes.
        value = new String(bytes);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(SString rhs) {
        return this.value.compareTo(rhs.value);
    }
}
