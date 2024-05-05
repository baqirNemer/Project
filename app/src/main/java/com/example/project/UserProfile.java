package com.example.project;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class UserProfile extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static String userID;
    String TAG;
    private DatabaseHandler dbHandler;
    private ProfileUpdateReceiver profileUpdateReceiver;
    private static final String ACTIVITY_TAG = "UserProfile";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        dbHandler = new DatabaseHandler(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ID")) {
            userID = intent.getStringExtra("ID");
        }

        TAG = "Firebase";

        TextView name = findViewById(R.id.Name);
        TextView age = findViewById(R.id.Age);
        TextView email = findViewById(R.id.Email);
        TextView phone = findViewById(R.id.PhoneNumber);
        TextView blood = findViewById(R.id.BloodType);
        TextView sex = findViewById(R.id.Sex);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = db.getReference("users");

        usersRef.orderByChild("email").equalTo(userID+"@gmail.com").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null) {
                            String userEmail = user.getEmail();
                            String userPhoneNumber = user.getPhoneNumber();
                            String userFirstName = user.getFirstName();
                            String userLastName = user.getLastName();
                            String userBloodType = user.getBloodType();
                            String userSex = user.getSex();
                            int userAge = user.getAge();

                            name.setText(userFirstName + " " + userLastName);
                            age.setText(Integer.toString(userAge));
                            email.setText(userEmail);
                            phone.setText(userPhoneNumber);
                            blood.setText(userBloodType);
                            sex.setText(userSex);

                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
                Toast.makeText(UserProfile.this, "Database error.", Toast.LENGTH_LONG).show();
                name.setText("");
                age.setText("");
                email.setText("");
                phone.setText("");
                blood.setText("");
                sex.setText("");
            }
        });

        ImageView edit = findViewById(R.id.Edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, EditProfile.class);
                String fullName = name.getText().toString();
                String[] names = fullName.split(" ");

                String firstName = "";
                String lastName = "";
                if (names.length > 0) {
                    firstName = names[0];
                    if (names.length > 1) {
                        lastName = names[1];
                    }
                }
                intent.putExtra("ID", userID);
                intent.putExtra("FIRST", firstName);
                intent.putExtra("LAST", lastName);
                intent.putExtra("AGE", age.getText().toString());
                intent.putExtra("PHONE", phone.getText().toString());
                intent.putExtra("SEX", sex.getText().toString());
                intent.putExtra("BLOOD", blood.getText().toString());
                startActivity(intent);
            }
        });

        String profilePicturePath = dbHandler.getUserProfilePicturePath(userID);
        ImageView profileImageView = findViewById(R.id.Profile);
        if (!profilePicturePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(profilePicturePath);
            if (bitmap != null) {
                profileImageView.setImageBitmap(bitmap);
            } else {
                Log.e("Error", "Failed to decode bitmap from file: " + profilePicturePath);
            }
        }

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startCameraActivity();
        }
    }

    private void startCameraActivity() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Log.d("Error", "Error while taking picture.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imageBitmap = detectFaces(imageBitmap);
            String imagePath = saveImageToFile(imageBitmap);
            dbHandler.addOrUpdateUserProfilePicture(userID, imagePath);

            ImageView profileImageView = findViewById(R.id.Profile);
            if (!imagePath.isEmpty()) {
                 imageBitmap = BitmapFactory.decodeFile(imagePath);
                if (imageBitmap != null) {
                    profileImageView.setImageBitmap(imageBitmap);
                } else {
                    Log.e("Error", "Failed to decode bitmap from file: " + imagePath);
                }
            }
        }
    }

    private String saveImageToFile(Bitmap imageBitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageFileName + ".jpg");

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "Image saved to: " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap detectFaces(Bitmap imageBitmap) {
        FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .build();
        Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);

        Bitmap croppedBitmap = imageBitmap;

        if (faces.size() > 0) {
            Face face = faces.valueAt(0);

            float x1 = face.getPosition().x;
            float y1 = face.getPosition().y;
            float x2 = x1 + face.getWidth();
            float y2 = y1 + face.getHeight();

            croppedBitmap = Bitmap.createBitmap(imageBitmap, (int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1));
        } else {
            Log.d("Faces", "No faces detected.");
        }

        faceDetector.release();
        return croppedBitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onResume() {
        super.onResume();
        profileUpdateReceiver = new ProfileUpdateReceiver();
        IntentFilter filter = new IntentFilter(EditProfile.ACTION_PROFILE_UPDATED);
        registerReceiver(profileUpdateReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        Log.d(ACTIVITY_TAG, "Profile update receiver registered.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (profileUpdateReceiver != null) {
            unregisterReceiver(profileUpdateReceiver);
            Log.d(ACTIVITY_TAG, "Profile update receiver unregistered.");
        }
    }
}