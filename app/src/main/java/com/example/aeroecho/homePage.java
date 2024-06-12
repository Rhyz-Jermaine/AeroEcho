// homePage.java
package com.example.aeroecho;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class homePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        // Get the Intent that started this activity and extract the username
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");

        if (username == null) {
            // If username is not passed via Intent, retrieve it from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            username = sharedPreferences.getString("USERNAME", "Guest");
        }

        // Update the TextView with the username
        TextView textUserName = findViewById(R.id.textUserName);
        textUserName.setText(username);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onMazeButtonClick(View view) {
        Intent intent = new Intent(this, mazeSimulations.class);
        startActivity(intent);
    }

    public void onTowerButtonClick(View view) {
        Intent intent = new Intent(this, towerSimulations.class);
        startActivity(intent);
    }

    public void onProfileButtonClick(View view) {
        Intent intent = new Intent(this, profile.class);
        startActivity(intent);
    }

    public void onModuleClick(View view) {
        Intent intent = new Intent(this, module.class);
        startActivity(intent);
    }

    public void onInfoButtonClick(View view) {
        Intent intent = new Intent(this, help.class);
        startActivity(intent);
    }
}
