package com.example.dognutrition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Admin_Dashboard_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button manageUsersButton, manageOrdersButton, manageProductButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userEmail = user.getEmail();

            // Ensure only the admin can access this activity
            if ("admin@example.com".equals(userEmail)) { // Replace with your actual admin email
                // Initialize UI components
                manageUsersButton = findViewById(R.id.manageUsersButton);
                manageOrdersButton = findViewById(R.id.manageOrdersButton);
                manageProductButton = findViewById(R.id.manageProductButton);
                logoutButton = findViewById(R.id.logoutButton); // Initialize the logout button

                // Set up button click listeners
                manageUsersButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle manage users action
                        Toast.makeText(Admin_Dashboard_Activity.this, "Manage Users", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(AdminDashboardActivity.this, ManageUsersActivity.class));
                    }
                });

                manageOrdersButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle manage orders action
                        Toast.makeText(Admin_Dashboard_Activity.this, "Manage Orders", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(AdminDashboardActivity.this, ManageOrdersActivity.class));
                    }
                });

                manageProductButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle manage product action
                        Toast.makeText(Admin_Dashboard_Activity.this, "Manage Product", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(AdminDashboardActivity.this, ManageProductsActivity.class));
                    }
                });

                logoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Sign out the user and redirect to the login activity
                        mAuth.signOut();
                        Toast.makeText(Admin_Dashboard_Activity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Admin_Dashboard_Activity.this, Login_Activity.class));
                        finish();
                    }
                });

            } else {
                // If not admin, redirect to main activity
                Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Admin_Dashboard_Activity.this, Main_Activity.class));
                finish();
            }
        } else {
            // If no user is logged in, redirect to login
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Admin_Dashboard_Activity.this, Login_Activity.class));
            finish();
        }
    }
}
