package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecordCardPage extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_card_page);

        TextView hospitalProfile = findViewById(R.id.Hospital);
        hospitalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordCardPage.this, HospitalProfile.class);
                startActivity(intent);
            }
        });

        TextView doctorProfile = findViewById(R.id.Doctor);
        doctorProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordCardPage.this, DoctorProfile.class);
                startActivity(intent);
            }
        });
    }
}
