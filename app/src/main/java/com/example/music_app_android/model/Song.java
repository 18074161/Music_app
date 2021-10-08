package com.example.music_app_android.model;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String single;
    private int image;
    private int resourece;

    public Song(String title, String single, int image, int resourece) {
        this.title = title;
        this.single = single;
        this.image = image;
        this.resourece = resourece;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getResourece() {
        return resourece;
    }

    public void setResourece(int resourece) {
        this.resourece = resourece;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", single='" + single + '\'' +
                ", image=" + image +
                ", resourece=" + resourece +
                '}';
    }
}
