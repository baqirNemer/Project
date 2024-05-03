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

        Intent intent = getIntent();

        String categoryName = intent.getStringExtra("CATEGORY");
        String procedureDescription = intent.getStringExtra("DESC");
        String procedureResult = intent.getStringExtra("RESULT");
        String doctorName = intent.getStringExtra("DOCTOR");
        String hospitalName = intent.getStringExtra("HOSPITAL");
        String dateOfProcedure = intent.getStringExtra("DATE");
        //String hospitalID = intent.getStringExtra("HID");
        String doctorID = intent.getStringExtra("DID");

        TextView category = findViewById(R.id.Title);
        TextView doctor = findViewById(R.id.Doctor);
        TextView hospital = findViewById(R.id.Hospital);
        TextView date = findViewById(R.id.Date);
        TextView result = findViewById(R.id.Result);
        TextView description = findViewById(R.id.Description);

        category.setText(categoryName);
        doctor.setText(doctorName);
        hospital.setText(hospitalName);
        date.setText(dateOfProcedure);
        result.setText(procedureResult);
        description.setText(procedureDescription);

        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordCardPage.this, HospitalProfile.class);
                intent.putExtra("NAME", hospitalName);
                startActivity(intent);
            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordCardPage.this, DoctorProfile.class);
                intent.putExtra("ID", doctorID);
                startActivity(intent);
            }
        });
    }
}
