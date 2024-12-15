package vn.edu.stu.leafmusic.database;

import java.util.ArrayList;
import vn.edu.stu.leafmusic.model.Song;

public class DataManager {
    private static DataManager instance;
    private ArrayList<Song> favoriteSongs;

    private DataManager() {
        favoriteSongs = new ArrayList<>();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public ArrayList<Song> getFavoriteSongs() {
        return favoriteSongs;
    }

    public void addFavoriteSong(Song song) {
        if (favoriteSongs == null) {
            favoriteSongs = new ArrayList<>();
        }
        favoriteSongs.add(song);
    }

    public void removeFavoriteSong(Song song) {
        if (favoriteSongs != null) {
            favoriteSongs.remove(song);
        }
    }
}
