/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */


package com.company.serialization.util;

import com.company.serialization.Serializable;
import com.company.serialization.SerializationException;
import com.company.serialization.Serializer;

import java.io.IOException;

/**
 * Box class
 *
 * - Stores a value in a box. Example of serialization for generic classes.
 *
 * Format:
 *
 * 1.  Serial representation of the value.
 *
 */
public class Box<T extends Serializable> implements Serializable {

    public static final byte SERIAL_ID = 0x03;

    private T value;

    public Box() {
    }

    public Box(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Box{" + value.toString() + "}";
    }

    @Override
    public byte getSerialId() {
        return SERIAL_ID;
    }

    @Override
    public void writeTo(Serializer s) throws IOException {
        s.serialize(value);
    }

    @Override
    public void readFrom(Serializer s) throws IOException, SerializationException {
        value = (T) s.deserialize();
    }
}
