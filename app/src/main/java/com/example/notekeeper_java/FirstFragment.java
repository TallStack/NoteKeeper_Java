package com.example.notekeeper_java;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {
    public static final String NOTE_POSITION = "com.example.NoteKeeper_Java.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo note;
    boolean isNewNote;
    Spinner spinnerCourses;
    EditText noteTitle;
    EditText noteText;
    int notePosition;
    private  NoteActivityViewModel noteActivityViewModel;
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

        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()));
        noteActivityViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        if(noteActivityViewModel.isNewlyCreated && savedInstanceState != null)
            noteActivityViewModel.restoreState(savedInstanceState);

        noteActivityViewModel.isNewlyCreated = false;

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(
                this.requireActivity(),android.R.layout.simple_spinner_item, courses
        );
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses = view.findViewById(R.id.spinner_courses);
        spinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();
        saveOriginalNoteValues();

        noteTitle = view.findViewById(R.id.txtNoteTitle);
        noteText = view.findViewById(R.id.txtNoteText);

        if(!isNewNote)
            displayNote(spinnerCourses, noteTitle, noteText);

        
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    private void saveOriginalNoteValues() {
        if(isNewNote)
            return;
        noteActivityViewModel.originalCourseId = note.getCourse().getCourseId();
        noteActivityViewModel.originalTitle = note.getTitle();
        noteActivityViewModel.originalText = note.getText();


    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        notePosition = dm.createNewNote();
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
        if(isNewNote) {
            createNewNote();
        }
        else {
            note = DataManager.getInstance().getNotes().get(position);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        //TODO implement proper isCancelling boolean
        if(NoteActivity.isCancelling)
        {
            if(isNewNote)
                DataManager.getInstance().removeNote(notePosition);
            else{
                storePreviousNoteValues();
            }
        } else {
            saveNote();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null)
            noteActivityViewModel.saveState(outState);
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(noteActivityViewModel.originalCourseId);
        note.setCourse(course);
        note.setTitle(noteActivityViewModel.originalTitle);
        note.setText(noteActivityViewModel.originalText);
    }

    private void saveNote() {
        note.setCourse((CourseInfo)spinnerCourses.getSelectedItem());
        note.setTitle(noteTitle.getText().toString());
        note.setText(noteText.getText().toString());
    }
}