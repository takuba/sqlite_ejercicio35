package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "songs.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_URL = "url";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_SONGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_URL + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        onCreate(db);
    }

    public long addSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, song.getTitle());
        values.put(COLUMN_AUTHOR, song.getAuthor());
        values.put(COLUMN_URL, song.getUrl());
        long id = db.insert(TABLE_SONGS, null, values);
        db.close();
        return id;
    }

    public int updateSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, song.getTitle());
        values.put(COLUMN_AUTHOR, song.getAuthor());
        values.put(COLUMN_URL, song.getUrl());
        int rowsAffected = db.update(TABLE_SONGS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(song.getId())});
        db.close();
        return rowsAffected;
    }

    public void deleteSong(long songId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONGS, COLUMN_ID + " = ?", new String[]{String.valueOf(songId)});
        db.close();
    }

    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SONGS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                song.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                song.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)));
                song.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
                songs.add(song);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return songs;
    }
}
