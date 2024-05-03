package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup02Activity extends AppCompatActivity {
    private EditText blood;
    private EditText role;
    private EditText sex;
    private DatabaseReference ref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup02);

        blood = findViewById(R.id.bloodTypeEditText);
        sex = findViewById(R.id.sexEditText);
        role  = findViewById(R.id.roleEditText);

        Intent intent = getIntent();

        String email = intent.getStringExtra("EMAIL");
        String first = intent.getStringExtra("FIRST_NAME");
        String last = intent.getStringExtra("LAST_NAME");
        String phone = intent.getStringExtra("PHONE");
        String password = intent.getStringExtra("PASSWORD");

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
                String bloodText = blood.getText().toString();
                String sexText = sex.getText().toString();
                String roleText = role.getText().toString();

                if (bloodText.isEmpty() || sexText.isEmpty() || roleText.isEmpty()) {
                    Toast.makeText(Signup02Activity.this, "All fields are required", Toast.LENGTH_LONG).show();
                    return;
                }

                addNewUser(email, first, last, password, phone, bloodText, sexText, roleText);
            }
        });
    }

    public void addNewUser(String email, String first, String last, String pass, String phone, String blood, String sex, String role){
        User user = new User(email, first, last, pass, phone, blood, sex, role);

        String emailPrefix = email.split("@")[0];

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        ref = db.getReference("users");
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
