package com.example.womenssafety;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    ImageView im1,im2;
    Animation a;

    @Override
      protected void onCreate(Bundle savedInstanceState)
      {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            im1=findViewById(R.id.ima1);
            im2=findViewById(R.id.ima2);
          Thread t = new Thread() {
              public void run() {
                  try {
                      // Load animations
                      a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
                      im1.startAnimation(a);
                      a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation1);
                      im2.startAnimation(a);

                      // Wait for the animation to finish before starting pg1
                      sleep(3500);  // Increased delay to ensure animations are completed

                      // Start pg1 activity after animation
                      Intent i = new Intent(MainActivity.this, pg1.class);
                      startActivity(i);
                      finish();
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }
          };
          t.start();

        }

}