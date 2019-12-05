package com.nawacreative.whereikeep;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.nawacreative.whereikeep.R;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(3000)
                .withBackgroundColor(Color.parseColor("#FFFFFF"))
                .withFooterText("Developed by Nawa Ton, 2019")
                .withLogo(R.drawable.whereikeep_logo_withtext);

        config.getFooterTextView().setTextColor(Color.GRAY);

        View easysplashscreen = config.create();
        setContentView(easysplashscreen);


    }
}
