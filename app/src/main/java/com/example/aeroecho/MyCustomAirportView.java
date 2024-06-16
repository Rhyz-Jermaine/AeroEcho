package com.example.aeroecho;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class MyCustomAirportView extends View {
    private Paint paint;
    private Aircraft[] aircrafts;
    private String windCommand;
    private Paint textPaint;

    public MyCustomAirportView(Context context) {
        super(context);
        init();
    }

    public MyCustomAirportView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(150);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);

        // Initialize aircraft positions
        aircrafts = new Aircraft[4];
        aircrafts[0] = new Aircraft(200, 150);
        aircrafts[1] = new Aircraft(250, 100);
        aircrafts[2] = new Aircraft(300, 50);
        aircrafts[3] = new Aircraft(350, 0);

        // Generate initial wind command
        generateWindCommand();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw background
        canvas.drawColor(Color.LTGRAY);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);

        // Draw angled runway
        canvas.save();
        canvas.rotate(-45, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(300, 500, 800, 0, paint);
        canvas.restore();

        // Draw taxiways and terminals
        canvas.save();
        canvas.rotate(-45, getWidth() / 2, getHeight() / 2);
        canvas.drawRect(100, 100, 300, 300, paint);
        canvas.drawRect(600, 100, 800, 300, paint);
        canvas.restore();

        // Draw aircrafts
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        for (Aircraft aircraft : aircrafts) {
            canvas.drawCircle(aircraft.getX(), aircraft.getY(), 20, paint);
        }

        // Draw wind command
        canvas.drawText(windCommand, 50, 50, textPaint);
    }

    public void updateAircraftPosition(int index, float x, float y) {
        if (index >= 0 && index < aircrafts.length) {
            aircrafts[index].moveTo(x, y);
            invalidate();
        }
    }

    public void animateAircraftPosition(int index, float x, float y) {
        if (index >= 0 && index < aircrafts.length) {
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(aircrafts[index], "x", x);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(aircrafts[index], "y", y);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animatorX, animatorY);
            animatorSet.setDuration(1000); // Animation duration in milliseconds
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    invalidate();
                }
            });
            animatorSet.start();
        }
    }

    private void generateWindCommand() {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        int speed = new Random().nextInt(30) + 1;
        String direction = directions[new Random().nextInt(directions.length)];
        windCommand = "Wind: " + direction + " at " + speed + " knots";
    }

    public void refresh() {
        generateWindCommand();
        invalidate();
    }
}