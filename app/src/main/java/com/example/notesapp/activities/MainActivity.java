package com.example.notesapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.notesapp.R;
import com.example.notesapp.adapters.NoteAdapter;
import com.example.notesapp.database.NoteDatabase;
import com.example.notesapp.entities.Note;
import com.example.notesapp.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_AND_NOTE = 1;

    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private NoteAdapter noteAdapter;
    private NotesListener notesListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(getApplication(), CreateNoteActivity.class),REQUEST_CODE_AND_NOTE
                );
            }
        });

        notesRecyclerView = findViewById(R.id.noteRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        );

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList,notesListener);
        notesRecyclerView.setAdapter(noteAdapter);

        getNotes();
    }

    private void getNotes(){

        @SuppressLint("StaticFieldLeak")
        class GetNoteTask extends AsyncTask<Void, Void, List<Note>>{

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NoteDatabase
                        .getNoteDatabase(getApplication())
                        .noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if(noteList.size()==0){
                    noteList.addAll(notes);
                    noteAdapter.notifyDataSetChanged();
                }else {
                    noteList.add(0,notes.get(0));
                    noteAdapter.notifyItemInserted(0);
                }
                notesRecyclerView.smoothScrollToPosition(0);
            }

        }

        new GetNoteTask().execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_AND_NOTE && resultCode == RESULT_OK){
            getNotes();
        }
    }
}