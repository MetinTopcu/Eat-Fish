package com.metin.oyuncalismasi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class SonucFragment extends Fragment {

    private TextView textViewToplamSkor,textViewkirmizisonuc,textViewmavisonuc,textViewsarisonuc,textViewEnYuksekSkor;
    private Context context;
    private AdView banner;
    private MediaPlayer button_ses;
    private ImageView buttonTekrarDene;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sonuc, container, false);

        banner = rootView.findViewById(R.id.banner);

        button_ses = MediaPlayer.create(this.getActivity(),R.raw.sonucekrani);

        textViewToplamSkor = rootView.findViewById(R.id.textViewToplamSkor);
        textViewkirmizisonuc = rootView.findViewById(R.id.textViewkirmizisonuc);
        textViewmavisonuc = rootView.findViewById(R.id.textViewmavisonuc);
        textViewsarisonuc = rootView.findViewById(R.id.textViewsarisonuc);
        textViewEnYuksekSkor = rootView.findViewById(R.id.textViewEnYuksekSkor);
        buttonTekrarDene = rootView.findViewById(R.id.buttonTekrarDene);

        MobileAds.initialize(this.getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        Bundle bundle = getArguments();
        if (bundle !=null){
            int skor = bundle.getInt("skor",0);
            int skorm = bundle.getInt("skorm",0);
            int skork = bundle.getInt("skork",0);
            int skors = bundle.getInt("skors",0);

            textViewToplamSkor.setText(String.valueOf(skor));
            textViewmavisonuc.setText(String.valueOf(skorm));
            textViewkirmizisonuc.setText(String.valueOf(skork));
            textViewsarisonuc.setText(String.valueOf(skors));

            SharedPreferences sp = this.getActivity().getSharedPreferences("Sonuc", Context.MODE_PRIVATE);
            int enYuksekSkor = sp.getInt("enYuksekSkor",0);


            if (skor > enYuksekSkor){
                SharedPreferences.Editor editor = getContext().getSharedPreferences("Sonuc",Context.MODE_PRIVATE).edit();
                editor.putInt("enYuksekSkor",skor);
                editor.commit();

                textViewEnYuksekSkor.setText(String.valueOf(skor));
            }else{
                textViewEnYuksekSkor.setText(String.valueOf(enYuksekSkor));
            }
        }else {
            Toast.makeText(getContext(),"HATA !!",Toast.LENGTH_LONG).show();
        }

        buttonTekrarDene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                buttonTekrarDene.startAnimation(myAnim);

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
                        SonucFragment fa = (SonucFragment) manager.findFragmentByTag("A");
                        FragmentTransaction transaction = manager.beginTransaction();
                        if (fa != null){
                            transaction.remove(fa);
                            transaction.commit();
                        }else {
                            Toast.makeText(getActivity(),"Hata var",Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = getActivity().getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().overridePendingTransition(0, 0);
                        getActivity().finish();

                        getActivity().overridePendingTransition(0, 0);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        return rootView;
    }


}