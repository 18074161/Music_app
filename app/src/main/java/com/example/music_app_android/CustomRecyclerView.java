package com.example.music_app_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app_android.model.Song;

import java.util.ArrayList;
import java.util.List;

public class CustomRecyclerView extends RecyclerView.Adapter<CustomRecyclerView.ViewHolder> {
    List<Song> mSongs;
    OnClickListener listener;

    public CustomRecyclerView(List<Song> songs, OnClickListener listener) {
        mSongs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = mSongs.get(position);
        holder.asong = song;
        holder.imageView.setImageResource(song.getImage());
        holder.songSinger.setText(song.getSingle());
        holder.songTitle.setText(song.getTitle());
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView songTitle, songSinger;
        Song asong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rcv_image);
            songTitle = itemView.findViewById(R.id.rcv_title);
            songSinger = itemView.findViewById(R.id.rcv_singer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.clickItem(asong);
                }
            });
        }
    }
}
