package com.example.aeroecho;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AirportView extends View {
    private Paint paint;

    public AirportView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xFF0000FF);  // Blue color for lines
        paint.setStrokeWidth(10);    // Line thickness
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d("AirportView", "onDraw called");

        // Draw runways
        drawRunways(canvas);

        // Draw taxiways
        drawTaxiways(canvas);

        // Draw terminals
        drawTerminals(canvas);
    }

    private void drawRunways(Canvas canvas) {
        // Example: Draw Runway 06/24
        canvas.drawLine(200, 1000, 1800, 200, paint);  // Adjust coordinates as needed
    }

    private void drawTaxiways(Canvas canvas) {
        // Example: Draw Taxiway Bravo (parallel to the runway)
        canvas.drawLine(250, 200, 1850, 800, paint);

        // Draw Taxiway connections to the runway
        canvas.drawLine(200, 1000, 250, 800, paint);   // Hotel 1
        canvas.drawLine(1800, 200, 1850, 400, paint);   // Hotel 2
        canvas.drawLine(1000, 600, 1050, 800, paint);   // Echo
    }

    private void drawTerminals(Canvas canvas) {
        // Example: Draw Terminal connections
        canvas.drawLine(250, 300, 350, 300, paint);   // Charlie 1
        canvas.drawLine(250, 500, 350, 500, paint);   // Charlie 2
        canvas.drawLine(250, 700, 350, 700, paint);   // Charlie 3

        // Draw Terminal buildings
        paint.setStrokeWidth(20);
        canvas.drawLine(350, 250, 350, 350, paint);   // Terminal 1
        canvas.drawLine(350, 450, 350, 550, paint);   // Terminal 2
        canvas.drawLine(350, 650, 350, 750, paint);   // Terminal 3
    }
}
