package vn.edu.stu.leafmusic.database;

import java.util.ArrayList;
import vn.edu.stu.leafmusic.model.Song;

public class DataManager {
    private static DataManager instance;
    private ArrayList<Song> favoriteSongs;
    private ArrayList<Song> playlistSongs = new ArrayList<>();

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

    public void addSongToPlaylist(Song song) {
        if (!playlistSongs.contains(song)) {
            playlistSongs.add(song);
        }
    }

    public ArrayList<Song> getPlaylistSongs() {
        return playlistSongs;
    }

    public void removeSongFromPlaylist(Song song) {
        if (playlistSongs != null) {
            playlistSongs.remove(song);
        }
    }
}
