package com.example.dognutrition;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_Fragment extends Fragment {

    private TextView nameTextView, addressTextView, paymentMethodTextView, ageTextView;
    private Button updateDetailsButton, logoutButton;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTextView = view.findViewById(R.id.nameTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        paymentMethodTextView = view.findViewById(R.id.paymentMethodTextView);
        ageTextView = view.findViewById(R.id.ageTextView);
        updateDetailsButton = view.findViewById(R.id.updateDetailsButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Initialize Firebase Database reference
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Load user data
        loadUserData();

        // Set up update button click listener
        updateDetailsButton.setOnClickListener(v -> showUpdateDialog());

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent logoutIntent = new Intent(getActivity(), Login_Activity.class);
            startActivity(logoutIntent);
            getActivity().finish();
        });

        return view;
    }

    private void loadUserData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String paymentMethod = dataSnapshot.child("paymentMethod").getValue(String.class);
                    Integer age = dataSnapshot.child("age").getValue(Integer.class);

                    // Set text only if the values are not null
                    nameTextView.setText(name != null ? name : "N/A");
                    addressTextView.setText(address != null ? address : "N/A");
                    paymentMethodTextView.setText(paymentMethod != null ? paymentMethod : "N/A");
                    ageTextView.setText(age != null ? age.toString() : "N/A");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                showToast("Failed to load user data");
            }
        });
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Details");

        View view = getLayoutInflater().inflate(R.layout.dialog_update_details, null);
        final EditText nameEditText = view.findViewById(R.id.dialogNameEditText);
        final EditText addressEditText = view.findViewById(R.id.dialogAddressEditText);
        final Spinner paymentMethodSpinner = view.findViewById(R.id.dialogPaymentMethodSpinner);
        final EditText ageEditText = view.findViewById(R.id.dialogAgeEditText);
        builder.setView(view);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String name = nameEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String paymentMethod = paymentMethodSpinner.getSelectedItem().toString();
            String ageString = ageEditText.getText().toString().trim();

            // Check if age is valid and not empty
            Integer ageVal = null;
            if (!TextUtils.isEmpty(ageString)) {
                try {
                    ageVal = Integer.parseInt(ageString);
                    if (ageVal < 0 || ageVal > 110) {
                        showToast("Invalid age. Enter a valid age!");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showToast("Invalid age format. Enter a numeric value.");
                    return;
                }
            }

            // Validate name and address
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
                showToast("Name and Address cannot be empty");
                return;
            }

            // Update user data
            updateUserData(name, address, paymentMethod, ageVal);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void updateUserData(String name, String address, String paymentMethod, Integer age) {
        databaseReference.child("name").setValue(name).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                databaseReference.child("address").setValue(address).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        databaseReference.child("paymentMethod").setValue(paymentMethod).addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                // Add age update here if age is not null
                                if (age != null) {
                                    databaseReference.child("age").setValue(age).addOnCompleteListener(task3 -> {
                                        if (task3.isSuccessful()) {
                                            showToast("User details updated successfully");
                                        } else {
                                            showToast("Failed to update age");
                                        }
                                    });
                                } else {
                                    showToast("User details updated successfully");
                                }
                            } else {
                                showToast("Failed to update payment method");
                            }
                        });
                    } else {
                        showToast("Failed to update address");
                    }
                });
            } else {
                showToast("Failed to update name");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
