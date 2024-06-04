package com.example.aeroecho;

//necessary
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//other widgets
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//para sa firebase
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.app.AlertDialog;

public class loginstudent extends AppCompatActivity {

    private EditText loginstudUsername, loginstudpass;
    private Button loginstudbutton;
    private TextView forgotpass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginstudent);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_loginstudent);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("students");

        loginstudUsername = findViewById(R.id.loginstudUsername);
        loginstudpass = findViewById(R.id.loginstudpass);
        loginstudbutton = findViewById(R.id.loginstudbutton);

        loginstudbutton.setOnClickListener(v -> loginStudent());

    }

    public void onSigninButtonClick(View view) {
        Intent intent = new Intent(this, createstudentacc.class);
        startActivity(intent);
    }

    public void onForgotPassButtonClick(View view) {
        Intent intent = new Intent(this, forgotpass.class);
        startActivity(intent);
    }

    private void loginStudent() {
        String usernameOrNumber = loginstudUsername.getText().toString().trim();
        String password = loginstudpass.getText().toString().trim();

        if (TextUtils.isEmpty(usernameOrNumber) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean foundUser = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    if (student != null && (usernameOrNumber.equals(student.username) || usernameOrNumber.equals(student.studentNum))) {
                        foundUser = true;
                        String email = student.email;

                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(loginstudent.this, task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(loginstudent.this, "Login successful", Toast.LENGTH_SHORT).show();
                                        // Proceed to the next activity or home screen
                                        Intent intent = new Intent(loginstudent.this, homePage.class);
                                        startActivity(intent);
                                        finish(); //close the current activity para kahit ma back ng user hindi bumalik don unless mag logout
                                    }else {
                                        Toast.makeText(loginstudent.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        break;
                    }
                }

                if (!foundUser) {
                    Toast.makeText(loginstudent.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(loginstudent.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static class Student {
        public String username;
        public String email;
        public String studentNum;
        public String section;

        public Student() {
            // Default constructor required for calls to DataSnapshot.getValue(Student.class)
        }

        public Student(String username, String email, String studentNum, String section) {
            this.username = username;
            this.email = email;
            this.studentNum = studentNum;
            this.section = section;
        }
    }



}