package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup01Activity extends AppCompatActivity {
    private EditText email;
    private EditText first;
    private EditText last;
    private EditText phone;
    private EditText password;
    private EditText confirm;
    private EditText age;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup01);

        email = findViewById(R.id.emailEditText);
        first = findViewById(R.id.firstNameEditText);
        last = findViewById(R.id.lastNameEditText);
        phone = findViewById(R.id.phoneEditText);
        password = findViewById(R.id.passwordEditText);
        confirm = findViewById(R.id.confirmPasswordEditText);
        age = findViewById(R.id.ageEditText);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = db.getReference("users");

        TextView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String firstText = first.getText().toString();
                String lastText = last.getText().toString();
                String phoneText = phone.getText().toString();
                String passwordText = password.getText().toString();
                String confirmText = confirm.getText().toString();
                String ageText = age.getText().toString();

                if (emailText.isEmpty() || firstText.isEmpty() || lastText.isEmpty() ||
                        phoneText.isEmpty() || passwordText.isEmpty() || confirmText.isEmpty() || ageText.isEmpty()) {
                    Toast.makeText(Signup01Activity.this, "All fields are required", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!passwordText.equals(confirmText)) {
                    Toast.makeText(Signup01Activity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }

                int userAge = Integer.parseInt(ageText);

                usersRef.orderByChild("email").equalTo(emailText).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(Signup01Activity.this, "Email is already taken", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(Signup01Activity.this, Signup02Activity.class);
                            intent.putExtra("EMAIL", emailText);
                            intent.putExtra("FIRST_NAME", firstText);
                            intent.putExtra("LAST_NAME", lastText);
                            intent.putExtra("PHONE", phoneText);
                            intent.putExtra("PASSWORD", passwordText);
                            intent.putExtra("AGE", userAge);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Signup01Activity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}