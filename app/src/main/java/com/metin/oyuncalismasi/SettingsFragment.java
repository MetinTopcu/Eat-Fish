package com.metin.oyuncalismasi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment {
    private ImageView imageViewmusic,imageViewtiklama,imageViewdevam;
    private SeekBar seekBarsensivity;
    private TextView textViewsensivityayar;

    private MediaPlayer kick_ses;
    private MediaPlayer button_ses;

    public Boolean dur = true;
    public Boolean buttondur = true;

    public int sensivitydegeri = 50;

    private SharedPreferences preferences;
    private static final String PROGRESS = "SEEKBAR";
    private static final String SENSIVITY = "sensivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        kick_ses = MediaPlayer.create(getActivity(),R.raw.music);
        button_ses = MediaPlayer.create(this.getActivity(),R.raw.sonucekrani);

        imageViewdevam = view.findViewById(R.id.imageViewdevam);
        imageViewmusic = view.findViewById(R.id.imageViewmusic);
        imageViewtiklama = view.findViewById(R.id.imageViewtiklama);
        seekBarsensivity = view.findViewById(R.id.seekBarsensivity);
        textViewsensivityayar = view.findViewById(R.id.textViewsensivityayar);

        preferences = getActivity().getSharedPreferences(" ",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        String a = "R.drawable.ic_baseline_music_note_24";
        seekBarsensivity.setProgress(preferences.getInt(PROGRESS,50));
        textViewsensivityayar.setText(String.valueOf(preferences.getInt(SENSIVITY,50)));


        editor.putBoolean("isLogin",dur);
        dur = preferences.getBoolean("isLogin",true);

        if (dur){
            imageViewmusic.setImageResource(R.drawable.ic_baseline_music_note_24);
            dur = false;
        }else {
            imageViewmusic.setImageResource(R.drawable.ic_baseline_music_off_24);
            dur = true;
        }

        seekBarsensivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sensivitydegeri = progress;

                textViewsensivityayar.setText(String.valueOf(sensivitydegeri));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt(SENSIVITY,sensivitydegeri);
                editor.putInt(PROGRESS,seekBar.getProgress());
                editor.commit();

                Intent intent = new Intent(getActivity(),OyunEkraniActivity.class);
                intent.putExtra("key",sensivitydegeri);
            }
        });

        imageViewmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.commit();
                if (dur){
                    imageViewmusic.setImageResource(R.drawable.ic_baseline_music_note_24);
                    dur = false;
                }else {
                    imageViewmusic.setImageResource(R.drawable.ic_baseline_music_off_24);
                    dur = true;
                }
            }
        });

        imageViewtiklama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences("Sonuc", Context.MODE_PRIVATE);
                buttondur = sp.getBoolean("buttondurdu",false);
                SharedPreferences.Editor editor = getContext().getSharedPreferences("Sonuc",Context.MODE_PRIVATE).edit();
                editor.putBoolean("buttondurdu",buttondur);
                if (buttondur){
                    imageViewtiklama.setImageResource(R.drawable.audio);
                    buttondur = false;
                }else {
                    imageViewtiklama.setImageResource(R.drawable.noaudio);
                    buttondur = true;
                }
                editor.commit();
            }
        });

        imageViewdevam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                imageViewdevam.startAnimation(myAnim);

                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                        buttonsesClass buttonsesClass = new buttonsesClass(button_ses);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        FragmentManager manager;
                        manager = getFragmentManager();
                        SettingsFragment fa = (SettingsFragment) manager.findFragmentByTag("B");
                        FragmentTransaction transaction = manager.beginTransaction();
                        if (fa != null){
                            transaction.remove(fa);
                            transaction.commit();
                        }else {
                            Toast.makeText(getActivity(),"Hata var",Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        return view;

    }

}