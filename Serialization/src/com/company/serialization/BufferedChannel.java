/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company.serialization;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Read/write to a channel.
 * - Can be used as either a Source or Destination (but not both at the same time).
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class BufferedChannel implements Source, Destination {

    private final FileChannel channel;
    private final Mode mode;
    private final ByteBuffer buffer;
    private int position;

    public enum Mode { READ, WRITE }

    private static final int BUFFER_SIZE = 1024;

    public BufferedChannel(FileChannel channel, Mode mode) throws IOException {
        this.channel = channel;
        this.mode = mode;
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
        position = 0;

        if(mode == Mode.READ) {
            channel.read(buffer);
            buffer.flip();
        }
    }

    @Override
    public void write(byte b) throws IOException {
        if(mode != Mode.WRITE)
            throw new RuntimeException("Cannot write in read mode.");
        if(!buffer.hasRemaining()) {
            buffer.flip();
            channel.write(buffer);
            buffer.flip();
        }
        buffer.put(b);
        position++;
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        if(mode != Mode.WRITE)
            throw new RuntimeException("Cannot write in read mode.");
        for(byte b : bytes) {
            if(!buffer.hasRemaining()) {
                buffer.flip();
                channel.write(buffer);
                buffer.flip();
            }
            buffer.put(b);
            position++;
        }
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public byte read() throws IOException {
        if(mode != Mode.READ)
            throw new RuntimeException("Cannot read in write mode.");

        if(!buffer.hasRemaining()) {
            buffer.flip();
            channel.read(buffer);
            buffer.flip();
        }
        position++;
        return buffer.get();
    }

    @Override
    public int read(byte[] bytes, int length) throws IOException {
        if(mode != Mode.READ)
            throw new RuntimeException("Cannot read in write mode.");
        int i;
        for(i = 0; i < length; i++) {
            if(!this.buffer.hasRemaining()) {
                this.buffer.flip();
                channel.read(this.buffer);
                this.buffer.flip();
            }
            bytes[i] = this.buffer.get();
        }
        position += i;
        return 0;
    }

    public void close() throws IOException {
        if(mode == Mode.WRITE) {
            buffer.flip();
            channel.write(buffer);
            channel.close();
        }
    }

}
