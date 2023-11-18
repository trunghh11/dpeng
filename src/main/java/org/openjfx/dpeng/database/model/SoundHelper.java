package org.openjfx.dpeng.database.model;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundHelper {
    public static MediaPlayer soundInGame;
    // public static MediaPlayer soundInGame;
    // private MediaPlayer soundPush;

    public static MediaPlayer readSound(String path) {
        File file = new File(path);
        Media med = new Media(file.toURI().toString());
        MediaPlayer mp = new MediaPlayer(med);
        return mp;
    }

    public static void loadAllSound() {
        String fullPath = "src/main/java/org/openjfx/dpeng/database/model/inGame.mp3";
        soundInGame = readSound(fullPath);
    }

    public static void playSound(MediaPlayer sound) {
        if (sound == null) {
            return;
        }
        sound.play();
    }

    public static void pauseSound(MediaPlayer sound) {
        if (sound == null) {
            return;
        }
        if (sound.getStatus() == MediaPlayer.Status.PLAYING) {
            sound.pause();
        }
    }

    public static void stopSound(MediaPlayer sound) {
        if (sound == null) {
            return;
        }
        if (sound.getStatus() == MediaPlayer.Status.PLAYING) {
            sound.stop();
        }
    }

    public static void setVolume(MediaPlayer sound, double volume) {
        if (sound == null) {
            return;
        }
        sound.setVolume(volume / 100.0);
    }
}