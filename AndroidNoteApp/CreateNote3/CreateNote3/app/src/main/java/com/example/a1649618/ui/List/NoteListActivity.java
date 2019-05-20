package com.example.a1649618.ui.List;

import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.a1649618.createnote.R;
import com.example.a1649618.model.Note;
import com.example.a1649618.sqlite.DatabaseException;
import com.example.a1649618.ui.Editor.NoteEditActivity;

import java.util.Date;
import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    public static final int COLUMNS = 2;
    public RecyclerView noteRecyclerView;
    private NoteListFragment noteListFragment;
    private View mainCoordinatorLayout;
    private int position;
    private boolean isNew;
    private Note backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        // --------- Recycler view --------- \\
        noteRecyclerView = findViewById(R.id.note_recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, COLUMNS, GridLayoutManager.VERTICAL, false);
        noteRecyclerView.setLayoutManager(gridLayoutManager);

        // --------- Toolbar --------- \\
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // --------- Find components by their ids --------- \\
        noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mainCoordinatorLayout = findViewById(R.id.main_coordinatorLayout);
        FloatingActionButton newNoteFloatingActionBtn = findViewById(R.id.newNote_floatingActionButton);


        // --------- Preps and opens the NoteEdit Activity --------- \\
        noteListFragment.setOnNoteChosenListener((note, position) -> {
            backup = note.clone();
            // Opens up the edit screen
            NoteListActivity.this.position = position;
            Intent intent = new Intent(NoteListActivity.this, NoteEditActivity.class);
            isNew = note.getTitle() == null;
            if(note != null) intent.putExtra(NoteEditActivity.param.INITIAL, note);

            startActivityForResult(intent, 1);
        });

        // --------- Floating action event listener --------- \\
        newNoteFloatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note newNote = new Note();
                newNote.setCreated(new Date());
                newNote.setModified(new Date());
                noteListFragment.notes.add(newNote);

                try {
                    noteListFragment.dbh.insertNote(newNote);
                } catch (DatabaseException e) {
                    e.printStackTrace();
                }
                noteListFragment.callOnNoteChosen(newNote, noteListFragment.notes.indexOf(newNote));
            }
        });
    }

    /**
     * Handle the results of the note edit
     * @param requestCode code sent with the intent
     * @param resultCode result of the intent
     * @param data contains parcelable extra --> the note
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Note note = data.getParcelableExtra(NoteEditActivity.result.FINAL);
        noteListFragment.setNote(note, position);

        position = noteListFragment.getPosition(note);
        DisplaySnackbar(isNew);
    }

    /**
     * Pops up snackbar when a note is edited or created
     * Has UNDO action, allows user to undo the change they just made
     * @param isNewNote true if note is new, false if note is just updated
     */
    private void DisplaySnackbar(boolean isNewNote){
        String text = isNewNote ? "Note created." : "Note updated";
        Snackbar.make(mainCoordinatorLayout, text, Snackbar.LENGTH_LONG)
                .setAction("UNDO", new SnackbarClickListener())
                .show();
    }

    /**
     * CLASS --> OnClickListener for UNDO action on snackbar
     */
    private class SnackbarClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(isNew)
                // delete the new note at position
                noteListFragment.removeNote(position);
            else
                // revert the note to previous at position
                noteListFragment.setNote(backup, position);
        }
    }
}
