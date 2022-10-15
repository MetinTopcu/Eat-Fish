package com.metin.oyuncalismasi;

import android.content.Context;
import android.media.MediaPlayer;

public class buttonsesClass {
    private MediaPlayer button_ses;

    public buttonsesClass() {
    }

    public buttonsesClass(MediaPlayer button_ses) {
        this.button_ses = button_ses;
        SettingsFragment settingsFragment = new SettingsFragment();
        if (settingsFragment.buttondur){

        }else {
            button_ses.start();
        }
    }


}
