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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {
    ImageView iv_gun;
    ImageView iv_dino;
    ImageView iv_clay;

    double screen_width, screen_height;
    float bullet_height, bullet_width;
    float gun_height, gun_width;
    float clay_height, clay_width;
    float bullet_center_x, bullet_center_y;
    float clay_center_x, clay_center_y;
    double gun_x, gun_y;
    double dino_x, dino_y_before, dino_y_after;
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

        iv_dino = new ImageView(this);
        iv_gun    = new ImageView(this);
        iv_clay   = new ImageView(this);

        iv_gun.setImageResource(R.drawable.gun);
        iv_gun.measure(iv_gun.getMeasuredWidth(), iv_gun.getMeasuredHeight());
        gun_height = iv_gun.getMeasuredHeight();
        gun_width = iv_gun.getMeasuredWidth();
        layout.addView(iv_gun);

        iv_dino.setImageResource(R.drawable.dino1);
        iv_dino.measure(iv_dino.getMeasuredWidth(), iv_dino.getMeasuredHeight());
        bullet_height = iv_dino.getMeasuredHeight();
        bullet_width  = iv_dino.getMeasuredWidth();
        //iv_dino.setVisibility(View.INVISIBLE);
        layout.addView(iv_dino);

        iv_clay.setImageResource(R.drawable.clay);
        iv_clay.setScaleX(0.8f);
        iv_clay.setScaleY(0.8f);
        iv_clay.measure(iv_dino.getMeasuredWidth(), iv_dino.getMeasuredHeight());
        clay_height = iv_clay.getMeasuredHeight();
        clay_width  = iv_clay.getMeasuredWidth();
        iv_clay.setVisibility(View.INVISIBLE);   // step 2
        layout.addView(iv_clay);

        gun_center_x = screen_width * 0.1;
        gun_x = gun_center_x - gun_width * 0.5;
        gun_y = screen_height - gun_height;
        iv_gun.setX((float)gun_x);
        iv_gun.setY((float)gun_y - 100f);


        dino_x = screen_width * 0.1;
        dino_y_before = screen_height * 0.55;
        dino_y_after = screen_height * 0.1;
        iv_dino.setX((float)dino_x);
        iv_dino.setY((float)(dino_y_before));

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
        //iv_gun.setOnClickListener(this);
        //implements View.OnClickListener
    }
    AnimatorSet bullet = new AnimatorSet();
    // step 2
//    @Override
//    public void onClick(View v) {
//        ObjectAnimator bullet_translateX = ObjectAnimator.ofFloat(iv_dino, "translationX", (float)dino_x, (float)dino_x);
//        ObjectAnimator bullet_translateY = ObjectAnimator.ofFloat(iv_dino, "translationY", (float)(dino_y_before), (float)(dino_y_after), (float)(dino_y_before));
//
//        bullet_translateY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                bullet_center_x = iv_dino.getX() + bullet_width*0.5f;
//                bullet_center_y = iv_dino.getY() + bullet_height*0.5f;
//
//                clay_center_x = iv_clay.getX() + clay_width*0.5f;
//                clay_center_y = iv_clay.getY() + clay_height*0.5f;
//
//                double dist = Math.sqrt(Math.pow(bullet_center_x - clay_center_x, 2) + Math.pow(bullet_center_y - clay_center_y, 2));
//                if (dist <= 200) {
//                    //iv_clay.setVisibility(View.INVISIBLE);
//                    //bullet.end();
//                    Toast.makeText(getApplicationContext(), "게임 종료!!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        bullet.playTogether(bullet_translateX, bullet_translateY);
//        bullet.setDuration(1300);
//        bullet.start();
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int userAction = event.getAction();
        switch (userAction) {
            case MotionEvent.ACTION_DOWN:
                ObjectAnimator bullet_translateX = ObjectAnimator.ofFloat(iv_dino, "translationX", (float)dino_x, (float)dino_x);
                ObjectAnimator bullet_translateY = ObjectAnimator.ofFloat(iv_dino, "translationY", (float)(dino_y_before), (float)(dino_y_after), (float)(dino_y_before));

                bullet_translateY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        bullet_center_x = iv_dino.getX() + bullet_width*0.5f;
                        bullet_center_y = iv_dino.getY() + bullet_height*0.5f;

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

            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}