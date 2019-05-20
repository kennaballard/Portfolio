package com.example.a1649618.createnote;

import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.a1649618.model.Category;
import com.example.a1649618.model.Note;
import com.example.a1649618.model.NoteDatabaseHandler;
import com.example.a1649618.model.SampleData;
import com.example.a1649618.sqlite.DatabaseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * Unit tests for DB
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private List<Note> seedData;
    private NoteDatabaseHandler dbh;

    @Before
    public void setUp() throws DatabaseException {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        dbh = new NoteDatabaseHandler(appContext);
        tearDown();

        // insert all seed data.
        seedData = SampleData.generateNotes();
        for(Note n : seedData)
            dbh.getNoteTable().create(n);
    }

    @After
    public void tearDown() {
        // delete all rows from the database.
        SQLiteDatabase database = dbh.getWritableDatabase();
        database.delete(dbh.getNoteTable().getName(), "", new String[]{});
    }

    @Test
    public void testCreateSetsIds() {
        // check that all notes in seed data now have DB ids.
        for(Note n : seedData)
            assertTrue(n.getId() >= 0);
    }

    @Test
    public void testCreate() throws DatabaseException, ParseException {
        // check that one of the seed data items is in the DB
        Note note = dbh.getNoteTable().read(seedData.get(0).getId());
        assertEquals(seedData.get(0), note);
    }

    @Test
    public void testDateFormat() throws DatabaseException, ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.ZONE_OFFSET, 10);
        calendar.set(Calendar.MILLISECOND, 321);
        Note note = new Note()
                        .setBody("Foo")
                        .setTitle("Bar")
                        .setCategory(Category.PURPLE)
                        .setHasReminder(false)
                        .setCreated(calendar.getTime())
                        .setModified(new Date());
        dbh.getNoteTable().create(note);

        Note test = dbh.getNoteTable().read(note.getId());

        Calendar result = Calendar.getInstance();
        result.setTime(test.getCreated());
        assertEquals(10, result.get(Calendar.ZONE_OFFSET));
        assertEquals(321, result.get(Calendar.MILLISECOND));

    }

    @Test(expected = DatabaseException.class)
    public void testCreateTitleNull() throws DatabaseException {
        // create note with no title
        Note test = new Note()
                        .setBody("Foo")
                        .setCategory(Category.PURPLE)
                        .setHasReminder(false)
                        .setCreated(new Date())
                        .setModified(new Date());
        dbh.getNoteTable().create(test);
    }

    @Test(expected = DatabaseException.class)
    public void testCreateCreatedNull() throws DatabaseException {
        // create note with no created date
        Note test = new Note()
                .setTitle("Bar")
                .setBody("Foo")
                .setCategory(Category.PURPLE)
                .setHasReminder(false)
                .setModified(new Date());
        dbh.getNoteTable().create(test);
    }

    @Test(expected = DatabaseException.class)
    public void testCreateModifiedNull() throws DatabaseException {
        // create note with no created date
        Note test = new Note()
                .setTitle("Bar")
                .setBody("Foo")
                .setCategory(Category.PURPLE)
                .setHasReminder(false)
                .setCreated(new Date());
        dbh.getNoteTable().create(test);
    }

    @Test(expected = DatabaseException.class)
    public void testCreatedBeforeModified() throws DatabaseException {
        Note test = new Note()
                .setBody("Foo")
                .setCategory(Category.PURPLE)
                .setHasReminder(false)
                .setCreated(new Date());
        test.setModified(new Date(test.getCreated().getTime() - 1000));

        dbh.getNoteTable().create(test);
    }

    @Test
    public void testRead() throws DatabaseException, ParseException {
        // read one of the seed data notes
        Note note = dbh.getNoteTable().read(seedData.get(0).getId());
        assertEquals(seedData.get(0), note);
    }

    @Test
    public void testReadAll() throws DatabaseException, ParseException {
        List<Note> data = dbh.getNoteTable().readAll();
        assertTrue(data.containsAll(seedData) && seedData.containsAll(data));
    }

    @Test
    public void testUpdate() throws DatabaseException, ParseException {

        // make edits to a note.
        Note note = seedData.get(0);
        note.setTitle("Foo")
                .setBody("Bar")
                .setHasReminder(true)
                .setReminder(new Date())
                .setCategory(Category.PURPLE)
                .setModified(new Date());

        // update in DB
        assertTrue(dbh.getNoteTable().update(note));

        // read it back as a copy and test that it's identical to the above.
        Note test = dbh.getNoteTable().read(note.getId());
        assertEquals(note, test);
    }

    @Test(expected = DatabaseException.class)
    public void testDelete() throws DatabaseException, ParseException {
        try {
            // delete from db and check that it's gone.
            assertTrue(dbh.getNoteTable().delete(seedData.get(0)));
        }
        catch (DatabaseException e) {
            fail("Failed to delete note.");
        }
        // this line should throw as DatabaseException.
        dbh.getNoteTable().read(seedData.get(0).getId());
    }

}

