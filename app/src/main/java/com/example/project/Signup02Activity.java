package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class Signup02Activity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup02);

        Spinner bloodSpinner = findViewById(R.id.bloodTypeSpinner);
        Spinner roleSpinner = findViewById(R.id.roleSpinner);
        Spinner sexSpinner = findViewById(R.id.sexSpinner);

        List<String> bloodTypes = Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodTypes);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodSpinner.setAdapter(bloodAdapter);

        List<String> sexes = Arrays.asList("Male", "Female");
        ArrayAdapter<String> sexAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sexes);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(sexAdapter);

        List<String> roles = Arrays.asList("Patient");
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        Intent intent = getIntent();

        String email = intent.getStringExtra("EMAIL");
        String first = intent.getStringExtra("FIRST_NAME");
        String last = intent.getStringExtra("LAST_NAME");
        String phone = intent.getStringExtra("PHONE");
        String password = intent.getStringExtra("PASSWORD");
        int age = intent.getIntExtra("AGE", 0);

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
                String bloodText = bloodSpinner.getSelectedItem().toString();
                String sexText = sexSpinner.getSelectedItem().toString();
                String roleText = roleSpinner.getSelectedItem().toString();

                if (bloodText.isEmpty() || sexText.isEmpty() || roleText.isEmpty()) {
                    Toast.makeText(Signup02Activity.this, "All fields are required", Toast.LENGTH_LONG).show();
                    return;
                }

                addNewUser(email, first, last, password, phone, bloodText, sexText, roleText, age);
            }
        });
    }

    public void addNewUser(String email, String first, String last, String pass, String phone, String blood, String sex, String role, int age){
        User user = new User(email, first, last, pass, phone, blood, sex, role, age);

        String emailPrefix = email.split("@")[0];

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("users");
        //String userId = ref.push().getKey();

        ref.child(emailPrefix).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Signup02Activity.this, "User Added Successfully.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Signup02Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
