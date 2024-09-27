package Labs.Lab_8.exercise_1;

import java.util.ArrayList;
import java.util.List;


class Song {
    String title;
    String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title=" + title +
                ", artist=" + artist +
                '}';
    }
}

class MP3Player {
    int currentSong;
    List<Song> songs;
    boolean songsAlreadyStoped = false;
    public boolean SONG_IS_PLAYING = false;


    public MP3Player(List<Song> songs) {
        this.songs = songs;
        currentSong = 0;
    }

    public void pressPlay() {
        songsAlreadyStoped = false;
        if (SONG_IS_PLAYING) {
            System.out.println("Song is already playing");
        } else {
            SONG_IS_PLAYING = true;
            System.out.println(String.format("Song %d is playing", currentSong));
        }
    }

    public void pressStop() {
        if (SONG_IS_PLAYING) {
            System.out.println(String.format("Song %d is paused", currentSong));
            SONG_IS_PLAYING = false;
            songsAlreadyStoped = false;
        } else {
            if (!songsAlreadyStoped) {
                System.out.println("Songs are stopped");
                songsAlreadyStoped = true;
            } else {
                System.out.println("Songs are already stopped");
            }
            currentSong = 0;
        }


    }

    public void pressFWD() {
        System.out.println("Forward...");

        SONG_IS_PLAYING = false;
        if (currentSong == songs.size() - 1)
            currentSong = 0;
        else {
            currentSong++;
        }

    }

    public void pressREW() {
        System.out.println("Reward...");
        SONG_IS_PLAYING = false;

        if (currentSong == 0)
            currentSong = songs.size() - 1;
        else
            currentSong--;

    }

    public void printCurrentSong() {
        System.out.println(songs.get(currentSong));
    }

    @Override
    public String toString() {
        return "MP3Player{" +
                "currentSong = " + currentSong +
                ", songList = " + songs +
                '}';
    }

}

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde