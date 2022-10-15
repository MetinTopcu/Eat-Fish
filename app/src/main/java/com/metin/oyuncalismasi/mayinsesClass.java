package com.metin.oyuncalismasi;

import android.media.MediaPlayer;

public class mayinsesClass {
    private MediaPlayer patlama;

    public mayinsesClass() {
    }

    public mayinsesClass(MediaPlayer patlama) {
        this.patlama = patlama;
        SettingsFragment settingsFragment = new SettingsFragment();
        if (settingsFragment.buttondur){

        }else {
            patlama.start();
        }

    }
}
