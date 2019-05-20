package com.company.serialization.util;

import com.company.serialization.Serializable;
import com.company.serialization.SerializationException;
import com.company.serialization.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Objects;

/**
 * Represents a grade.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2018-04-29
 */
public class Grade implements Serializable {
    public static final byte SERIAL_ID = 0x05;

    private static final ByteBuffer intBuffer;
    private static final ByteBuffer longBuffer;

    // initialize a static field in a static initialization block.
    static {
        intBuffer = ByteBuffer.wrap(new byte[Integer.BYTES]);
        longBuffer = ByteBuffer.wrap(new byte[Long.BYTES]);
    }

    private String name;
    private int result;
    private Date date;

    public Grade(String name, int result, Date date) {
        this.name = name;
        this.result = result;
        this.date = date;
    }

    public Grade() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "name='" + name + '\'' +
                ", result=" + result +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return result == grade.result &&
                Objects.equals(name, grade.name) &&
                Objects.equals(date, grade.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, result, date);
    }

    @Override
    public byte getSerialId() {
        return SERIAL_ID;
    }

    /**
     * Serialize Grade
     * @param s serializer
     * @throws IOException
     */
    @Override
    public void writeTo(Serializer s) throws IOException {
        // allocate enough room for result, name, date
        byte[] bytes = new byte[Integer.BYTES + Integer.BYTES + name.length() + Long.BYTES];

        // write the length and the characters to the serializer
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.putInt(result);
        buffer.putLong(date.getTime());
        buffer.putInt(name.length());
        // store each character of name
        for(char c : name.toCharArray())
            buffer.put((byte)c);

        // write the bytes to the serializer.
        s.write(bytes);
    }

    /**
     * Deserialize grade
     * @param s serializer
     * @throws IOException
     * @throws SerializationException
     */
    @Override
    public void readFrom(Serializer s) throws IOException, SerializationException {
        // clear the buffer in case of previous use.
        intBuffer.clear();
        longBuffer.clear();

        // extract the backing byte[] and read into it.
        byte[] intBytes = intBuffer.array();
        byte[] longBytes = longBuffer.array();
        s.read(intBytes);
        s.read(longBytes);

        // read the integer value from the ByteBuffer,
        // since the backing byte[] now has the data.
        result = intBuffer.getInt();
        date = new Date(longBuffer.getLong());

        // read the char array from the serializer.
        byte[] bytes = s.readNext();
        // create a string from the bytes.
        name = new String(bytes);
    }
}
