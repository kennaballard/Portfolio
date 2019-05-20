package ca.qc.johnabbott.cs406;

import ca.qc.johnabbott.cs406.collections.EmptySetException;
import ca.qc.johnabbott.cs406.collections.FullSetException;
import ca.qc.johnabbott.cs406.collections.SortedSet;
import ca.qc.johnabbott.cs406.collections.TraversalException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by ian on 2016-02-06.
 */
public class SortedSetTest {

    private static final long SEED = 123L;
    private static final int TEST_SET_SIZE = 15;
    private static final int TEST_SET_CAPACITY = 20;
    private static final int TEST_SET_MAX_ELEMENT = 20;

    private SortedSet<Integer> set;

    // keep track of the data outside of the set for testing purposes.
    private int[] data;

    @Before
    public void setUp() throws Exception {

        set = new SortedSet<>(TEST_SET_CAPACITY);

        data = new int[TEST_SET_SIZE];

        Random r = new Random(SEED);
        int i = 0;
        while(i < TEST_SET_SIZE) {
            int x = r.nextInt(TEST_SET_MAX_ELEMENT);
            if(set.add(x))
                data[i++] = x;
        }

        Arrays.sort(data);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testContains() throws Exception {

        // all data can be found in the set.
        for (int x : data)
            assertTrue(set.contains(x));

        // contains works for values lower than min and greate than max.
        assertFalse(set.contains(data[0] - 1));
        assertFalse(set.contains(data[TEST_SET_SIZE - 1] + 1));

        // missing data not in set.
        for (int x : new Range(TEST_SET_CAPACITY))
            if (Arrays.binarySearch(data, x) < 0)
                assertFalse(set.contains(x));
    }

    @Test
    public void testConstructorMakesEmptySet() {
        set = new SortedSet<Integer>();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    public void testAddFirst() throws Exception {
        assertTrue(set.add(data[0] - 1));
        assertEquals(TEST_SET_SIZE + 1, set.size());
    }

    @Test
    public void testAddLast() throws Exception {
        assertTrue(set.add(data[TEST_SET_SIZE - 1] + 1));
        assertEquals(TEST_SET_SIZE + 1, set.size());
    }

    @Test
    public void testAddMiddle() throws Exception {
        for (int x : new Range(data[0], data[TEST_SET_SIZE - 1]))
            if (Arrays.binarySearch(data, x) < 0) {
                assertTrue(set.add(x));
                assertEquals(TEST_SET_SIZE + 1, set.size());
                return;
            }
        fail();
    }

    @Test
    public void testNoDuplicates() {
        for(int x : data){
            set.add(x);
            assertEquals(TEST_SET_SIZE, set.size());
        }
    }

    @Test
    public void testFullSet() {
        SortedSet<Integer> set = new SortedSet<>(TEST_SET_CAPACITY);
        for(int i : new Range(TEST_SET_CAPACITY))
            set.add(i);
        assertTrue(set.size() == TEST_SET_CAPACITY);
        assertTrue(set.isFull());
    }

    @Test (expected = FullSetException.class)
    public void testFullSetException() {
        SortedSet<Integer> set = new SortedSet<>(TEST_SET_CAPACITY);
        for(int i : new Range(TEST_SET_CAPACITY + +1))
            set.add(i);
    }

    @Test
    public void testRemoveFirst() {
        assertTrue(set.remove(data[0]));
        assertEquals(TEST_SET_SIZE-1, set.size());
    }

    @Test
    public void testRemoveLast() {
        assertTrue(set.remove(data[TEST_SET_SIZE - 1]));
        assertEquals(TEST_SET_SIZE-1, set.size());
    }

    @Test
    public void testRemoveMiddle() {
        assertTrue(set.remove(data[TEST_SET_SIZE/2]));
        assertEquals(TEST_SET_SIZE - 1, set.size());

    }

    @Test
    public void testRemoveNotInSet() {
        for (int i : new Range(data[0], data[TEST_SET_SIZE - 1]))
            if (Arrays.binarySearch(data, i) > 0) {
                assertFalse(set.remove(data[0] - 1));
                assertEquals(TEST_SET_SIZE, set.size());
                return;
            }
        fail();
    }

    @Test
    public void testEmptySet() {
        SortedSet<Integer> set = new SortedSet<>();
        assertTrue(set.isEmpty());
    }

    @Test
    public void testRemoveOnEmptySet() {
        SortedSet<Integer> set = new SortedSet<>();
        assertFalse(set.remove(42));
    }

    @Test
    public void testRemoveOnEmptySetNoThrow() {
        SortedSet<Integer> set = new SortedSet<>();
        try {
            set.remove(42);
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testMin() throws Exception {
        assertTrue(set.min() == data[0]);
    }

    @Test
    public void testMax() throws Exception {
        assertTrue(set.max() == data[TEST_SET_SIZE - 1]);
    }

    @Test
    public void testNewMin() {
        set.add(data[0] - 1);
        assertEquals(data[0] - 1,  (int) set.min()); // not sure why I need the cast here...
    }

    @Test
    public void testNewMax() {
        set.add(data[TEST_SET_SIZE - 1] + 1);
        assertEquals(data[TEST_SET_SIZE - 1] + 1, (int) set.max()); // same as above
    }

    @Test
    public void testMinMaxInSingletonSet() {
        SortedSet<Integer> set = new SortedSet<>();
        set.add(42);
        assertEquals(set.min(), set.max());
    }

    @Test (expected = EmptySetException.class)
    public void testMinOnEmptySet() {
        SortedSet<Integer> set = new SortedSet<>();
        set.min();
    }

    @Test (expected = EmptySetException.class)
    public void testMaxOnEmptySet() {
        SortedSet<Integer> set = new SortedSet<>();
        set.max();
    }

    @Test
    public void testTraversal() {
        set.reset();
        for(int x : data) {
            assertTrue(set.hasNext());
            assertEquals(x, (int) set.next());
        }
        assertFalse(set.hasNext());
    }

    @Test
    public void testTraversalWithoutError() {
        set.reset();
        try {
            while (set.hasNext())
                set.next();
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTraversalEmptySet() {
        SortedSet<Integer> set = new SortedSet<>();
        set.reset();
        assertFalse(set.hasNext());
    }

    @Test (expected = TraversalException.class)
    public void testAddDuringTraversal() {
        set.reset();
        set.next();
        set.add(123);
        set.next();
    }

    @Test (expected = TraversalException.class)
    public void testCallNextOnCompletedTraversal() {
        set.reset();
        while(set.hasNext())
            set.next();
        set.next();
    }

    
    @Test
    public void testContainsAll() {
        int capacity = 100;
        SortedSet<Integer> all = new SortedSet<>(capacity);
        SortedSet<Integer> even = new SortedSet<>(capacity);
        for (int i = 0; i < capacity; i++) {
            all.add(i);
            if (i % 2 == 0)
                even.add(i);
        }

        assertFalse(even.containsAll(all));
        assertTrue(all.containsAll(even));

        // remove from subset
        even.remove(2);
        assertTrue(all.containsAll(even));
        even.add(2);

        // remove from superset
        all.remove(2);
        assertFalse(all.containsAll(even));


        // empty set is subset of all sets
        SortedSet<Integer> empty = new SortedSet<>();
        assertTrue(even.containsAll(empty));
        assertFalse(empty.containsAll(even));


        // equal sets are subsets of each other
        SortedSet<Integer> allCopy = new SortedSet<>();
        all.reset();
        while (all.hasNext())
            allCopy.add(all.next());
        assertTrue(allCopy.containsAll(all));
        assertTrue(all.containsAll(allCopy));

        // another way this might occur:
        assertTrue(all.containsAll(all));

    }

    @Test
    public void testToString() {

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        for(int x : data) {
            if(first)
                first = false;
            else
                sb.append(", ");
            sb.append(x);
        }
        sb.append('}');

        assertEquals(sb.toString(), set.toString());

        set = new SortedSet<>(TEST_SET_CAPACITY);
        assertEquals("{}", set.toString());

        set.add(123);
        assertEquals("{123}", set.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubsetIllegalArguments() {
        set.subset(100, 99);
    }

    @Test
    public void testSubsetOnEmptySet() {
        SortedSet<Integer> set = new SortedSet<>();
        SortedSet<Integer> subset = set.subset(0, 1000);
        assertTrue(subset.isEmpty());
    }
    
    @Test
    public void testSubset() {

        // low = high gives empty set
        SortedSet<Integer> empty = set.subset(100, 100);
        assertTrue(empty.isEmpty());

        // same but for existing value
        empty = set.subset(data[TEST_SET_SIZE / 2], data[TEST_SET_SIZE / 2]);
        assertTrue(empty.isEmpty());

        // test subset for the three innermost elements
        SortedSet<Integer> subset = set.subset(data[TEST_SET_SIZE / 2 - 1], data[TEST_SET_SIZE / 2 + 1] + 1);
        assertEquals(3, subset.size());
        assertTrue(subset.contains(data[TEST_SET_SIZE / 2 - 1]));
        assertTrue(subset.contains(data[TEST_SET_SIZE / 2]));
        assertTrue(subset.contains(data[TEST_SET_SIZE / 2 + 1]));

        // test subset with exclusive
        subset = set.subset(data[TEST_SET_SIZE / 2 - 1], data[TEST_SET_SIZE / 2 + 1]);
        assertEquals(2, subset.size());
        assertTrue(subset.contains(data[TEST_SET_SIZE / 2 - 1]));
        assertTrue(subset.contains(data[TEST_SET_SIZE / 2]));

        // subset is the same as the set if the entire range is specified
        subset = set.subset(data[0] - 1, data[TEST_SET_SIZE - 1] + 1);
        assertEquals(set.size(), subset.size());
        assertTrue(set.containsAll(subset));
    }

}