package com.metin.oyuncalismasi;

import android.media.MediaPlayer;

public class baliksesClass {
    private MediaPlayer balikyeme;


    public baliksesClass() {
    }

    public baliksesClass(MediaPlayer balikyeme) {
        this.balikyeme = balikyeme;
        SettingsFragment settingsFragment = new SettingsFragment();
        if (settingsFragment.buttondur){

        }else {
            balikyeme.start();
        }
    }
}
