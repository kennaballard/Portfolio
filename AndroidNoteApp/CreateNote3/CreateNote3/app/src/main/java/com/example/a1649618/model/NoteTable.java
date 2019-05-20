package com.example.a1649618.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import com.example.a1649618.sqlite.Column;
import com.example.a1649618.sqlite.DatabaseException;
import com.example.a1649618.sqlite.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteTable extends Table<Note> {

    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_HAS_REMINDER = "hasReminder";
    public static final String COLUMN_REMINDER = "reminder";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_MODIFIED = "modified";

    /**
     * Create a database table
     *
     * @param dbh  the handler that connects to the sqlite database.
     */
    public NoteTable(SQLiteOpenHelper dbh) {
        super(dbh, TABLE_NAME);
        addColumn(new Column(COLUMN_TITLE, Column.Type.TEXT));
        addColumn(new Column(COLUMN_BODY, Column.Type.TEXT));
        addColumn(new Column(COLUMN_CATEGORY, Column.Type.TEXT));
        addColumn(new Column(COLUMN_HAS_REMINDER, Column.Type.INTEGER));
        addColumn(new Column(COLUMN_REMINDER, Column.Type.TEXT));
        addColumn(new Column(COLUMN_CREATED, Column.Type.TEXT));
        addColumn(new Column(COLUMN_MODIFIED, Column.Type.TEXT));
    }

    @Override
    public ContentValues toContentValues(Note element) throws DatabaseException {
        ContentValues values = new ContentValues();
        String title = element.getTitle();
        String body = element.getBody();
        Category category = element.getCategory();
        Date created = element.getCreated();
        Date modified = element.getModified();
        boolean hasReminder = element.isHasReminder();

        if(title == null || category == null || modified == null || created == null)
            throw new DatabaseException("Note is missing values");

        // Add all values
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_BODY, body);
        values.put(COLUMN_CATEGORY,category.toString());

        if(hasReminder){
            values.put(COLUMN_REMINDER, DATE_FORMAT.format(element.getReminder()));
            values.put(COLUMN_HAS_REMINDER, 1);
        }
        else{
            values.put(COLUMN_REMINDER, "");
            values.put(COLUMN_HAS_REMINDER, 0);
        }

        values.put(COLUMN_CREATED, DATE_FORMAT.format(created));
        values.put(COLUMN_MODIFIED, DATE_FORMAT.format(modified));
        return values;
    }

    @Override
    public Note fromCursor(Cursor cursor) throws DatabaseException, ParseException {
        Note note = new Note();
        note.setId(cursor.getInt(0));
        // Set all properties of the note
        note.setTitle(cursor.getString(1));
        note.setBody(cursor.getString(2));
        note.setCategory(Category.valueOf(cursor.getString(3)));

        // Check if note has a set reminder, set the reminder and flag
        int hasReminder = cursor.getInt(4);
        if(hasReminder == 1){
            Date reminder = DATE_FORMAT.parse(cursor.getString(5));
            note.setReminder(reminder);
            note.setHasReminder(true);
        }
        else{
            note.setHasReminder(false);
        }

        note.setCreated(DATE_FORMAT.parse(cursor.getString(6)));
        note.setModified(DATE_FORMAT.parse(cursor.getString(7)));
        return note;
    }


    @Override
    public boolean hasInitialData() {
        return true;
    }

    @Override
    public void initialize(SQLiteDatabase database) {
        for (Note n : SampleData.generateNotes()) {
            try {
                database.insert(TABLE_NAME, null, toContentValues(n));
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertNote(SQLiteDatabase database, Note n) throws DatabaseException {
        database.insert(TABLE_NAME, null, toContentValues(n));
    }
}
