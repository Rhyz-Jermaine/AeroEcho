package com.example.aeroecho;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class loginadmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginadmin);

        // Find the views by their ID
        EditText usernameEditText = findViewById(R.id.loginAdminUsername);
        EditText passwordEditText = findViewById(R.id.loginAdminPass);
        Button loginButton = findViewById(R.id.loginstudbutton);

        // Set the default admin credentials
        usernameEditText.setText("ADMIN");
        passwordEditText.setText("@dmin123");

        // Set a click listener on the login button
        loginButton.setOnClickListener(v -> {
            // Get the entered username and password
            String enteredUsername = usernameEditText.getText().toString();
            String enteredPassword = passwordEditText.getText().toString();

            // Check if the entered credentials match the admin credentials
            if (TextUtils.equals(enteredUsername, "ADMIN") && TextUtils.equals(enteredPassword, "@dmin123")) {
                // Login successful
                Toast.makeText(loginadmin.this, "Login successful", Toast.LENGTH_SHORT).show();
                // Proceed to the next activity or perform any other necessary actions
            } else {
                // Login failed
                Toast.makeText(loginadmin.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}