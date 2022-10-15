package com.metin.oyuncalismasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class OyunEkraniActivity extends AppCompatActivity {

    private TextView textViewsifir;
    private TextView textViewOyunaBasla;
    private ImageView anakarakter,saridaire,siyahkare,kirmiziucgen,mavibalik,mayin,saridaire1,imageViewSettings;
    private ConstraintLayout cl;
    private TextView textViewKirmizi,textViewMavi,textViewSari;

    private Timer timer = new Timer();
    private Handler handler = new Handler();

    //Ses dosyaları

    private MediaPlayer patlama,balikyeme;

    //Balık skorları
    private int textViewK,textViewM,textViewS;

    //Pozisyonlar

    private int anakarakterX;
    private int anakarakterY;
    private int saridaireX;
    private int saridaireY;
    private int siyahkareX;
    private int siyahkareY;
    private int kirmiziucgenX;
    private int kirmiziucgenY;
    private int mavibalikX;
    private int mavibalikY;
    private int mayinX;
    private int mayinY;
    private int saridaire1X;
    private int saridaire1Y;

    //Boyutlar
    private int ekranGenisligi;
    private int ekranYuksekligi;
    private int anakarakterGenisligi;
    private int anakarakterYuksekligi;

    //Hızlar
    private int anakarakterHiz;
    private int saridaireHiz;
    private int siyahkareHiz;
    private int kirmiziucgenHiz;
    private int mavibalikHiz;
    private int mayinHiz;
    private int saridaire1Hiz;

    //Kontroller

    private boolean dokunmaKontrol = false;
    private boolean baslangıcKontrol = false;
    private boolean devam = true;

    private int skor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oyun_ekrani);

        imageViewSettings = findViewById(R.id.imageViewSettings);
        textViewOyunaBasla = findViewById(R.id.textViewOyunaBasla);
        textViewsifir = findViewById(R.id.textViewsifir);
        anakarakter = findViewById(R.id.anakarakter);
        saridaire = findViewById(R.id.saridaire);
        siyahkare = findViewById(R.id.siyahkare);
        kirmiziucgen = findViewById(R.id.kirmiziucgen);
        mavibalik = findViewById(R.id.mavibalik);
        mayin = findViewById(R.id.mayin);
        cl = findViewById(R.id.cl);
        textViewKirmizi = findViewById(R.id.textViewKirmizi);
        textViewMavi = findViewById(R.id.textViewMavi);
        textViewSari = findViewById(R.id.textViewSari);
        saridaire1 = findViewById(R.id.saridaire1);

        patlama = MediaPlayer.create(getApplicationContext(),R.raw.patlama);
        balikyeme = MediaPlayer.create(getApplicationContext(),R.raw.balikyeme);

        siyahkare.setX(-300);
        siyahkare.setY(-300);
        saridaire.setX(-300);
        saridaire.setY(-300);
        kirmiziucgen.setX(-300);
        kirmiziucgen.setY(-300);
        mavibalik.setX(-300);
        mavibalik.setY(-300);
        mayin.setX(-300);
        mayin.setY(-300);
        saridaire1.setX(-300);
        saridaire1.setY(-300);

        imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.add(R.id.fragment_tutucu,new SettingsFragment(),"B");
                ft.commit();
            }
        });

        cl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (baslangıcKontrol){
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        //ekrana dokundu
                        dokunmaKontrol = true;
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP){
                        //ekranı bıraktı
                        dokunmaKontrol = false;
                    }
                }else {
                    baslangıcKontrol=true;

                    textViewOyunaBasla.setVisibility(View.INVISIBLE);

                    anakarakterX = (int) anakarakter.getX();
                    anakarakterY = (int) anakarakter.getY();

                    anakarakterGenisligi = anakarakter.getWidth();
                    anakarakterYuksekligi = anakarakter.getHeight();
                    ekranGenisligi = cl.getWidth();
                    ekranYuksekligi = cl.getHeight();

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    anakarakterHareketEttirme();
                                    cisimleriHareketEttir();
                                    carpismaKontrol();
                                }
                            });
                        }
                    },0,20);

                }
                return true;
            }
        });
    }

    public void anakarakterHareketEttirme(){

        anakarakterHizAyari();

        if (dokunmaKontrol){
            anakarakterY -= anakarakterHiz;
        }else {
            anakarakterY +=anakarakterHiz;
        }
        if (anakarakterY <=0){
            anakarakterY = 0;
        }

        if (anakarakterY >= ekranYuksekligi - anakarakterYuksekligi){
            anakarakterY = ekranYuksekligi - anakarakterYuksekligi;
        }
        anakarakter.setY(anakarakterY);
    }

    public void cisimleriHareketEttir(){

        if (skor<20){
            saridaire1Hiz = Math.round(ekranGenisligi/90);
            saridaireHiz = Math.round(ekranGenisligi/80);
            siyahkareHiz = Math.round(ekranGenisligi/70);
            mayinHiz = Math.round(ekranGenisligi/80);
            kirmiziucgenHiz = Math.round(ekranGenisligi/80);
            mavibalikHiz = Math.round(ekranGenisligi/80);
        }
        else if (skor <80){
            saridaire1Hiz = Math.round(ekranGenisligi/75);
            saridaireHiz = Math.round(ekranGenisligi/85);
            siyahkareHiz = Math.round(ekranGenisligi/70);
            mayinHiz = Math.round(ekranGenisligi/70);
            kirmiziucgenHiz = Math.round(ekranGenisligi/60);
            mavibalikHiz = Math.round(ekranGenisligi/70);
        }

        else if (skor<200){
            saridaire1Hiz = Math.round(ekranGenisligi/65);
            saridaireHiz = Math.round(ekranGenisligi/75);
            siyahkareHiz = Math.round(ekranGenisligi/65);
            mayinHiz = Math.round(ekranGenisligi/62);
            kirmiziucgenHiz = Math.round(ekranGenisligi/52);
            mavibalikHiz = Math.round(ekranGenisligi/62);
        }
        else if (skor<500){
            saridaire1Hiz = Math.round(ekranGenisligi/62);
            saridaireHiz = Math.round(ekranGenisligi/72);
            siyahkareHiz = Math.round(ekranGenisligi/62);
            mayinHiz = Math.round(ekranGenisligi/60);
            kirmiziucgenHiz = Math.round(ekranGenisligi/50);
            mavibalikHiz = Math.round(ekranGenisligi/60);
        }
        else if (skor<1000){
            saridaire1Hiz = Math.round(ekranGenisligi/60);
            saridaireHiz = Math.round(ekranGenisligi/70);
            siyahkareHiz = Math.round(ekranGenisligi/60);
            mayinHiz = Math.round(ekranGenisligi/55);
            kirmiziucgenHiz = Math.round(ekranGenisligi/45);
            mavibalikHiz = Math.round(ekranGenisligi/55);
        }else {
            siyahkareHiz = Math.round(ekranGenisligi/50);
            mayinHiz = Math.round(ekranGenisligi/50);
        }

        siyahkareX -= siyahkareHiz;

        if (siyahkareX <0 ){
            siyahkareX = ekranGenisligi + 20;
            siyahkareY = (int) Math.floor(Math.random() * ekranYuksekligi);
        }

        siyahkare.setX(siyahkareX);
        siyahkare.setY(siyahkareY);

        saridaireX -= saridaireHiz;

        if (saridaireX <0 ){
            saridaireX = ekranGenisligi + 20;
            saridaireY= (int) Math.floor(Math.random() * ekranYuksekligi);
        }

        saridaire.setX(saridaireX);
        saridaire.setY(saridaireY);

        saridaire1X -= saridaire1Hiz;

        if (saridaire1X <0 ){
            saridaire1X = ekranGenisligi + 30;
            saridaire1Y= (int) Math.floor(Math.random() * ekranYuksekligi);
        }

        saridaire1.setX(saridaire1X);
        saridaire1.setY(saridaire1Y);

        kirmiziucgenX -= kirmiziucgenHiz;

        if (kirmiziucgenX <0 ){
            kirmiziucgenX = ekranGenisligi + 20;
            kirmiziucgenY = (int) Math.floor(Math.random() * ekranYuksekligi);
        }

        kirmiziucgen.setX(kirmiziucgenX);
        kirmiziucgen.setY(kirmiziucgenY);

        mavibalikX -= mavibalikHiz;

        if (mavibalikX <0 ){
            mavibalikX = ekranGenisligi + 20;
            mavibalikY= (int) Math.floor(Math.random() * ekranYuksekligi);
        }

        mavibalik.setX(mavibalikX);
        mavibalik.setY(mavibalikY);

        mayinX -= mayinHiz;

        if (mayinX < 0 && skor >= 500){
            mayinX = ekranGenisligi + 20;
            mayinY = (int) Math.floor(Math.random() * ekranYuksekligi);
        }

        mayin.setX(mayinX);
        mayin.setY(mayinY);
    }

    public void carpismaKontrol(){

        int saridaireMerkezX = saridaireX + saridaire.getWidth()/2;
        int saridaireMerkezY = saridaireY + saridaire.getHeight()/2;

        if (0 <= saridaireMerkezX && saridaireMerkezX <= anakarakterGenisligi
                && anakarakterY <= saridaireMerkezY && saridaireMerkezY <= anakarakterY+anakarakterYuksekligi){

            balikyemeSes();
            skor += 25;
            saridaireX = -10;
            textViewS += 1;
            textViewSari.setText(String.valueOf(textViewS));
        }

        int saridaire1MerkezX = saridaire1X + saridaire1.getWidth()/2;
        int saridaire1MerkezY = saridaire1Y + saridaire1.getHeight()/2;

        if (0 <= saridaire1MerkezX && saridaire1MerkezX <= anakarakterGenisligi
                && anakarakterY <= saridaire1MerkezY && saridaire1MerkezY <= anakarakterY+anakarakterYuksekligi){
           balikyemeSes();
            skor += 25;
            saridaire1X = -10;
            textViewS += 1;
            textViewSari.setText(String.valueOf(textViewS));
        }

        int kirmiziucgenMerkezX = kirmiziucgenX + kirmiziucgen.getWidth()/2;
        int kirmiziucgenMerkezY = kirmiziucgenY + kirmiziucgen.getHeight()/2;

        if (0 <= kirmiziucgenMerkezX && kirmiziucgenMerkezX <= anakarakterGenisligi
                && anakarakterY <= kirmiziucgenMerkezY && kirmiziucgenMerkezY <= anakarakterY+anakarakterYuksekligi){
            balikyemeSes();
            skor += 50;
            kirmiziucgenX = -10;
            textViewK += 1;
            textViewKirmizi.setText(String.valueOf(textViewK));
        }

        int mavibalikMerkezX = mavibalikX + mavibalik.getWidth()/2;
        int mavibalikMerkezY = mavibalikY + mavibalik.getHeight()/2;

        if (0 <= mavibalikMerkezX && mavibalikMerkezX <= anakarakterGenisligi
                && anakarakterY <= mavibalikMerkezY && mavibalikMerkezY <= anakarakterY+anakarakterYuksekligi){
            balikyemeSes();
            skor += 40;
            mavibalikX = -10;
            textViewM += 1;
            textViewMavi.setText(String.valueOf(textViewM));
        }

        int siyahkareMerkezX = siyahkareX + siyahkare.getWidth()/2;
        int siyahkareMerkezY = siyahkareY + siyahkare.getHeight()/2;

        if (0 <= siyahkareMerkezX && siyahkareMerkezX <= anakarakterGenisligi
                && anakarakterY <= siyahkareMerkezY && siyahkareMerkezY <= anakarakterY+anakarakterYuksekligi){
            siyahkareX = -10;

            mayinyemeSes();
           carpismaveriaktarim();
        }

        int mayinMerkezX = mayinX + mayin.getWidth()/2;
        int mayinMerkezY = mayinY + mayin.getHeight()/2;

        if (0 <= mayinMerkezX && mayinMerkezX <= anakarakterGenisligi
                && anakarakterY <= mayinMerkezY && mayinMerkezY <= anakarakterY+anakarakterYuksekligi){
            mayinX = -10;

            mayinyemeSes();
            carpismaveriaktarim();

        }

        if (devam){
            textViewsifir.setText(String.valueOf(skor));

        }else {
            textViewsifir.setText("0");
            textViewKirmizi.setText("0");
            textViewMavi.setText("0");
            textViewSari.setText("0");
        }

    }

    public void carpismaveriaktarim(){
        //Timer durdur.
        timer.cancel();
        timer = null;

        siyahkare.setX(-300);
        siyahkare.setY(-300);
        saridaire.setX(-300);
        saridaire.setY(-300);
        kirmiziucgen.setX(-300);
        kirmiziucgen.setY(-300);
        mavibalik.setX(-300);
        mavibalik.setY(-300);
        mayin.setX(-300);
        mayin.setY(-300);
        saridaire1.setX(-300);
        saridaire1.setY(-300);
        anakarakter.setX(-300);
        anakarakter.setY(-300);
        devam = false;

        Bundle bundle = new Bundle();
        bundle.putInt("skor",skor);
        bundle.putInt("skorm",textViewM);
        bundle.putInt("skork",textViewK);
        bundle.putInt("skors",textViewS);


        ReklamFragment reklamFragment = new ReklamFragment();
        reklamFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_tutucu,reklamFragment);
        transaction.commit();
    }

    private void mayinyemeSes(){
        if (balikyeme.isPlaying()){
            balikyeme.pause();
            mayinsesClass mayinsesClass = new mayinsesClass(patlama);
        }else{
            mayinsesClass mayinsesClass = new mayinsesClass(patlama);
        }
    }

    private void balikyemeSes(){
        if (balikyeme.isPlaying()){
            balikyeme.pause();
            balikyeme.seekTo(0);
            baliksesClass baliksesClass = new baliksesClass(balikyeme);
        }else{
            baliksesClass baliksesClass = new baliksesClass(balikyeme);
        }
    }

    private void anakarakterHizAyari(){

        Intent intent = getIntent();
        int sensivitydegeri = intent.getIntExtra("key",0);

        SettingsFragment settingsFragment = new SettingsFragment();
        if (sensivitydegeri == 50){
            anakarakterHiz = Math.round(ekranYuksekligi/60);
        }
        else if(sensivitydegeri > 50 && sensivitydegeri <= 55){
            anakarakterHiz = Math.round(ekranYuksekligi/58);
        }
        else if(settingsFragment.sensivitydegeri > 55 && settingsFragment.sensivitydegeri <= 60){
            anakarakterHiz = Math.round(ekranYuksekligi/56);
        }
        else if(settingsFragment.sensivitydegeri > 60 && settingsFragment.sensivitydegeri <= 65){
            anakarakterHiz = Math.round(ekranYuksekligi/54);
        }
        else if(settingsFragment.sensivitydegeri > 65 && settingsFragment.sensivitydegeri <= 75){
            anakarakterHiz = Math.round(ekranYuksekligi/52);
        }
        else if(settingsFragment.sensivitydegeri > 75 && settingsFragment.sensivitydegeri <= 90){
            anakarakterHiz = Math.round(ekranYuksekligi/50);
        }
        else if(sensivitydegeri > 90 && sensivitydegeri <= 100){
            anakarakterHiz = Math.round(ekranYuksekligi/15);
        }
        else if(settingsFragment.sensivitydegeri >= 45 && settingsFragment.sensivitydegeri < 50){
            anakarakterHiz = Math.round(ekranYuksekligi/62);
        }
        else if(settingsFragment.sensivitydegeri >= 40 && settingsFragment.sensivitydegeri < 45){
            anakarakterHiz = Math.round(ekranYuksekligi/64);
        }
        else if(settingsFragment.sensivitydegeri >= 35 && settingsFragment.sensivitydegeri < 40){
            anakarakterHiz = Math.round(ekranYuksekligi/66);
        }
        else if(settingsFragment.sensivitydegeri >= 25 && settingsFragment.sensivitydegeri < 35){
            anakarakterHiz = Math.round(ekranYuksekligi/70);
        }
        else if(settingsFragment.sensivitydegeri >= 15 && settingsFragment.sensivitydegeri < 25){
            anakarakterHiz = Math.round(ekranYuksekligi/74);
        }
        else if(settingsFragment.sensivitydegeri >= 0 && settingsFragment.sensivitydegeri < 15){
            anakarakterHiz = Math.round(ekranYuksekligi/78);
        }

    }

}

