package com.example.notesapp.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notesapp.dao.NoteDao;
import com.example.notesapp.entities.Note;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase noteDatabase;

    public static synchronized NoteDatabase getNoteDatabase(Context context){
        if(noteDatabase == null){
            noteDatabase = Room.databaseBuilder(
                    context,
                    NoteDatabase.class,
                    "notes_db"
            ).build();
        }
        return noteDatabase;
    }

    public abstract NoteDao noteDao();

}
