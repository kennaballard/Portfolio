package com.example.a1649618.ui.Editor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.a1649618.createnote.R;
import com.example.a1649618.model.Category;
import com.example.a1649618.model.Collaborator;
import com.example.a1649618.model.Note;
import com.example.a1649618.model.NoteDatabaseHandler;
import com.example.a1649618.model.User;
import com.example.a1649618.sqlite.DatabaseException;
import com.example.a1649618.ui.util.AddCollaboratorDialogFragment;
import com.example.a1649618.ui.util.CircleView;
import com.example.a1649618.ui.util.DatePickerDialogFragment;
import com.example.a1649618.ui.util.TimePickerDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * A placeholder fragment containing a simple view.
 */
public class NoteEditFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    //region Fields for NoteEditFragment
    private LinearLayout hiddenView;
    private ConstraintLayout reminderLayout;
    private LinearLayout collabHiddenLinLayout;
    private ConstraintLayout mainConstraintLayout;

    private NoteDatabaseHandler dbh;

    private CircleView pink;
    private CircleView rust;
    private CircleView lightBlue;
    private CircleView darkBlue;
    private CircleView purple;
    private CircleView orange;
    private CircleView beige;
    private CircleView green;
    private EditText bodyEditText;
    private EditText titleEditText;
    private TextView reminderText;
    private ImageButton addCollabImageButton;

    private Date chosenDate;

    // collaborators
    // all possible user
    private List<User> users;

    // collaborators from database
    private List<Collaborator> allCollaborators;
    // collaborator ids for current note
    private List<Long> collaboratorIds;
    // Users for current note
    private ArrayList<User> collaborators;
    // Users not collaborators for current note
    private ArrayList<User> options;

    private RecyclerView collaboratorsRecyclerView;
    private RecyclerView.Adapter<CollaboratorViewHolder> collaboratorViewHolderAdapterdapter;

    public static Note theNote = new Note();
    private SimpleDateFormat df = new SimpleDateFormat("EEEE, MMM d 'at' h:mm a");
    private final String reminderString = "Reminder: ";
    private final String noReminder = "Add Reminder";

    // Saves the different states --> undo
    private Stack<Note> states = new Stack<>();

    //endregion

    public NoteEditFragment() {
        theNote.setCreated(new Date());
        theNote.setBody("");
        theNote.setTitle("");
        theNote.setReminder(null);
        theNote.setCategory(Category.RED);
        modified();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_note, container, false);

        // ------------------ Database handle ------------------- \\
        Context ctx = getContext();
        dbh = new NoteDatabaseHandler(ctx);
        try {
            users = dbh.getUsersTable().readAll();
            allCollaborators = dbh.getCollaboratorsTable().readAll();
        } catch (DatabaseException | ParseException e) {
            e.printStackTrace();
        }

        // ---------------- Find components by id ---------------- \\
        Switch openViewSwitch = root.findViewById(R.id.openView_switch);

        // Hidden Views
        hiddenView = root.findViewById(R.id.hiddenLinLayout);
        reminderLayout = root.findViewById(R.id.hiddenConstLayout);
        collabHiddenLinLayout = root.findViewById(R.id.collab_hiddenLinLayout);
        mainConstraintLayout = root.findViewById(R.id.main_constraintLayout);

        // Circle Views
        pink = root.findViewById(R.id.pink_CV);
        purple = root.findViewById(R.id.purple_CV);
        rust = root.findViewById(R.id.rust_CV);
        orange = root.findViewById(R.id.orange_CV);
        beige = root.findViewById(R.id.beige_CV);
        green = root.findViewById(R.id.green_CV);
        lightBlue = root.findViewById(R.id.lightBlue_CV);
        darkBlue = root.findViewById(R.id.darkBlue_CV);

        // backgrounds
        bodyEditText = root.findViewById(R.id.body_editText);
        titleEditText = root.findViewById(R.id.title_editText);

        // Reminder
        reminderText = root.findViewById(R.id.reminder_textView);

        // Undo Button
        ImageButton undoBtn = root.findViewById(R.id.undo_btn);

        // Collaborator
        addCollabImageButton = root.findViewById(R.id.addCollab_imageButton);

        // ---------------- Event Listeners ---------------- \\
        // Switch onclick listener
        openViewSwitch.setOnCheckedChangeListener(this);

        // Circle views onclick listener
        View.OnClickListener circleViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircleView cv = (CircleView) v;
                int colour = cv.getColor();
                // Set the colour
                titleEditText.setBackgroundColor(colour);
                bodyEditText.setBackgroundColor(colour);
                mainConstraintLayout.setBackgroundColor(colour);

                // Modified
                modified();
                // set category
                if(colour == pink.getColor())
                    theNote.setCategory(Category.RED);
                else if(colour == purple.getColor())
                    theNote.setCategory(Category.PURPLE);
                else if(colour == green.getColor())
                    theNote.setCategory(Category.GREEN);
                else if(colour == lightBlue.getColor())
                    theNote.setCategory(Category.LIGHT_BLUE);
                else if(colour == darkBlue.getColor())
                    theNote.setCategory(Category.DARK_BLUE);
                else if(colour == rust.getColor())
                    theNote.setCategory(Category.BROWN);
                else if(colour == orange.getColor())
                    theNote.setCategory(Category.ORANGE);
                else if(colour == beige.getColor())
                    theNote.setCategory(Category.YELLOW);
            }
        };

        pink.setOnClickListener(circleViewListener);
        purple.setOnClickListener(circleViewListener);
        rust.setOnClickListener(circleViewListener);
        orange.setOnClickListener(circleViewListener);
        beige.setOnClickListener(circleViewListener);
        green.setOnClickListener(circleViewListener);
        lightBlue.setOnClickListener(circleViewListener);
        darkBlue.setOnClickListener(circleViewListener);

        // Title is changed
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                modified();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                theNote.setTitle(s.toString());
            }
        });

        // Body is changed
        bodyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                modified();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                theNote.setBody(s.toString());
            }
        });

        // Reminder selected
        reminderText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chooseDate();
                theNote.setHasReminder(true);
                modified();
            }
        });

        // Undo Button event listener
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pop the last state of the Note
                if(!states.empty()){
                    Note lastNote = states.pop();
                    String title = lastNote.getTitle();
                    String body = lastNote.getBody();
                    Date reminder = lastNote.getReminder();
                    boolean hasRemind = lastNote.isHasReminder();
                    Category colour = lastNote.getCategory();

                    // Update current object to match the previous state
                    theNote.setReminder(reminder);
                    theNote.setBody(body);
                    theNote.setTitle(title);
                    chosenDate = reminder;
                    theNote.setHasReminder(hasRemind);
                    theNote.setCategory(colour);

                    // Update view
                    titleEditText.setText(title);
                    states.pop();
                    bodyEditText.setText(body);
                    states.pop();

                    if(reminder == null)
                        reminderText.setText(noReminder);
                    else
                        reminderText.setText(reminderString + df.format(reminder));

                    updateColour(colour);
                }
            }
        });

        // Add collaborator event listener
        addCollabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // popup dialog
                options = new ArrayList<>();
                users.forEach(u ->{
                    if(!collaborators.contains(u))
                        options.add(u);
                });
                AddCollaboratorDialogFragment collaboratorDialogFragment = new AddCollaboratorDialogFragment(options, NoteEditFragment.this);
                collaboratorDialogFragment.show(getFragmentManager(), "addCollaborator");
            }
        });

        // Setup Collaborators
        collaboratorSetup();

        collaboratorsRecyclerView = root.findViewById(R.id.collaborator_RecyclerView);
        collaboratorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        collaboratorsRecyclerView.setHasFixedSize(true);
        collaboratorViewHolderAdapterdapter = new CollaboratorAdapter();
        collaboratorsRecyclerView.setAdapter(collaboratorViewHolderAdapterdapter);

        return root;
    }

    private class CollaboratorViewHolder extends RecyclerView.ViewHolder {

        private final ImageView avatarImageView;

        public CollaboratorViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatar_ImageView);
        }

        public void setCollaborator(User user) {
            avatarImageView.setImageBitmap(user.getAvatar());
        }
    }

    private class CollaboratorAdapter extends RecyclerView.Adapter<CollaboratorViewHolder>{

        @NonNull
        @Override
        public CollaboratorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            return new CollaboratorViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_item_collaborator_avatar_only, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull CollaboratorViewHolder holder, int position) {
            holder.setCollaborator(collaborators.get(position));
        }

        @Override
        public int getItemCount() {
            return collaborators.size();
        }
    }

    /**
     * Changes the background colour to selected
     * @param cat the selected colour
     */
    private void updateColour(Category cat) {
        int colour;
        switch(cat){
            case BROWN:
                colour = rust.getColor();
                break;
            case RED:
                colour = pink.getColor();
                break;
            case PURPLE:
                colour = purple.getColor();
                break;
            case GREEN:
                colour = green.getColor();
                break;
            case ORANGE:
                colour = orange.getColor();
                break;
            case YELLOW:
                colour = beige.getColor();
                break;
            case LIGHT_BLUE:
                colour = lightBlue.getColor();
                break;
            default:
                colour = darkBlue.getColor();
        }

        titleEditText.setBackgroundColor(colour);
        bodyEditText.setBackgroundColor(colour);
        mainConstraintLayout.setBackgroundColor(colour);
    }

    /**
     * Get the reminder date
     * Calls chooseTime()
     */
    private void chooseDate() {
        // Today's date
        Date date = new Date();
        Calendar c = Calendar.getInstance();

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
        DatePickerDialogFragment dialogFragment = DatePickerDialogFragment.create(tomorrow, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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
                final Date date = calendar.getTime();

                // Open time picker
                chooseTime(date);
            }
        });
        dialogFragment.show(getFragmentManager(), "datePicker");
    }

    /**
     * Get the reminder time
     * @param tomorrow the selected date
     */
    private void chooseTime(final Date tomorrow) {
        final TimePickerDialogFragment dialogFragment = TimePickerDialogFragment.create(tomorrow, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tomorrow);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                Date date = calendar.getTime();

                reminderText.setText(reminderString + df.format(date));
                chosenDate = date;
                theNote.setReminder(chosenDate);
            }
        });
        FragmentManager fm = getFragmentManager();
        dialogFragment.show(fm, "timePicker");
    }

    /**
     * Displays or hide the colour and reminder bar
     * @param buttonView the button
     * @param isChecked is the button checked: true --> display, false --> hide
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            // make the layouts visible
            hiddenView.setVisibility(View.VISIBLE);
            reminderLayout.setVisibility(View.VISIBLE);
            collabHiddenLinLayout.setVisibility(View.VISIBLE);
        }
        else {
            // hide the layouts
            hiddenView.setVisibility(View.GONE);
            reminderLayout.setVisibility(View.GONE);
            collabHiddenLinLayout.setVisibility(View.GONE);
        }
    }

    /**
     * A note has been changed
     * Update the modification date
     */
    private void modified(){
        // save the state
        theNote.setModified(new Date());
        states.push(theNote.clone());
    }

    /**
     * Get the current note
     * @return the note
     */
    public Note getNote(){
        return theNote;
    }

    /**
     * Updates the current note
     * @param note contains values to apply to current note
     */
    public void setNote(Note note) {
        // Set all fields of the Note
        theNote.setId(note.getId());
        theNote.setHasReminder(note.isHasReminder());
        theNote.setTitle(note.getTitle());
        theNote.setBody(note.getBody());
        theNote.setCategory(note.getCategory());
        theNote.setReminder(note.getReminder());
        theNote.setCreated(note.getCreated());
        theNote.setModified(note.getModified());

        // Make sure the collaborators match the Note ID
        collaboratorSetup();
        // Update the visual components to match the note
        updateView();
    }

    /**
     * Updates the visual components
     * Uses values stored in theNote
     */
    private void updateView(){
        // Updates the visible fields of the Note edit
        titleEditText.setText(theNote.getTitle());
        bodyEditText.setText(theNote.getBody());

        updateColour(theNote.getCategory());

        if(theNote.isHasReminder())
            reminderText.setText((reminderString + df.format(theNote.getReminder())));
        else
            reminderText.setText(noReminder);
    }


    public void addCollaborator(User user) throws DatabaseException {
        // update view
        collaborators.add(user);
        collaboratorViewHolderAdapterdapter.notifyDataSetChanged();

        // update database
        Collaborator newCollaborator = new Collaborator();
        newCollaborator.setNoteId(theNote.getId());
        newCollaborator.setUserId(user.getId());

        dbh.insertCollaborator(newCollaborator);
    }

    private void collaboratorSetup(){
        // Get user ids this note has as collaborators
        collaboratorIds = allCollaborators.stream()
                .filter(c -> c.getNoteId() == theNote.getId())
                .map(Collaborator::getUserId).collect(Collectors.toList());

        // RECYCLER VIEW
        collaborators = new ArrayList<>();
        for (User user : users) {
            if(collaboratorIds.contains(user.getId()))
                collaborators.add(user);
        }
    }
}
