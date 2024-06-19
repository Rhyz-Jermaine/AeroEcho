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
        // These coordinates should match the positions drawn on the canvas
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

        // Draw background
        canvas.drawColor(Color.LTGRAY);

        // Draw the aircraft
        paint.setColor(Color.RED); // Adjust color as needed
        canvas.drawCircle(aircraft.getX(), aircraft.getY(), 20, paint); // Adjust size as needed

        // Draw the rest of the airport (runways, taxiways, terminals)
        drawAirport(canvas);

        // Draw wind command
        canvas.drawText(windCommand, getWidth() / 2f, 50, textPaint);

        // Move the aircraft along the path
        moveAircraft();
    }

    private void drawAirport(Canvas canvas) {
        // Draw runways, taxiways, terminals
        for (Taxiway taxiway : taxiways.values()) {
            // Draw each taxiway based on its position
            paint.setColor(Color.BLACK); // Adjust color as needed
            canvas.drawCircle(taxiway.getX(), taxiway.getY(), 10, paint); // Example drawing, adjust size and shape

            // Example text display for each taxiway (name)
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(30);
            canvas.drawText(taxiway.getName(), taxiway.getX(), taxiway.getY() - 20, textPaint);
        }
    }

    private void setFixedWindCommand() {
        int direction = 45; // Example fixed direction in degrees (NE)
        int speed = 15; // Example fixed speed
        windCommand = "Wind: " + direction + "° at " + speed + " knots";
    }

    public void refresh() {
        invalidate();
    }

    public void processVoiceCommand(String command) {
        // Extract the taxiway sequence from the command
        String[] parts = command.toLowerCase().split(",");
        boolean validCommand = true;

        path.clear(); // Clear previous path

        for (String part : parts) {
            part = part.trim();
            if (taxiways.containsKey(part)) {
                Taxiway taxiway = taxiways.get(part);
                path.add(new float[]{taxiway.getX(), taxiway.getY()});
            } else {
                validCommand = false;
                break;
            }
        }

        if (validCommand) {
            pathIndex = 0;
            invalidate();
        } else {
            // Voice feedback for error
            speak("Invalid command. Please specify a valid taxiway sequence.");
        }
    }

    private void moveAircraft() {
        if (pathIndex < path.size()) {
            float[] target = path.get(pathIndex);
            float dx = target[0] - aircraft.getX();
            float dy = target[1] - aircraft.getY();
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance < 5) {
                aircraft.moveTo(target[0], target[1]);
                pathIndex++;
            } else {
                aircraft.moveTo(aircraft.getX() + dx / distance, aircraft.getY() + dy / distance);
            }

            handler.postDelayed(() -> invalidate(), 20); // Adjust the delay as needed
        }
    }

    private void speak(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDetachedFromWindow();
    }
}
