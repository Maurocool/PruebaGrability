package com.grability.PruebaGrability;
 
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
 
public class SplashScreenActivity extends CustomActivity {
 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.up_from_bottom);
//        animation.setStartOffset(position * Const.ANIMATIO_START_OFF_SET);
        
        ImageView imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgLogo.startAnimation(animation);
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
 
}