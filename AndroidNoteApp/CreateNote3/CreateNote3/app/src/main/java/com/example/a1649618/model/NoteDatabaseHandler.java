package com.example.a1649618.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.a1649618.sqlite.DatabaseException;
import com.example.a1649618.sqlite.Table;

import com.example.a1649618.sqlite.TableFactory;

/**
 * Notes database
 */
public class NoteDatabaseHandler extends SQLiteOpenHelper {

    /**
     * Filename to store the local database (on device).
     */
    private static final String DATABASE_FILE_NAME = "notes.db";

    /**
     * Update this field for every structural change to the database.
     */
    private static final int DATABASE_VERSION = 1;

    private Context context;

    /*  NoteDatabaseHandler Tables */
    private Table<Note> notesTable;
    private Table<User> usersTable;
    private Table<Collaborator> collaboratorsTable;

    /**
     * Construct a new database handler.
     * @param context The application context.
     */
    public NoteDatabaseHandler(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);

        usersTable = TableFactory.makeFactory(this, User.class)
                                 .setSeedData(SampleData.generateUsers(context))
                                 .getTable();

        notesTable = TableFactory.makeFactory(this, Note.class)
                                .setSeedData(SampleData.generateNotes())
                                .getTable();

        collaboratorsTable = TableFactory.makeFactory(this, Collaborator.class)
                                         .getTable();

        this.context = context;
    }

    /**
     * Get the Category table.
     * @return The Category table.
     */
    /*public CategoryTable getCategoryTable() {
        return categoryTable;
    }*/

    @Override
    public void onCreate(SQLiteDatabase database) {
        //database.execSQL(categoryTable.getCreateSQL());
        database.execSQL(notesTable.getCreateTableStatement());
        database.execSQL(usersTable.getCreateTableStatement());
        database.execSQL(collaboratorsTable.getCreateTableStatement());

        if(usersTable.hasInitialData())
            usersTable.initialize(database);
        if(notesTable.hasInitialData())
            notesTable.initialize(database);
        if(collaboratorsTable.hasInitialData())
            collaboratorsTable.initialize(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(NoteDatabaseHandler.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        database.execSQL(notesTable.getDropTableStatement());
        database.execSQL(usersTable.getDropTableStatement());
        database.execSQL(collaboratorsTable.getDropTableStatement());
        onCreate(database);
    }

    public Table<Note> getNotesTable() {
        return notesTable;
    }

    public Table<User> getUsersTable() {
        return usersTable;
    }

    public Table<Collaborator> getCollaboratorsTable() {
        return collaboratorsTable;
    }

    public Context getContext() {
        return context;
    }

    public void insertNote(Note note) throws DatabaseException {
        notesTable.create(note);
    }

    public void insertCollaborator(Collaborator collaborator) throws DatabaseException {
        collaboratorsTable.create(collaborator);
    }
}
