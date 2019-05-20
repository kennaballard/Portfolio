/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company.serialization;

import java.io.IOException;

/**
 * Source interface.
 *
 * - Operations needed to be used as a serialization input source.
 */
public interface Source {

    /**
     * Read a byte from the input source.
     * @return the read byte.
     * @throws IOException
     */
    byte read() throws IOException;

    /**
     * Read up to n bytes into a provided buffer.
     * @param bytes
     * @param length
     * @return the actual number of bytes read.
     */
    int read(byte[] bytes, int length) throws IOException;

    /**
     * Get the byte index from the first byte read from the input.
     * @return the byte index.
     */
    int getPosition();

    void close() throws IOException;
}
