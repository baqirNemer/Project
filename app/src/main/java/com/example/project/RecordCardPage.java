package com.example.project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecordCardPage extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_card_page);

        createNotificationChannel();

        Intent intent = getIntent();

        String categoryName = intent.getStringExtra("CATEGORY");
        String procedureDescription = intent.getStringExtra("DESC");
        String procedureResult = intent.getStringExtra("RESULT");
        String doctorName = intent.getStringExtra("DOCTOR");
        String hospitalName = intent.getStringExtra("HOSPITAL");
        String dateOfProcedure = intent.getStringExtra("DATE");
        String doctorID = intent.getStringExtra("DID");

        TextView category = findViewById(R.id.Title);
        TextView doctor = findViewById(R.id.Doctor);
        TextView hospital = findViewById(R.id.Hospital);
        TextView date = findViewById(R.id.Date);
        TextView result = findViewById(R.id.Result);
        TextView description = findViewById(R.id.Description);

        ImageView share = findViewById(R.id.Share);
        ImageView download = findViewById(R.id.Download);

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

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordCardPage.this, ChooseRecipient.class);
                String message = categoryName + " performed at " + hospitalName + " under the supervision of Dr. " +
                        doctorName + " on " + dateOfProcedure + "." + " This procedure was " + procedureResult;
                intent.putExtra("SMS", message);
                startActivity(intent);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downloadIntent = new Intent(RecordCardPage.this, DownloadService.class);
                downloadIntent.putExtra("PDF_URL", "https://www.cdc.gov/ncbddd/sicklecell/documents/SCD-factsheet_What-is-SCD.pdf");
                startService(downloadIntent);
            }
        });
    }

    private void createNotificationChannel() {
        CharSequence name = "Download Channel";
        String description = "Channel for download notifications";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel("download_channel", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
