package com.company.collections;

import com.company.serialization.Serializable;
import com.company.serialization.SerializationException;
import com.company.serialization.Serializer;

import java.io.IOException;

/**
 * Represents one of two possible options.
 *
 * Create an Either object through its static methods. Ex:
 *
 * Either<String, Integer> e1 = Either.left("abc");
 * Either<String, Integer> e2 = Either.right(1234);
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2018-02-01
 */
public interface Either<S extends Serializable,T extends Serializable> extends Serializable {

    enum Type { LEFT, RIGHT }

    /**
     * Determine if the object is a left or right Either.
     * @return
     */
    Type getType();

    /**
     * Get the left value of the either.
     * @return
     */
    S getLeft();

    /**
     * Get the right value of the either.
     * @return
     */
    T getRight();


    /**
     * Generate a left Either object.
     * @param val
     * @param <S>
     * @param <T>
     * @return
     */
    static <S extends Serializable,T extends Serializable> Either<S,T> left(S val) {
        return new LeftEither<>(val);
    }

    static <S extends Serializable,T extends Serializable> Either<S,T> right(T val) {
        return new RightEither<>(val);
    }

    /**
     * Class to store left Either values.
     * TODO: should be private but Java won't let me. Update to anonymous inner class next semester ;)
     * @param <S>
     * @param <T>
     */
    class LeftEither<S extends Serializable,T extends Serializable> implements Either<S,T> {
        public static final byte SERIAL_ID = 0x06;
        private S val;

        public LeftEither(S val) {
            this.val = val;
        }

        public LeftEither() { }

        @Override
        public Type getType() {
            return Type.LEFT;
        }

        @Override
        public S getLeft() {
            return val;
        }

        @Override
        public T getRight() {
            throw new RuntimeException();
        }

        @Override
        public byte getSerialId() {
            return SERIAL_ID;
        }

        @Override
        public void writeTo(Serializer s) throws IOException {
            s.serialize(val);
        }

        @Override
        public void readFrom(Serializer s) throws IOException, SerializationException {
            val = (S) s.deserialize();
        }
    }

    /**
     * Class to store right Either values.
     * TODO: should be private but Java won't let me. Update to anonymous inner class next semester ;)
     * @param <S>
     * @param <T>
     */
    class RightEither<S extends Serializable,T extends Serializable> implements Either<S,T> {
        public static final byte SERIAL_ID = 0x07;
        private T val;

        public RightEither(T val) {
            this.val = val;
        }

        public RightEither() {
        }

        @Override
        public Type getType() {
            return Type.RIGHT;
        }

        @Override
        public S getLeft() {
            throw new RuntimeException();
        }

        @Override
        public T getRight() {
            return val;
        }

        @Override
        public byte getSerialId() {
            return SERIAL_ID;
        }

        @Override
        public void writeTo(Serializer s) throws IOException {
            s.serialize(val);
        }

        @Override
        public void readFrom(Serializer s) throws IOException, SerializationException {
            val = (T) s.deserialize();
        }
    }
}
