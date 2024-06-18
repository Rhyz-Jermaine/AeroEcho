package com.example.aeroecho;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class createstudentacc extends AppCompatActivity {

    private EditText createStudUser, createEmail, createStudNum, createPassword, createRePassword;
    private Spinner createSectionSpinner;
    private Button createAccountButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String selectedSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_createstudentacc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("students");

        createStudUser = findViewById(R.id.createStudUser);
        createEmail = findViewById(R.id.createEmail);
        createStudNum = findViewById(R.id.createStudNum);
        createSectionSpinner = findViewById(R.id.createSectionSpinner);
        createPassword = findViewById(R.id.createPassword);
        createRePassword = findViewById(R.id.createRePassword);
        createAccountButton = findViewById(R.id.registerStudButton);

        setupSpinner();

        createAccountButton.setOnClickListener(v -> createAccount());
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sections_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        createSectionSpinner.setAdapter(adapter);

        createSectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedSection = parent.getItemAtPosition(position).toString();
                } else {
                    selectedSection = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set the hint
        createSectionSpinner.setAdapter(
                new ArrayAdapter<String>(this, R.layout.spinner_hint, new String[]{"Section"}) {
                    @Override
                    public boolean isEnabled(int position) {
                        return position != 0;
                    }
                }
        );
    }

    private void createAccount() {
        String username = createStudUser.getText().toString().trim();
        String email = createEmail.getText().toString().trim();
        String studentNum = createStudNum.getText().toString().trim();
        String password = createPassword.getText().toString().trim();
        String rePassword = createRePassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(studentNum) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(rePassword) || TextUtils.isEmpty(selectedSection)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(rePassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            writeNewStudent(user.getUid(), username, email, studentNum, selectedSection);
                        }
                        Toast.makeText(createstudentacc.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(createstudentacc.this, loginstudent.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(createstudentacc.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void writeNewStudent(String userId, String username, String email, String studentNum, String section) {
        Student student = new Student(username, email, studentNum, section);
        mDatabase.child(section).child(userId).setValue(student);
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
