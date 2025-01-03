package com.example.dognutrition;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Activity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, nameEditText, addressEditText, ageEditText;
    private Button registerButton, loginButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        ageEditText = findViewById(R.id.ageEditText);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        registerButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String ageString = ageEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email is required.");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Password is required.");
                return;
            }

            if (TextUtils.isEmpty(name)) {
                nameEditText.setError("Name is required.");
                return;
            }

            if (TextUtils.isEmpty(address)) {
                addressEditText.setError("Address is required.");
                return;
            }

            if (TextUtils.isEmpty(ageString)) {
                ageEditText.setError("Age is required.");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageString);
                if (age < 0 || age > 110) {
                    ageEditText.setError("Enter a valid age.");
                    return;
                }
            } catch (NumberFormatException e) {
                ageEditText.setError("Age must be a number.");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid();
                                mDatabase.child(userId).setValue(new Profile(email, name, address, "Cash On Delivery", age));
                                Toast.makeText(Register_Activity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                // Redirect to login or main activity
                                startActivity(new Intent(Register_Activity.this, Login_Activity.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(Register_Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println(task.getException().getMessage());
                        }
                    });
        });

        loginButton.setOnClickListener(view -> {
            startActivity(new Intent(Register_Activity.this, Login_Activity.class));
        });
    }
}
