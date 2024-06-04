package com.example.aeroecho;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPassword extends AppCompatActivity {

    private EditText newPassword, confirmPassword;
    private Button resetButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("students");

        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        resetButton = findViewById(R.id.resetButton);

        // Get the email from the intent
        email = getIntent().getStringExtra("email");

        resetButton.setOnClickListener(v -> resetPassword());

    }

    private void resetPassword() {
        String newPasswordText = newPassword.getText().toString().trim();
        String confirmPasswordText = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(newPasswordText) || TextUtils.isEmpty(confirmPasswordText)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPasswordText.equals(confirmPasswordText)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reset the password in Firebase Auth
        mAuth.getCurrentUser().updatePassword(newPasswordText).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Update the password in the database
                mDatabase.child(mAuth.getCurrentUser().getUid()).child("password").setValue(newPasswordText);
                Toast.makeText(ResetPassword.this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResetPassword.this, loginstudent.class));
                finish();
            } else {
                Toast.makeText(ResetPassword.this, "Password reset failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}