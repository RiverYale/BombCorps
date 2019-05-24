package com.bombcorps.game.controller;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioController {
    public static final AudioController instance = new AudioController();
    private Music playingMusic;

    public void play(Music music) {
        stopMusic();
        playingMusic = music;
        float volume = DataController.instance.getVolMusic();
        if (volume > 0) {
            music.setLooping(true);
            music.setVolume(volume);
            music.play();
        }
    }

    public void stopMusic() {
        if (playingMusic != null){
            playingMusic.stop();
        }
    }

    public void play(Sound sound) {
        play(sound, 1);
    }

    public void play(Sound sound, float volume) {
        play(sound, volume, 1);
    }

    public void play(Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    }

    public void play(Sound sound, float multiply, float pitch, float pan) {
        float volume = DataController.instance.getVolSound();
        if (volume > 0){
            sound.play(volume * multiply, pitch, pan);
        }
    }

    public void onSettingsUpdated() {
        if (playingMusic != null){
            float volume = DataController.instance.getVolMusic();
            playingMusic.setVolume(volume);
            if (volume > 0) {
                if (!playingMusic.isPlaying()){
                    playingMusic.play();
                }
            } else {
                playingMusic.pause();
            }
        }
    }
}
