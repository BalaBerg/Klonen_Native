package com.example.klonen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    // for Animation :
    private static final int SPLASH_SCREEN = 5000; // 5 sec
    Animation sp_anim;
    ImageView splash_img;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  ---> for Full
        setContentView(R.layout.activity_main);

        // Animation :
        sp_anim = AnimationUtils.loadAnimation(this,    R.anim.splash_anim);

        // Apply animation to Image :
        splash_img = findViewById(R.id.sp_img);

        splash_img.setAnimation(sp_anim);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Switch to New Page : after the Timeout ( Delay )
        new Handler().postDelayed(() -> {

            if (currentUser != null){
                startActivity(new Intent(MainActivity.this,Home.class));
                finish();
            }
            else{
                startActivity(new Intent(MainActivity.this,LoginPage.class));
                finish();
            }// To avoid the splash screen.
        },SPLASH_SCREEN);
    }
}