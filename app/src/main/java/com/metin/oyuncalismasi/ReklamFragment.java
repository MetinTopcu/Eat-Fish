package com.metin.oyuncalismasi;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


public class ReklamFragment extends Fragment {

    private Button buttonOdul,buttonReklamIzle;

    private MediaPlayer button_ses;

    //Reklam
    private RewardedAd mRewardedAd;
    private AdRequest adRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reklam, container, false);

        button_ses = MediaPlayer.create(this.getActivity(),R.raw.sonucekrani);

        buttonReklamIzle = view.findViewById(R.id.buttonReklamIzle);

        adRequest = new AdRequest.Builder().build();

        loadAd();

        buttonReklamIzle.setOnClickListener(View->{
            buttonsesClass buttonsesClass = new buttonsesClass(button_ses);

            if (mRewardedAd != null){
                mRewardedAd.show(getActivity(), new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        loadAd();
                    }
                });
            }

        });


        buttonOdul = view.findViewById(R.id.buttonOdul);

        buttonOdul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsesClass buttonsesClass = new buttonsesClass(button_ses);

                Bundle bundle = getArguments();
                if (bundle != null){
                    int skor = bundle.getInt("skor",0);
                    int skorm = bundle.getInt("skorm",0);
                    int skork = bundle.getInt("skork",0);
                    int skors = bundle.getInt("skors",0);

                    bundle.putInt("skor",skor);
                    bundle.putInt("skorm",skorm);
                    bundle.putInt("skork",skork);
                    bundle.putInt("skors",skors);
                    SonucFragment sonucFragment = new SonucFragment();
                    sonucFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_tutucu,sonucFragment,"A");
                    transaction.commit();
                }else {
                    Toast.makeText(getActivity(),"Sıkıntı büyüdü",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
    public void loadAd(){
        RewardedAd.load(getActivity(), "ca-app-pub-3940256099942544/5224354917", adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                mRewardedAd = rewardedAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mRewardedAd = null;
            }
        });

    }
}