package com.example.aeroecho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class mazeSimulations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_maze_simulations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onProfileButtonClick(View view) {

        Intent intent = new Intent(this, profile.class);

        startActivity(intent);
    }

    public void onHomeButtonClick(View view) {

        Intent intent = new Intent(this, homePage.class);

        startActivity(intent);
    }

    public void onInfoButtonClick(View view) {

        Intent intent = new Intent(this, help.class);

        startActivity(intent);
    }

}