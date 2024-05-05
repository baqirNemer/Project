package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private ImageView profileButton;
    private TabLayout tabs;
    public static String userID;
    private ViewPager2 viewPager2;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DatabaseHandler(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USERID")) {
            userID = intent.getStringExtra("USERID");
        }

        logUserProfilePicturePath(userID);

        if (!dbHandler.isUserIDExists(userID)) {
            dbHandler.addUser(userID);
        }

        Bundle bundle = new Bundle();
        bundle.putString("USERID", userID);
        RecordsFragment recordsFragment = new RecordsFragment();
        recordsFragment.setArguments(bundle);

        tabs = findViewById(R.id.tabs);
        viewPager2 = findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
                tabs.getTabAt(position).select();
            }
        });

        profileButton = findViewById(R.id.profileIcon);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfilePopup();
            }
        });
    }
    private void logoutUser() {
        // Perform logout actions here (e.g., clear session, preferences, etc.)

        // Navigate to LoginActivity to log out
        Intent intent = new Intent(MainActivity.this, HomePage.class);
        // Clear back stack and start LoginActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // Finish current activity
        finish();
    }

    private void showProfilePopup() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, profileButton);
        popupMenu.getMenuInflater().inflate(R.menu.profile_items, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.Profile) {
                    // Open user profile activity
                    Intent intent = new Intent(MainActivity.this, UserProfile.class);
                    intent.putExtra("ID", userID);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.Logout) {
                    // Perform logout action
                    logoutUser();
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }


    private void logUserProfilePicturePath(String userID) {
        String profilePicturePath = dbHandler.getUserProfilePicturePath(userID);
    }
}
