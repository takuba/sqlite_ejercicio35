package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private List<Song> songs;
    private OnItemClickListener itemClickListener;
    private static int selectedPosition = RecyclerView.NO_POSITION;

    public SongAdapter(List<Song> songs, OnItemClickListener itemClickListener) {
        this.songs = songs;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.bind(song);
        holder.itemView.setSelected(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Song song);
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public Song getItemAtPosition(int position) {
        if (position >= 0 && position < songs.size()) {
            return songs.get(position);
        }
        return null;
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView authorTextView;

        public SongViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textTitle);
            authorTextView = itemView.findViewById(R.id.textAuthor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        selectedPosition = position;
                        notifyDataSetChanged();
                        itemClickListener.onItemClick(songs.get(position));
                    }
                }
            });
        }

        public void bind(Song song) {
            titleTextView.setText(song.getTitle());
            authorTextView.setText(song.getAuthor());
        }
    }
}