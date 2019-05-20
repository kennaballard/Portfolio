package com.company;

import com.company.collections.Either;
import com.company.collections.list.SLinkedList;
import com.company.collections.map.SHashMap;
import com.company.collections.set.STreeSet;
import com.company.serialization.BufferedChannel;
import com.company.serialization.Serializable;
import com.company.serialization.Serializer;
import com.company.serialization.util.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * Serialization example.
 */
public class MainSerialize {

    public static void main(String arg[]) throws IOException {

        BufferedChannel channel = new BufferedChannel(new RandomAccessFile("foo.bin", "rw").getChannel(), BufferedChannel.Mode.WRITE);

        Serializer serializer = new Serializer(null, channel);

        SInteger i = new SInteger(123);
        SString s = new SString("hello,");
        Box<SString> bs = new Box<>(new SString("world."));

        // date
        SDate d = new SDate(new Date());

        // grade
        Grade g = new Grade("bob", 90, new Date());

        // either
        Either<SInteger, SString> e = Either.left(new SInteger(807));
        Either<SInteger, SString> e1 = Either.right(new SString("bar"));

        // linked list
        SLinkedList l = new SLinkedList();
        l.add(new SInteger(321));
        l.add(new SInteger(21));
        l.add(new SInteger(1211));

        // hash map
        SHashMap<SString, SInteger> m = new SHashMap<>();
        m.put(new SString("hours"), new SInteger(24));
        m.put(new SString("age"), new SInteger(18));
        m.put(new SString("level"), new SInteger(3));
        m.put(new SString("program code"), new SInteger(420));

        // tree
        STreeSet<SInteger> t = new STreeSet<>();
        t.add(new SInteger(10));
        t.add(new SInteger(6));
        t.add(new SInteger(12));
        t.add(new SInteger(14));
        t.add(new SInteger(11));
        t.add(new SInteger(9));
        t.add(new SInteger(8));


        serializer.serialize(i);
        serializer.serializeNull();
        serializer.serialize(s);
        serializer.serializeNull();
        serializer.serialize(bs);
        serializer.serializeNull();
        serializer.serialize(d);
        serializer.serializeNull();
        serializer.serialize(g);
        serializer.serializeNull();
        serializer.serialize(e);
        serializer.serializeNull();
        serializer.serialize(e1);
        serializer.serializeNull();
        serializer.serialize(l);
        serializer.serializeNull();
        serializer.serialize(m);
        serializer.serializeNull();
        serializer.serialize(t);


        channel.close();

    }
}
