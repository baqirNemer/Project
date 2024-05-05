package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class EditProfile extends AppCompatActivity {
    public static final String ACTION_PROFILE_UPDATED = "com.example.project.ACTION_PROFILE_UPDATED";
    EditText first, last, age, phone;
    Spinner bloodTypeSpinner, sexSpinner;
    private static String userID;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        List<String> bloodTypes = Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodTypes);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodTypeSpinner = findViewById(R.id.bloodTypeSpinner);
        bloodTypeSpinner.setAdapter(bloodAdapter);

        List<String> sexes = Arrays.asList("Male", "Female");
        ArrayAdapter<String> sexAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sexes);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner = findViewById(R.id.sexSpinner);
        sexSpinner.setAdapter(sexAdapter);

        first = findViewById(R.id.firstNameEditText);
        last = findViewById(R.id.lastNameEditText);
        age = findViewById(R.id.ageEditText);
        phone = findViewById(R.id.phoneEditText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("ID", "");
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userFirstName = snapshot.child("firstName").getValue(String.class);
                        String userLastName = snapshot.child("lastName").getValue(String.class);
                        Integer userAge = snapshot.child("age").getValue(Integer.class);
                        String userPhoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                        String userBloodType = snapshot.child("bloodType").getValue(String.class);
                        String userSex = snapshot.child("sex").getValue(String.class);

                        first.setText(userFirstName);
                        last.setText(userLastName);
                        if (userAge != null) {
                            age.setText(String.valueOf(userAge));
                        }
                        phone.setText(userPhoneNumber);

                        bloodTypeSpinner.setSelection(bloodAdapter.getPosition(userBloodType));
                        sexSpinner.setSelection(sexAdapter.getPosition(userSex));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditProfile.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        TextView saveButton = findViewById(R.id.Save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();

                Intent intent = new Intent(EditProfile.this, ProfileUpdateReceiver.class);
                intent.setAction(ProfileUpdateReceiver.ACTION_PROFILE_UPDATED);
                sendBroadcast(intent);

                finish();
            }
        });
    }

    private void updateUserData() {
        String newFirstName = first.getText().toString().trim();
        String newLastName = last.getText().toString().trim();
        Integer newAge = Integer.parseInt(age.getText().toString().trim());
        String newPhoneNumber = phone.getText().toString().trim();
        String newBloodType = bloodTypeSpinner.getSelectedItem().toString();
        String newSex = sexSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(newFirstName) || TextUtils.isEmpty(newLastName) || TextUtils.isEmpty(newPhoneNumber)
                || TextUtils.isEmpty(newBloodType) || TextUtils.isEmpty(newSex)) {
            Toast.makeText(EditProfile.this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        userRef.child("firstName").setValue(newFirstName);
        userRef.child("lastName").setValue(newLastName);
        userRef.child("age").setValue(newAge);
        userRef.child("phoneNumber").setValue(newPhoneNumber);
        userRef.child("bloodType").setValue(newBloodType);
        userRef.child("sex").setValue(newSex);

        Toast.makeText(EditProfile.this, "User data updated successfully", Toast.LENGTH_LONG).show();
        finish();
    }
}
