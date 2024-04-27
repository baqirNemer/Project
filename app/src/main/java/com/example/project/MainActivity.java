package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private ImageView profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileButton = findViewById(R.id.profileIcon);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfilePopup();
            }
        });


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void showProfilePopup() {
        PopupMenu popupMenu02 = new PopupMenu(MainActivity.this, profileButton);
        popupMenu02.getMenuInflater().inflate(R.menu.profile_items, popupMenu02.getMenu());
        popupMenu02.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item clicks
                if (item.getItemId() == R.id.Profile) {
                    // Handle About item click
                    // Add your code here
                    return true;
                } else if (item.getItemId() == R.id.Settings) {
                    // Handle Learn item click
                    // Add your code here
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu02.show();
    }
}
