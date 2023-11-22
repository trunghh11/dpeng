package org.openjfx.dpeng.database.model;

import java.io.File;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundHelper {
    public static MediaPlayer soundInGame;
    // public static MediaPlayer soundInGame;
    public static AudioClip soundClick;
    public static AudioClip soundPushLetter;
    public static AudioClip soundPopLetter;
    public static AudioClip soundCorrect;
    public static AudioClip soundInCorrect;
    public static AudioClip soundShowResult;

    public static MediaPlayer readSound(String path) {
        File file = new File(path);
        Media med = new Media(file.toURI().toString());
        MediaPlayer mp = new MediaPlayer(med);
        return mp;
    }

    public static void loadAllSound() {
        soundInGame = readSound("src/main/resources/org/openjfx/dpeng/sounds/GameSound/inGame.mp3");
        soundClick = new AudioClip(SoundHelper.class.getResource("/org/openjfx/dpeng/sounds/GameSound/mouseClick.wav").toString());
        soundPushLetter  = new AudioClip(SoundHelper.class.getResource("/org/openjfx/dpeng/sounds/GameSound/push.wav").toString());
        soundPopLetter  = new AudioClip(SoundHelper.class.getResource("/org/openjfx/dpeng/sounds/GameSound/pop.wav").toString());
        soundCorrect  = new AudioClip(SoundHelper.class.getResource("/org/openjfx/dpeng/sounds/GameSound/correct.wav").toString());
        soundInCorrect  = new AudioClip(SoundHelper.class.getResource("/org/openjfx/dpeng/sounds/GameSound/incorrect.wav").toString());
        soundShowResult  = new AudioClip(SoundHelper.class.getResource("/org/openjfx/dpeng/sounds/GameSound/showResult.wav").toString());

    }

    public static void playSound(MediaPlayer sound) {
        if (sound == null || sound.getStatus() == MediaPlayer.Status.PLAYING) {
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

    public static void stopGameSound() {
        stopSound(soundInGame);
    }
}