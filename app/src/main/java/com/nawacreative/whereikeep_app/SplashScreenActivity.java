package com.nawacreative.whereikeep_app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(3000)
                .withBackgroundColor(Color.parseColor("#173F5F"))
                .withFooterText("Developed by Nawa Ton, 2019")
                .withLogo(R.drawable.applogo);

        config.getFooterTextView().setTextColor(Color.WHITE);

        View easysplashscreen = config.create();
        setContentView(easysplashscreen);


    }
}
