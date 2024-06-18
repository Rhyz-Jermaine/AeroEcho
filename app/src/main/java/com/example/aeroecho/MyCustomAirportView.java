package com.example.aeroecho;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
        paint.setStrokeWidth(20); // Adjust stroke width as needed

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER); // Center text horizontally

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

        // Calculate center of the view
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        // Draw background
        canvas.drawColor(Color.LTGRAY);

        // Calculate runway coordinates relative to the center
        float startX = centerX - 400; // Adjust as needed
        float startY = centerY + 250; // Adjust as needed
        float endX = centerX + 400;   // Adjust as needed
        float endY = centerY - 250;   // Adjust as needed

        // Draw runway
        paint.setColor(Color.BLACK); // Adjust color as needed
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20); // Adjust stroke width as needed
        canvas.drawLine(startX, startY, endX, endY, paint);

        // Draw "06" sign
        drawRunwaySign(canvas, "06", startX, startY, endX, endY, true);

        // Draw "24" sign
        drawRunwaySign(canvas, "24", endX, endY, startX, startY, false);

        // Draw Bravo taxiway (parallel to runway)
        float bravoStartX = startX - 50; // Adjust leftward shift as needed
        float bravoStartY = startY - 100; // Adjust distance from runway as needed
        float bravoEndX = endX - 50;     // Adjust leftward shift as needed
        float bravoEndY = endY - 100;     // Adjust distance from runway as needed
        paint.setColor(Color.BLUE); // Adjust color as needed
        canvas.drawLine(bravoStartX, bravoStartY, bravoEndX, bravoEndY, paint);

        // Draw Hotel 1 taxiway (connecting Bravo to runway at point 06)
        float hotel1StartX = bravoStartX; // Start at Bravo
        float hotel1StartY = bravoStartY; // Same Y-coordinate as Bravo's start
        float hotel1EndX = startX;        // End at runway point 06
        float hotel1EndY = startY;        // Same Y-coordinate as runway's start
        paint.setColor(Color.GREEN);      // Adjust color as needed
        canvas.drawLine(hotel1StartX, hotel1StartY, hotel1EndX, hotel1EndY, paint);

        // Draw aircrafts
        paint.setColor(Color.RED); // Adjust color as needed
        paint.setStyle(Paint.Style.FILL);
        for (Aircraft aircraft : aircrafts) {
            canvas.drawCircle(aircraft.getX(), aircraft.getY(), 20, paint);
        }

        // Draw wind command
        canvas.drawText(windCommand, centerX, 50, textPaint);
    }

    private void drawRunwaySign(Canvas canvas, String text, float startX, float startY, float endX, float endY, boolean leftAligned) {
        // Calculate center position for the sign
        float centerX = (startX + endX) / 2;
        float centerY = (startY + endY) / 2;

        // Calculate offset for "06" or "24" text
        float offset = leftAligned ? -100 : 100;

        // Draw text
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        float textWidth = textPaint.measureText(text);
        float textHeight = bounds.height();
        canvas.drawText(text, centerX + offset, centerY + textHeight / 2, textPaint);
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
