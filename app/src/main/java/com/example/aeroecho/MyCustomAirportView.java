package com.example.aeroecho;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyCustomAirportView extends View {
    private Paint paint;
    private String windCommand;
    private Paint textPaint;
    private Aircraft aircraft;
    private Map<String, Taxiway> taxiways;
    private Handler handler;
    private List<float[]> path;
    private int pathIndex;
    private TextToSpeech textToSpeech;
    private Context context;

    public MyCustomAirportView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MyCustomAirportView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
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

        aircraft = new Aircraft(100, 100); // Initial position of the aircraft

        taxiways = new HashMap<>();
        handler = new Handler();
        path = new ArrayList<>();
        pathIndex = 0;

        defineTaxiways();

        // Set a fixed wind command
        setFixedWindCommand();

        // Initialize text-to-speech
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
            }
        });
    }

    private void defineTaxiways() {
        // Positions
        taxiways.put("terminal 1", new Taxiway("terminal 1", 250, 150));
        taxiways.put("terminal 2", new Taxiway("terminal 2", 550, 0));
        taxiways.put("charlie 1", new Taxiway("charlie 1", 350, 300));
        taxiways.put("charlie 2", new Taxiway("charlie 2", 750, 300));
        taxiways.put("bravo", new Taxiway("bravo", 400, 300));
        taxiways.put("hotel 1", new Taxiway("hotel 1", 400, 500));
        taxiways.put("echo", new Taxiway("echo", 600, 500));
        taxiways.put("hotel 2", new Taxiway("hotel 2", 800, 500));
        taxiways.put("runway_06", new Taxiway("runway_06", 200, 650));
        taxiways.put("runway_24", new Taxiway("runway_24", 600, 450));
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
        float charlie2EndX = charlie2StartX; // Vertical line directly below Terminal 2
        float charlie2EndY = charlie1EndY; // Top of Bravo
        paint.setColor(Color.GRAY); // Adjust color as needed
        canvas.drawLine(charlie2StartX, charlie2StartY, charlie2EndX, charlie2EndY, paint);
        // Draw the aircraft
        paint.setColor(Color.RED);
        canvas.drawCircle(aircraft.getX(), aircraft.getY(), 20, paint);

        // Draw wind information
        if (windCommand != null) {
            textPaint.setColor(Color.BLACK);
            canvas.drawText(windCommand, centerX, centerY, textPaint);
        }

        // Move the aircraft along the path if defined
        if (path != null && !path.isEmpty() && pathIndex < path.size()) {
            aircraft.move(path.get(pathIndex)[0], path.get(pathIndex)[1]);
            pathIndex++;
            handler.postDelayed(this::invalidate, 1000); // Adjust speed as needed
        }
    }


    private void drawRunwaySign(Canvas canvas, String text, float startX, float startY, float endX, float endY, boolean isStart) {
        float signX = (startX + endX) / 2;
        float signY = (startY + endY) / 2;

        // Adjust for text position relative to the runway
        if (isStart) {
            signX -= 200; // Adjust as needed
            signY += 50;  // Adjust as needed
        } else {
            signX += 200; // Adjust as needed
            signY -= 50;  // Adjust as needed
        }

        textPaint.setColor(Color.WHITE); // Adjust color as needed
        textPaint.setTextSize(80);       // Adjust text size as needed
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Draw the text
        canvas.drawText(text, signX, signY, textPaint);
    }

    public void setFixedWindCommand() {
        // Set a fixed wind command for demonstration purposes
        windCommand = "Wind 270 at 15 knots";
        invalidate();
    }

    public void startMovement() {
        path = new ArrayList<>();
        // Define path points for the aircraft to follow
        path.add(new float[]{250, 150}); // Move to Terminal 1
        path.add(new float[]{350, 300}); // Move to Charlie 1
        path.add(new float[]{400, 300}); // Move to Bravo
        path.add(new float[]{400, 500}); // Move to Hotel 1
        path.add(new float[]{600, 500}); // Move to Echo
        path.add(new float[]{800, 500}); // Move to Hotel 2
        path.add(new float[]{600, 450}); // Move to Runway 24
        path.add(new float[]{200, 650}); // Move to Runway 06
        path.add(new float[]{750, 300}); // Move to Charlie 2
        path.add(new float[]{550, 0});   // Move to Terminal 2

        pathIndex = 0;
        handler.postDelayed(this::invalidate, 1000);
    }

    public void speakCommand(String command) {
        if (textToSpeech != null) {
            textToSpeech.speak(command, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void shutdownTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    // Aircraft class
    private class Aircraft {
        private float x;
        private float y;

        public Aircraft(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public void move(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    // Taxiway class
    private class Taxiway {
        private String name;
        private float x;
        private float y;

        public Taxiway(String name, float x, float y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        public String getName() {
            return name;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    // Add the refresh method
    public void refresh() {
        invalidate();
    }
}
