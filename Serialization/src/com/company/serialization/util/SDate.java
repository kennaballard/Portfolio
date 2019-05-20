package com.company.serialization.util;

import com.company.serialization.Serializable;
import com.company.serialization.SerializationException;
import com.company.serialization.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

public class SDate implements Serializable, Comparable<SDate>  {
    public static final byte SERIAL_ID = 0x04;

    // optimization, use a static ByteBuffer to avoid extra allocations on each (de)serialize operation.
    private static final ByteBuffer buffer;

    // initialize a static field in a static initialization block.
    static {
        byte[] bytes = new byte[Long.BYTES];
        buffer = ByteBuffer.wrap(bytes);
    }

    // store the wrapped integer value.
    private long value;
    private Date date;

    public SDate(){
        date = new Date();
        value = 0;
    }

    public SDate(Date date){
        this.date = new Date(date.getTime());
        value = date.getTime();
    }

    @Override
    public byte getSerialId() {
        return SERIAL_ID;
    }

    /**
     * Serialize date
     * @param s serializer
     * @throws IOException
     */
    @Override
    public void writeTo(Serializer s) throws IOException {
        // clear the buffer in case of previous use.
        buffer.clear();

        // place the integer
        buffer.putLong(value);
        s.write(buffer.array());
    }

    /**
     * Deserialize date
     * @param s serializer
     * @throws IOException
     * @throws SerializationException
     */
    @Override
    public void readFrom(Serializer s) throws IOException, SerializationException {
        // clear the buffer in case of previous use.
        buffer.clear();

        // extract the backing byte[] and read into it.
        byte[] bytes = buffer.array();
        s.read(bytes);

        // read the integer value from the ByteBuffer,
        // since the backing byte[] now has the data.
        value = buffer.getLong();
        date = new Date(value);
    }

    @Override
    public int compareTo(SDate o) {
        return this.date.compareTo(o.date);
    }
}
