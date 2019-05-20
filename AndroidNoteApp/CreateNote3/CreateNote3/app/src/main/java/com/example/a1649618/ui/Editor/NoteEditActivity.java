package com.example.a1649618.ui.Editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.example.a1649618.createnote.R;
import com.example.a1649618.model.Note;

public class NoteEditActivity extends AppCompatActivity {
    public static class param {
        public static final String INITIAL = "initial_note";
    }

    public static class result {
        public static final String FINAL = "final_note";
    }

    NoteEditFragment noteEditFragment;

    private ShareActionProvider menuShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // event handler for back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note finalNote = noteEditFragment.getNote();
                if(finalNote.getTitle().isEmpty()){
                    Toast.makeText(noteEditFragment.getContext(), "Your note title cannot be empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(result.FINAL, finalNote);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        noteEditFragment = (NoteEditFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        Intent intent = getIntent();
        Note note = intent.getParcelableExtra(param.INITIAL);
        if(note != null){
            noteEditFragment.setNote(note);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        String data = NoteEditFragment.theNote.toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, data);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

        return super.onOptionsItemSelected(item);
    }

}
