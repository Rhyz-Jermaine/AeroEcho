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
        paint.setStrokeWidth(30); // Adjust stroke width as needed

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
        float bravoStartX = startX; // Adjust leftward shift as needed
        float bravoStartY = startY - 110; // Adjust distance from runway as needed
        float bravoEndX = endX;     // Adjust leftward shift as needed
        float bravoEndY = endY - 110;     // Adjust distance from runway as needed
        paint.setColor(Color.BLUE); // Adjust color as needed
        canvas.drawLine(bravoStartX, bravoStartY, bravoEndX, bravoEndY, paint);

        // Draw Hotel 1 taxiway (connecting Bravo to runway at point 06)
        float hotel1StartX = bravoStartX; // Start at Bravo
        float hotel1StartY = bravoStartY; // Same Y-coordinate as Bravo's start
        float hotel1EndX = startX;        // End at runway point 06
        float hotel1EndY = startY;        // Same Y-coordinate as runway's start
        paint.setColor(Color.GREEN);      // Adjust color as needed
        canvas.drawLine(hotel1StartX, hotel1StartY, hotel1EndX, hotel1EndY, paint);

        // Draw Echo taxiway (connecting Bravo to runway, slightly farther from Hotel 1)
        float echoStartX = bravoStartX + 200; // Start at Bravo
        float echoStartY = hotel1StartY - 130; // Adjust Y-coordinate to be slightly farther from Hotel 1
        float echoEndX = startX + 200;        // End at runway
        float echoEndY = hotel1EndY - 130;  // Adjust Y-coordinate to be slightly farther from Hotel 1
        paint.setColor(Color.MAGENTA); // Adjust color as needed
        canvas.drawLine(echoStartX, echoStartY, echoEndX, echoEndY, paint);

        // Draw Hotel 2 taxiway (connecting Bravo to runway at point 24)
        float hotel2StartX = bravoEndX; // Start at Bravo's end towards Runway 24
        float hotel2StartY = bravoEndY; // Same Y-coordinate as Bravo's end
        float hotel2EndX = endX;        // End at runway point 24
        float hotel2EndY = endY;        // Same Y-coordinate as runway's end
        paint.setColor(Color.CYAN);    // Adjust color as needed
        canvas.drawLine(hotel2StartX, hotel2StartY, hotel2EndX, hotel2EndY, paint);

        // Draw Terminal 1 (parallel to Bravo, above the runway)
        float terminal1X = bravoStartX + 50; // Adjust X-coordinate to the right of Bravo
        float terminal1Y = bravoStartY - 350; // Move Terminal 1 further up
        float terminal1Width = 200; // Terminal 1 width
        float terminal1Height = 100; // Terminal 1 height
        paint.setColor(Color.RED); // Adjust color as needed
        canvas.drawRect(terminal1X, terminal1Y, terminal1X + terminal1Width, terminal1Y + terminal1Height, paint);

// Draw Charlie 1 taxiway (connecting lower part of Terminal 1 to Bravo)
        float charlie1StartX = terminal1X + terminal1Width / 2; // Center of Terminal 1
        float charlie1StartY = terminal1Y + terminal1Height; // Lower part of Terminal 1
        float charlie1EndX = charlie1StartX; // Vertical line directly above Terminal 1
        float charlie1EndY = bravoStartY - 110; // Top of Bravo
        paint.setColor(Color.DKGRAY); // Adjust color as needed
        canvas.drawLine(charlie1StartX, charlie1StartY, charlie1EndX, charlie1EndY, paint);

// Draw Terminal 2 (parallel to Bravo, above the runway)
        float terminal2X = terminal1X + terminal1Width + 50; // Right next to Terminal 1
        float terminal2Y = terminal1Y - 150; // Same Y-coordinate as Terminal 1
        float terminal2Width = 200; // Terminal 2 width
        float terminal2Height = 100; // Terminal 2 height
        paint.setColor(Color.YELLOW); // Adjust color as needed
        canvas.drawRect(terminal2X, terminal2Y, terminal2X + terminal2Width, terminal2Y + terminal2Height, paint);

// Draw Charlie 2 taxiway (connecting lower part of Terminal 2 to Bravo)
        float charlie2StartX = terminal2X + terminal2Width / 2; // Center of Terminal 2 horizontally
        float charlie2StartY = terminal2Y + terminal2Height; // Lower part of Terminal 2

// Calculate the endpoint on Bravo
        float charlie2EndX = charlie2StartX; // Vertical line directly above Terminal 2
        float charlie2EndY = bravoEndY; // Y-coordinate where you want it to connect on Bravo

// Optionally adjust charlie2EndY to place the endpoint lower or higher as needed
// charlie2EndY = bravoEndY - 50; // Example adjustment to lower the endpoint

        paint.setColor(Color.DKGRAY); // Adjust color as needed
        canvas.drawLine(charlie2StartX, charlie2StartY, charlie2EndX, charlie2EndY, paint);

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
