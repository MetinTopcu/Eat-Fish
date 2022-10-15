package com.metin.oyuncalismasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class SonucEkraniActivity extends AppCompatActivity {
    private TextView textViewToplamskor,textViewEnYuksekSkor;
    private Button buttonTekrarDene;
    private TextView textViewkirmizisonuc,textViewsarisonuc,textViewmavisonuc;
    private AdView banner;

    private MediaPlayer button_ses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sonuc_ekrani);

        banner = findViewById(R.id.banner);

        button_ses = MediaPlayer.create(getApplicationContext(),R.raw.sonucekrani);

        textViewEnYuksekSkor = findViewById(R.id.textViewEnYuksekSkor);
        textViewToplamskor = findViewById(R.id.textViewToplamSkor);
        buttonTekrarDene = findViewById(R.id.buttonTekrarDene1);
        textViewkirmizisonuc = findViewById(R.id.textViewkirmizisonuc);
        textViewmavisonuc = findViewById(R.id.textViewmavisonuc);
        textViewsarisonuc = findViewById(R.id.textViewsarisonuc);

        MobileAds.initialize(SonucEkraniActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        int skor = getIntent().getIntExtra("skor",0);
        textViewToplamskor.setText(String.valueOf(skor));


        SharedPreferences sp = getSharedPreferences("Sonuc", Context.MODE_PRIVATE);
        int enYuksekSkor = sp.getInt("enYuksekSkor",0);



        if (skor > enYuksekSkor){
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("enYuksekSkor",skor);
            editor.commit();

            textViewEnYuksekSkor.setText(String.valueOf(skor));
        }else{
            textViewEnYuksekSkor.setText(String.valueOf(enYuksekSkor));
        }

        int skorm = getIntent().getIntExtra("maviskor",0);
        int skork = getIntent().getIntExtra("kirmiziskor",0);
        int skors = getIntent().getIntExtra("sariskor",0);
        textViewkirmizisonuc.setText(String.valueOf(skork));
        textViewmavisonuc.setText(String.valueOf(skorm));
        textViewsarisonuc.setText(String.valueOf(skors));

        buttonTekrarDene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation myAnim = AnimationUtils.loadAnimation(SonucEkraniActivity.this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                buttonTekrarDene.startAnimation(myAnim);

                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        button_ses.start();
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startActivity(new Intent(SonucEkraniActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

    }
}