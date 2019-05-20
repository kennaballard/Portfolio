package com.example.a1649618.ui.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.a1649618.model.Category;
import com.example.a1649618.model.NoteDatabaseHandler;
import com.example.a1649618.model.SampleData;
import com.example.a1649618.createnote.R;
import com.example.a1649618.model.Note;
import com.example.a1649618.model.User;
import com.example.a1649618.sqlite.DatabaseException;
import com.example.a1649618.ui.util.DatePickerDialogFragment;
import com.example.a1649618.ui.util.TimePickerDialogFragment;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class NoteListFragment extends Fragment {

    /**
     * Sets a note after it has been edited
     */
    public void setNote(Note note, int position) {
        // Set properties
        notes.get(position)
                .setModified(note.getModified())
                .setCreated(note.getCreated())
                .setHasReminder(note.isHasReminder())
                .setReminder(note.getReminder())
                .setTitle(note.getTitle())
                .setBody(note.getBody())
                .setCategory(note.getCategory());

        // Update in SQLite database
        try {
            dbh.getNotesTable().update(notes.get(position));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        // Update in view
        updateInformation();
    }

    /**
     * Event listener interface
     */
    public interface OnNoteChosenListener {
        /**
         * Contact is chosen from the list
         * @param note
         * @param position
         */
        void onNoteChosen(Note note, int position);
    }
    public void setOnNoteChosenListener(OnNoteChosenListener onNoteChosenListener){
        this.onNoteChosenListener = onNoteChosenListener;
    }



    /******************************************************
        Fields
     ******************************************************/
    // region List of fields for the note list fragment
    private OnNoteChosenListener onNoteChosenListener;
    private Spinner sortSpinner;
    private RecyclerView noteRecyclerView;
    public List<Note> notes;

    private NoteAdapter noteAdapter;
    private ArrayAdapter<String> spinnerAdapter;

    NoteDatabaseHandler dbh;

    private ActionMode actionMode;
    private Note selected;

    // Sorting fields
    private final String TITLE = "Title";
    private final String CREATE = "Creation Date";
    private final String MOD = "Last Modified";
    private final String REMINDER = "Reminder";
    private final String CAT = "Category";

    //endregion

    public NoteListFragment() {
    }

    /********************************************************
     * On Create function
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     *******************************************************/
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note_list, container, false);

        // ---------- Initialize ------------- \\
        sortSpinner =  root.findViewById(R.id.sort_spinner);

        noteRecyclerView =  root.findViewById(R.id.note_recyclerView);
        noteRecyclerView.setHasFixedSize(true);

        // ---------- Database Setup ------------- \\
        Context ctx = getContext();
        dbh = new NoteDatabaseHandler(ctx);
        try {
            notes = dbh.getNotesTable().readAll();
        } catch (DatabaseException | ParseException e) {
            e.printStackTrace();
        }



        // ---------- Adapters ------------- \\
        // Spinner
        List<String> spinnerArray = new ArrayList<>();
        addSpinnerItems(spinnerArray);

        spinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);

        // Recycler view
        noteAdapter = new NoteAdapter();
        noteRecyclerView.setAdapter(noteAdapter);

        // ---------- Event Listeners ------------- \\
        // On select for sorting notes
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Sort notes by spinner selection
                String selection = parent.getItemAtPosition(position).toString();
                SortNotesBy(selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // On scroll close open ActionMode
        // --> Otherwise does weird things
        noteRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(actionMode != null)
                    actionMode.finish();
            }
        });

        return root;
    }

    /********************************************************
     * NOTE VIEWHOLDER CLASS
     ********************************************************/
    private class NoteViewHolder extends RecyclerView.ViewHolder {
        // UI components
        private final TextView titleTextView;
        private final TextView bodyTextView;
        private final TextView reminderTextView;
        private final ConstraintLayout backgroundConstraintLayout;

        // Note and position
        private Note note;
        private int position;

        // Max # of lines to appear in preview
        private final int MAX_LINES_BODY = 3;
        private final int MAX_LINES_TITLE = 1;

        @SuppressLint("ClickableViewAccessibility")
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find components by id
            titleTextView = itemView.findViewById(R.id.Title_textView);
            titleTextView.setMaxLines(MAX_LINES_TITLE);
            bodyTextView = itemView.findViewById(R.id.Body_textView);
            bodyTextView.setMaxLines(MAX_LINES_BODY);
            reminderTextView = itemView.findViewById(R.id.Reminder_textView);
            backgroundConstraintLayout = itemView.findViewById(R.id.Background_constraintLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = note;
                    actionMode = v.startActionMode(actionModeCallback, ActionMode.TYPE_FLOATING);
                }
            });
        }

        /**
         * Updates a note's display
         * @param note containing the updated values
         */
        public void setNote(Note note) {
            this.note = note;

            titleTextView.setText(note.getTitle());
            bodyTextView.setText(note.getBody());
            if(note.isHasReminder()){
                SimpleDateFormat df = new SimpleDateFormat("EEEE, MMM d 'at' h:mm a");
                reminderTextView.setText(df.format(note.getReminder()));
                reminderTextView.setVisibility(View.VISIBLE);
            }
            else{
                reminderTextView.setVisibility(View.INVISIBLE);
            }
            Category cat = note.getCategory();
            setBackgroundColor(cat, backgroundConstraintLayout);
        }
    }

    /********************************************************
     * NOTE ADAPTER CLASS
     ********************************************************/
    private class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder>{
        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.list_item_note, viewGroup, false);
            // return a ViewHolder
            return new NoteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int pos) {
            noteViewHolder.setNote(notes.get(pos));
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }
    }

    /**
     * Sets the background colour of an individual note
     * @param category the colour
     * @param background the view that will change colour
     */
    private void setBackgroundColor(Category category, View background){
        int backgroundColor = 0;
        // Sets appropriate colour background of note preview
        switch(category){
            case GREEN: backgroundColor = getResources().getColor(R.color.GREEN, null); break;
            case RED: backgroundColor = getResources().getColor(R.color.PINK, null); break;
            case BROWN: backgroundColor = getResources().getColor(R.color.BROWN, null); break;
            case LIGHT_BLUE: backgroundColor = getResources().getColor(R.color.LIGHT_BLUE, null); break;
            case DARK_BLUE: backgroundColor = getResources().getColor(R.color.DARK_BLUE, null); break;
            case PURPLE: backgroundColor = getResources().getColor(R.color.PURPLE, null); break;
            case YELLOW: backgroundColor = getResources().getColor(R.color.YELLOW, null); break;
            case ORANGE: backgroundColor = getResources().getColor(R.color.ORANGE, null); break;
        }
        background.setBackgroundColor(backgroundColor);
    }

    /**
     * Initializes the spinner items
     * @param spinnerItems list of items for the spinner
     */
    private void addSpinnerItems(List<String> spinnerItems){
        spinnerItems.add(TITLE);
        spinnerItems.add(MOD);
        spinnerItems.add(CREATE);
        spinnerItems.add(CAT);
        spinnerItems.add(REMINDER);
    }

    /**
     * Sorts the notes by selection
     * @param selection the chosen item in the spinner
     */
    private void SortNotesBy(String selection){
        switch(selection){
            case TITLE:
                notes.sort(Comparator.comparing(Note::getTitle));
                break;
            case CREATE:
                notes.sort(Comparator.comparing(Note::getCreated));
                break;
            case MOD:
                notes.sort(Comparator.comparing(Note::getModified).reversed());
                break;
            case REMINDER:
                notes.sort(Comparator.comparing(Note::getReminder, Comparator.nullsLast(Comparator.naturalOrder())));
                break;
            case CAT:
                notes.sort(Comparator.comparing(Note::getCategory));
                break;
        }
        noteAdapter.notifyDataSetChanged();
    }

    /**
     * ActionMode Callback
     * For floating ActionMode for note
     */
    public ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        /**
         * Creates the action mode floating menu
         * @param mode the action mode
         * @param menu the layout/menu items to include
         * @return true when action mode has been created
         */
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // ActionMode is created when a note is clicked
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        /**
         * Performs action that was clicked
         * @param mode the actionMode
         * @param item the menuItem clicked (Can be Edit, Reminder, Trash, Close)
         * @return if callback handled the event
         */
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int index = notes.indexOf(selected);
            switch(item.getItemId()){
                case R.id.actionmode_close:
                    // Just close the menu
                    mode.finish();
                    return true;
                case R.id.actionmode_reminder:
                    // Add or edit the reminder
                    chooseDate();
                    mode.finish();
                    return true;
                case R.id.actionmode_trash:
                    // Delete note
                    removeNote(index);

                    // Notify the user it's been deleted
                    mode.finish();
                    Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.actionmode_edit:
                    // Calls method to open NoteEditActivity
                    callOnNoteChosen(selected, index);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };

    /**
     * Calls method setup in activity
     * @param note the note to edit
     * @param index position of the note
     */
    public void callOnNoteChosen(Note note, int index){
        // Call the method that opens the noteEdit
        if(onNoteChosenListener != null)
            onNoteChosenListener.onNoteChosen(note, index);
    }

    /**
     * Gets the date for reminder
     * Calls chooseTime()
     */
    private void chooseDate() {
        // Today's date
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        Date chosenDate = selected.getReminder();
        // A reminder hasn't been set yet
        if(chosenDate == null){
            // Set to default
            c.setTime(date);
            c.add(Calendar.DATE, 1);
        }
        // Reminder has been set
        else{
            // Set to previous reminder date
            c.setTime(chosenDate);
        }

        final Date tomorrow = c.getTime();
        DatePickerDialogFragment dialogFragment = DatePickerDialogFragment.create(tomorrow, (view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();

            // Check if a reminder hasn't been set already
            // --> set to default
            if(chosenDate == null){
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 0);
            }
            else{
                // Set to previous reminder time
                calendar.setTime(chosenDate);
            }
            calendar.set(year, month, dayOfMonth);
            final Date date1 = calendar.getTime();

            // Open time picker
            chooseTime(date1);
        });
        dialogFragment.show(getFragmentManager(), "datePicker");
    }

    /**
     * Gets the time for reminder
     * @param tomorrow the date selected
     */
    private void chooseTime(final Date tomorrow) {
        final TimePickerDialogFragment dialogFragment = TimePickerDialogFragment.create(tomorrow, (view, hourOfDay, minute) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tomorrow);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            Date date = calendar.getTime();
            SimpleDateFormat df = new SimpleDateFormat("EEEE, MMM d 'at' h:mm a");

            // UPDATE NOTE REMINDER
            // In memory
            selected.setReminder(date);
            selected.setModified(new Date());
            selected.setHasReminder(true);
            // In database
            int index = notes.indexOf(selected);
            try {
                dbh.getNotesTable().update(notes.get(index));
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            updateInformation();
        });
        dialogFragment.show(getFragmentManager(), "timePicker");
    }

    /**
     * Updates the recyclerview
     * notifies data change
     * Refreshes sort
     */
    private void updateInformation(){
        // Update the data and the sort
        noteAdapter.notifyDataSetChanged();
        SortNotesBy(sortSpinner.getSelectedItem().toString());
    }

    /**
     * Deletes a note
     * @param pos the position of note to be deleted
     */
    public void removeNote(int pos){
        // Delete the note from the database
        try {
            dbh.getNotesTable().delete(notes.get(pos));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        // Delete from memory
        notes.remove(pos);

        // A change was made
        updateInformation();
    }

    /**
     * Get the position of a specific note
     * @param n note
     * @return index
     */
    public int getPosition(Note n){
        return notes.indexOf(n);
    }
}
