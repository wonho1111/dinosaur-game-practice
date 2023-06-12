package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_gun;
    ImageView iv_bullet;
    ImageView iv_clay;

    double screen_width, screen_height;
    float bullet_height, bullet_width;
    float gun_height, gun_width;
    float clay_height, clay_width;
    float bullet_center_x, bullet_center_y;
    float clay_center_x, clay_center_y;
    double gun_x, gun_y;
    double bullet_x, bullet_y_before, bullet_y_after;
    double gun_center_x;

    final int NO_OF_CLAYS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.layout);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_width = size.x;
        screen_height = size.y;

        iv_bullet = new ImageView(this);
        iv_gun    = new ImageView(this);
        iv_clay   = new ImageView(this);

        iv_gun.setImageResource(R.drawable.gun);
        iv_gun.measure(iv_gun.getMeasuredWidth(), iv_gun.getMeasuredHeight());
        gun_height = iv_gun.getMeasuredHeight();
        gun_width = iv_gun.getMeasuredWidth();
        layout.addView(iv_gun);

        iv_bullet.setImageResource(R.drawable.bullet);
        iv_bullet.measure(iv_bullet.getMeasuredWidth(), iv_bullet.getMeasuredHeight());
        bullet_height = iv_bullet.getMeasuredHeight();
        bullet_width  = iv_bullet.getMeasuredWidth();
        //iv_bullet.setVisibility(View.INVISIBLE);
        layout.addView(iv_bullet);

        iv_clay.setImageResource(R.drawable.clay);
        iv_clay.setScaleX(0.8f);
        iv_clay.setScaleY(0.8f);
        iv_clay.measure(iv_bullet.getMeasuredWidth(), iv_bullet.getMeasuredHeight());
        clay_height = iv_clay.getMeasuredHeight();
        clay_width  = iv_clay.getMeasuredWidth();
        iv_clay.setVisibility(View.INVISIBLE);   // step 2
        layout.addView(iv_clay);

        gun_center_x = screen_width * 0.1;
        gun_x = gun_center_x - gun_width * 0.5;
        gun_y = screen_height - gun_height;
        iv_gun.setX((float)gun_x);
        iv_gun.setY((float)gun_y - 100f);


        bullet_x = screen_width * 0.1;
        bullet_y_before = screen_height * 0.7;
        bullet_y_after = screen_height * 0.1;
        iv_bullet.setX((float)bullet_x);
        iv_bullet.setY((float)(bullet_y_before));

        ObjectAnimator clay_translateX = ObjectAnimator.ofFloat(iv_clay, "translationX", (float)screen_width + 100f, -200f);
        ObjectAnimator clay_translateY = ObjectAnimator.ofFloat(iv_clay, "translationY", 650f, 650f);
        ObjectAnimator clay_rotation   = ObjectAnimator.ofFloat(iv_clay, "rotation", 0f, 1080f);
        clay_translateX.setRepeatCount(NO_OF_CLAYS-1);
        clay_translateY.setRepeatCount(NO_OF_CLAYS-1);
        clay_rotation.setRepeatCount(NO_OF_CLAYS-1);
        clay_translateX.setDuration(2500);
        clay_translateY.setDuration(2500);
        clay_rotation.setDuration(3500);

        clay_translateX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                iv_clay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Toast.makeText(getApplicationContext(), "게임 종료!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                iv_clay.setVisibility(View.VISIBLE);
            }
        });

        clay_translateX.start();
        clay_translateY.start();
        clay_rotation.start();

        // step 2
        iv_gun.setClickable(true);
        // iv_gun.setFocusable(true);
        iv_gun.setOnClickListener(this);
    }
    AnimatorSet bullet = new AnimatorSet();
    // step 2
    @Override
    public void onClick(View v) {
        ObjectAnimator bullet_translateX = ObjectAnimator.ofFloat(iv_bullet, "translationX", (float)bullet_x, (float)bullet_x);
        ObjectAnimator bullet_translateY = ObjectAnimator.ofFloat(iv_bullet, "translationY", (float)(bullet_y_before), (float)(bullet_y_after), (float)(bullet_y_before));

        bullet_translateY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bullet_center_x = iv_bullet.getX() + bullet_width*0.5f;
                bullet_center_y = iv_bullet.getY() + bullet_height*0.5f;

                clay_center_x = iv_clay.getX() + clay_width*0.5f;
                clay_center_y = iv_clay.getY() + clay_height*0.5f;

                double dist = Math.sqrt(Math.pow(bullet_center_x - clay_center_x, 2) + Math.pow(bullet_center_y - clay_center_y, 2));
                if (dist <= 200) {
                    //iv_clay.setVisibility(View.INVISIBLE);
                    //bullet.end();
                    Toast.makeText(getApplicationContext(), "게임 종료!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bullet.playTogether(bullet_translateX, bullet_translateY);
        bullet.setDuration(1300);
        bullet.start();
    }
}