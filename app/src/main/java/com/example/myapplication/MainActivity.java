package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SongAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private EditText editTextTitle, editTextAuthor, editTextURL;
    private Button buttonAdd, buttonUpdate, buttonDelete;
    private DatabaseHelper dbHelper;
    private SongAdapter adapter;
    private Button buttonPlay;
    private Song selectedSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextURL = findViewById(R.id.editTextURL);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);

        dbHelper = new DatabaseHelper(this);
        adapter = new SongAdapter(dbHelper.getAllSongs(), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSongToDatabase();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSongInDatabase();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSongFromDatabase();
            }
        });
        buttonPlay = findViewById(R.id.buttonPlay);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSong != null) {
                    playSongInBrowser(selectedSong.getUrl());
                } else {
                    Toast.makeText(MainActivity.this, "Selecciona una canción antes de reproducir", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(Song song) {
        editTextTitle.setText(song.getTitle());
        editTextAuthor.setText(song.getAuthor());
        editTextURL.setText(song.getUrl());
        selectedSong = song;
    }

    private void addSongToDatabase() {
        String title = editTextTitle.getText().toString();
        String author = editTextAuthor.getText().toString();
        String url = editTextURL.getText().toString();

        Song newSong = new Song(title, author, url);

        long id = dbHelper.addSong(newSong);
        if (id != -1) {
            adapter.setSongs(dbHelper.getAllSongs());
            clearEditTextFields();
        } else {
            Toast.makeText(this, "Error al añadir la canción", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSongInDatabase() {
        String title = editTextTitle.getText().toString();
        String author = editTextAuthor.getText().toString();
        String url = editTextURL.getText().toString();

        if (title.isEmpty() || author.isEmpty() || url.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPosition = adapter.getSelectedPosition();
        if (selectedPosition == RecyclerView.NO_POSITION) {
            Toast.makeText(this, "Selecciona una canción de la lista", Toast.LENGTH_SHORT).show();
            return;
        }

        Song selectedSong = adapter.getItemAtPosition(selectedPosition);

        Song updatedSong = new Song();
        updatedSong.setId(selectedSong.getId());
        updatedSong.setTitle(title);
        updatedSong.setAuthor(author);
        updatedSong.setUrl(url);

        int rowsAffected = dbHelper.updateSong(updatedSong);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Canción actualizada con éxito", Toast.LENGTH_SHORT).show();
            adapter.setSongs(dbHelper.getAllSongs());
            clearEditTextFields();
        } else {
            Toast.makeText(this, "Error al actualizar la canción", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteSongFromDatabase() {
        int selectedPosition = adapter.getSelectedPosition();
        if (selectedPosition == RecyclerView.NO_POSITION) {
            Toast.makeText(this, "Selecciona una canción de la lista", Toast.LENGTH_SHORT).show();
            return;
        }

        Song selectedSong = adapter.getItemAtPosition(selectedPosition);
        dbHelper.deleteSong(selectedSong.getId()); // Elimina la canción seleccionada por su ID
        adapter.setSongs(dbHelper.getAllSongs());
        clearEditTextFields();
    }


    private void clearEditTextFields() {
        editTextTitle.setText("");
        editTextAuthor.setText("");
        editTextURL.setText("");
    }
    private void playSongInBrowser(String url) {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else {
            Toast.makeText(this, "La canción no tiene una URL válida.", Toast.LENGTH_SHORT).show();
        }
    }
}