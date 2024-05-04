package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    private DatabaseReference usersRef;
    String TAG;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_PASSWORD = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        String savedEmail = sharedPreferences.getString(KEY_EMAIL, "");
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");

        email.setText(savedEmail);
        password.setText(savedPassword);

        TAG = "Firebase";

        TextView buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if (emailText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
                    return;
                }

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                usersRef = db.getReference("users");

                usersRef.orderByChild("email").equalTo(emailText).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                if (user != null && user.getPassword().equals(passwordText)) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_EMAIL, emailText);
                                    editor.putString(KEY_PASSWORD, passwordText);
                                    editor.apply();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    String userID = emailText.split("@")[0];
                                    intent.putExtra("USERID", userID);
                                    startActivity(intent);
                                    return;
                                }
                            }
                        }
                        Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "Database error: " + databaseError.getMessage());
                        Toast.makeText(LoginActivity.this, "Database error.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        TextView textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        TextView signupButton = findViewById(R.id.buttonSignUp);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Signup01Activity.class);
                startActivity(intent);
            }
        });

    }
}
