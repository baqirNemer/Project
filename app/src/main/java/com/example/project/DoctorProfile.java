package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DoctorProfile extends AppCompatActivity {
    String TAG;
    private DatabaseReference doctorsRef;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile);

        TAG = "Firebase";

        ImageView profile = findViewById(R.id.Profile);

        TextView name = findViewById(R.id.Name);
        TextView age = findViewById(R.id.Age);
        TextView email = findViewById(R.id.Email);
        TextView phone = findViewById(R.id.PhoneNumber);
        TextView description = findViewById(R.id.Description);

        ImageView call = findViewById(R.id.Call);
        ImageView send = findViewById(R.id.Send);

        Intent intent = getIntent();

        String doctorID = intent.getStringExtra("ID");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        doctorsRef = db.getReference("doctors");

        doctorsRef.orderByChild("email").equalTo(doctorID+"@gmail.com").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot doctorSnapshot : dataSnapshot.getChildren()) {
                        Doctor doctor = doctorSnapshot.getValue(Doctor.class);
                        if (doctor != null) {
                            String doctorDescription = doctor.getDescription();
                            String doctorEmail = doctor.getEmail();
                            String doctorPhoneNumber = doctor.getPhoneNumber();
                            String doctorFirstName = doctor.getFirstName();
                            String doctorLastName = doctor.getLastName();
                            String doctorImageURL = doctor.getImageURL();
                            int doctorAge = doctor.getAge();

                            Picasso.get().load(doctorImageURL).into(profile);
                            name.setText(doctorFirstName + " " + doctorLastName);
                            age.setText(Integer.toString(doctorAge));
                            description.setText(doctorDescription);
                            email.setText(doctorEmail);
                            phone.setText(doctorPhoneNumber);
                            return;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
                Toast.makeText(DoctorProfile.this, "Database error.", Toast.LENGTH_LONG).show();
                name.setText("");
                description.setText("");
                age.setText("");
                email.setText("");
                phone.setText("");
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(phone.getText().toString());
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(email.getText().toString());
            }
        });
    }

    private void makePhoneCall(String number){
        if(ContextCompat.checkSelfPermission(
                this.getApplicationContext(),android.Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new
                    String[]{android.Manifest.permission.CALL_PHONE}, 0);
        } else{
            String uri = "tel:" + number.trim();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(uri));
            if (callIntent.resolveActivity(this.getPackageManager()) != null) {
                this.startActivity(callIntent);
            }
        }
    }

    private void sendEmail(String address){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});

        Intent driveIntent = new Intent(Intent.ACTION_SEND);
        driveIntent.setType("text/plain");

        Intent chooserIntent = Intent.createChooser(emailIntent, "Choose app to send email with:");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{driveIntent});

        if (chooserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooserIntent);
        } else {
            Toast.makeText(this, "No apps found", Toast.LENGTH_LONG).show();
        }
    }
}