package com.example.notekeeper_java;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {
    public static final String NOTE_POSITION = "com.example.NoteKeeper_Java.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo note;
    boolean isNewNote;
    Spinner spinnerCourses;
    EditText noteTitle;
    EditText noteText;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(
                this.requireActivity(),android.R.layout.simple_spinner_item, courses
        );
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses = view.findViewById(R.id.spinner_courses);
        spinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();

        noteTitle = view.findViewById(R.id.txtNoteTitle);
        noteText = view.findViewById(R.id.txtNoteText);

        if(isNewNote) {
            createNewNote();
        }
        else {
            displayNote(spinnerCourses, noteTitle, noteText);
        }
        
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        int notePosition = dm.createNewNote();
        note = dm.getNotes().get(notePosition);
    }

    private void displayNote(Spinner spinnerCourses, EditText txtNoteTitle, EditText txtNoteText) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        int CourseIndex = courses.indexOf(note.getCourse());
        spinnerCourses.setSelection(CourseIndex);
        txtNoteTitle.setText(note.getTitle());
        txtNoteText.setText(note.getText());
    }

    private void readDisplayStateValues()
    {
        Intent intent = getActivity().getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        isNewNote = position == POSITION_NOT_SET;
        if(!isNewNote)
            note = DataManager.getInstance().getNotes().get(position);
    }


    @Override
    public void onPause() {
        super.onPause();
        saveNote();
    }

    private void saveNote() {
        note.setCourse((CourseInfo)spinnerCourses.getSelectedItem());
        note.setTitle(noteTitle.getText().toString());
        note.setText(noteText.getText().toString());
    }
}