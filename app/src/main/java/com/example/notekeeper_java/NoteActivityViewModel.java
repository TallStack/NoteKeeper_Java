package com.example.notekeeper_java;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.example.notekeeper_java.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.example.notekeeper_java.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.example.notekeeper_java.ORIGINAL_NOTE_TEXT";

    public String originalCourseId;
    public String originalTitle;
    public String originalText;
    public boolean isNewlyCreated = true;

    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID,originalCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE,originalTitle);
        outState.putString(ORIGINAL_NOTE_TEXT,originalText);
    }

    public void restoreState(Bundle inState)
    {
        originalCourseId = inState.getString(ORIGINAL_NOTE_COURSE_ID);
        originalTitle = inState.getString(ORIGINAL_NOTE_TITLE);
        originalText = inState.getString(ORIGINAL_NOTE_TEXT);

    }
}
