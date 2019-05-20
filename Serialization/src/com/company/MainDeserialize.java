package com.company;

import com.company.collections.Either;
import com.company.collections.list.SLinkedList;
import com.company.collections.map.SHashMap;
import com.company.collections.set.STreeSet;
import com.company.serialization.BufferedChannel;
import com.company.serialization.SerializationException;
import com.company.serialization.Serializer;
import com.company.serialization.util.*;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Deserialize example.
 */
public class MainDeserialize {
    public static void main(String arg[]) throws IOException, SerializationException {

        Serializer serializer = new Serializer(
                new BufferedChannel(
                        new RandomAccessFile("foo.bin", "rw").getChannel()
                        , BufferedChannel.Mode.READ
                ), null);

        SInteger i = (SInteger) serializer.deserialize();
        System.out.println(serializer.deserializeNoOp());
        SString s = (SString) serializer.deserialize();
        System.out.println(serializer.deserializeNoOp());
        Box<SString> bs = (Box<SString>) serializer.deserialize();
        System.out.println(serializer.deserializeNoOp());

        SDate d = (SDate) serializer.deserialize();
        System.out.println(serializer.deserializeNoOp());
        Grade g = (Grade) serializer.deserialize();
        System.out.println(serializer.deserializeNoOp());
        Either<SInteger,SString> e = (Either.LeftEither<SInteger,SString>) serializer.deserialize();
        System.out.println(serializer.deserializeNoOp());
        Either<SInteger,SString> e1 = (Either.RightEither<SInteger,SString>) serializer.deserialize();
        System.out.println(serializer.deserializeNoOp());
        SLinkedList<SInteger> l = (SLinkedList<SInteger>) serializer.deserialize();
        System.out.println(serializer.deserializeNoOp());
        SHashMap<SString,SInteger> m = (SHashMap<SString, SInteger>) serializer.deserialize();
        System.out.println(serializer.deserializeNoOp());
        STreeSet<SInteger> t = (STreeSet<SInteger>) serializer.deserialize();

        System.out.println(i);
        System.out.println(s);
        System.out.println(bs.toString());
        System.out.println(d.toString());
        System.out.println(g.toString());
        System.out.println(e.toString());
        System.out.println(e1.toString());
        System.out.println(l.toString());
        System.out.println(m.toString());
        System.out.println(t.toString());
        serializer.close();

    }
}
