package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

public class HospitalProfile extends AppCompatActivity {
    String TAG;
    private DatabaseReference hospitalsRef;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_profile);

        TAG = "Firebase";

        ImageView image = findViewById(R.id.Image);

        TextView name = findViewById(R.id.Name);
        TextView description = findViewById(R.id.Description);
        TextView phone = findViewById(R.id.PhoneNumber);
        TextView email = findViewById(R.id.Email);
        TextView location = findViewById(R.id.Location);

        ImageView call = findViewById(R.id.Call);
        ImageView send = findViewById(R.id.Send);
        ImageView map = findViewById(R.id.Map);

        Intent intent = getIntent();

        String hospitalName = intent.getStringExtra("NAME");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        hospitalsRef = db.getReference("hospitals");

        hospitalsRef.orderByChild("name").equalTo(hospitalName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot hospitalSnapshot : dataSnapshot.getChildren()) {
                        Hospital hospital = hospitalSnapshot.getValue(Hospital.class);
                        if (hospital != null) {
                            String hospitalDescription = hospital.getDescription();
                            String hospitalImageURL = hospital.getImageURL();
                            String hospitalLocation = hospital.getLocation();
                            String hospitalEmail = hospital.getEmail();
                            String hospitalPhoneNumber = hospital.getPhoneNumber();

                            Picasso.get().load(hospitalImageURL).into(image);
                            name.setText(hospitalName);
                            description.setText(hospitalDescription);
                            location.setText(hospitalLocation);
                            email.setText(hospitalEmail);
                            phone.setText(hospitalPhoneNumber);
                            return;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
                Toast.makeText(HospitalProfile.this, "Database error.", Toast.LENGTH_LONG).show();
                name.setText(hospitalName);
                description.setText("");
                location.setText("");
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

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospitalsRef.orderByChild("name").equalTo(hospitalName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot hospitalSnapshot : dataSnapshot.getChildren()) {
                                Hospital hospital = hospitalSnapshot.getValue(Hospital.class);
                                if (hospital != null) {
                                    double hospitalLongitude = hospital.getLongitude();
                                    double hospitalLatitude = hospital.getLatitude();

                                    Intent intent = new Intent(HospitalProfile.this, HospitalLocation.class);
                                    intent.putExtra("LONGITUDE", hospitalLongitude);
                                    intent.putExtra("LATITUDE", hospitalLatitude);
                                    intent.putExtra("TITLE", hospitalName);

                                    startActivity(intent);

                                    return;
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "Database error: " + databaseError.getMessage());
                        Toast.makeText(HospitalProfile.this, "Database error.", Toast.LENGTH_LONG).show();
                    }
                });
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

    private void sendEmail(String address/*, String subject, String message*/){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        //emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        Intent driveIntent = new Intent(Intent.ACTION_SEND);
        driveIntent.setType("text/plain");
        //driveIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //driveIntent.putExtra(Intent.EXTRA_TEXT, message);

        Intent chooserIntent = Intent.createChooser(emailIntent, "Choose app to send email with:");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{driveIntent});

        if (chooserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooserIntent);
        } else {
            Toast.makeText(this, "No apps found", Toast.LENGTH_LONG).show();
        }
    }
}
