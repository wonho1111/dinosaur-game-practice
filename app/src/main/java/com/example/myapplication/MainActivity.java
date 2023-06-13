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
    ImageView iv_dino;
    ImageView iv_ptero;

    double screen_width, screen_height;
    float dino_height, dino_width;
    float ptero_height, ptero_width;
    float dino_center_x, dino_center_y;
    float ptero_center_x, ptero_center_y;
    double dino_x, dino_y_before, dino_y_after;

    final int NO_OF_PTERO = 5;

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
        iv_ptero   = new ImageView(this);

        iv_dino.setImageResource(R.drawable.dino1);
        iv_dino.measure(iv_dino.getMeasuredWidth(), iv_dino.getMeasuredHeight());
        dino_height = iv_dino.getMeasuredHeight();
        dino_width  = iv_dino.getMeasuredWidth();
        layout.addView(iv_dino);

        iv_ptero.setImageResource(R.drawable.ptero);
        iv_ptero.setScaleX(0.8f);
        iv_ptero.setScaleY(0.8f);
        iv_ptero.measure(iv_dino.getMeasuredWidth(), iv_dino.getMeasuredHeight());
        ptero_height = iv_ptero.getMeasuredHeight();
        ptero_width  = iv_ptero.getMeasuredWidth();
        iv_ptero.setVisibility(View.INVISIBLE);   // step 2
        layout.addView(iv_ptero);

        dino_x = screen_width * 0.1;
        dino_y_before = screen_height * 0.55;
        dino_y_after = screen_height * 0.1;
        iv_dino.setX((float)dino_x);
        iv_dino.setY((float)(dino_y_before));

        ObjectAnimator ptero_translateX = ObjectAnimator.ofFloat(iv_ptero, "translationX", (float)screen_width + 100f, -200f);
        ObjectAnimator ptero_translateY = ObjectAnimator.ofFloat(iv_ptero, "translationY", 650f, 650f);
        ptero_translateX.setRepeatCount(NO_OF_PTERO-1);
        ptero_translateY.setRepeatCount(NO_OF_PTERO-1);
        ptero_translateX.setDuration(2500);
        ptero_translateY.setDuration(2500);

        ptero_translateX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                iv_ptero.setVisibility(View.VISIBLE);
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
                iv_ptero.setVisibility(View.VISIBLE);
            }
        });

        ptero_translateX.start();
        ptero_translateY.start();
        //ptero_rotation.start();

    }
    AnimatorSet dino = new AnimatorSet();

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int userAction = event.getAction();
        switch (userAction) {
            case MotionEvent.ACTION_DOWN:
                ObjectAnimator dino_translateX = ObjectAnimator.ofFloat(iv_dino, "translationX", (float)dino_x, (float)dino_x);
                ObjectAnimator dino_translateY = ObjectAnimator.ofFloat(iv_dino, "translationY", (float)(dino_y_before), (float)(dino_y_after), (float)(dino_y_before));

                dino_translateY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        dino_center_x = iv_dino.getX() + dino_width*0.5f;
                        dino_center_y = iv_dino.getY() + dino_height*0.5f;

                        ptero_center_x = iv_ptero.getX() + ptero_width*0.5f;
                        ptero_center_y = iv_ptero.getY() + ptero_height*0.5f;

                        double dist = Math.sqrt(Math.pow(dino_center_x - ptero_center_x, 2) + Math.pow(dino_center_y - ptero_center_y, 2));
                        if (dist <= 200) {
                            iv_ptero.setVisibility(View.INVISIBLE);

                            Toast.makeText(getApplicationContext(), "게임 종료!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dino.playTogether(dino_translateX, dino_translateY);
                dino.setDuration(1300);
                dino.start();

            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}