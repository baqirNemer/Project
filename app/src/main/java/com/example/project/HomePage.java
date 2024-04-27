package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class HomePage extends AppCompatActivity {

    private ImageView menuButton;
    private ImageView profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        menuButton = findViewById(R.id.menuButton);
        profileButton = findViewById(R.id.profileIcon);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuPopup();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfilePopup();
            }
        });
    }
    private void showMenuPopup() {
        PopupMenu popupMenu01 = new PopupMenu(HomePage.this, menuButton);
        popupMenu01.getMenuInflater().inflate(R.menu.menu_items, popupMenu01.getMenu());
        popupMenu01.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item clicks
                if (item.getItemId() == R.id.aboutItem) {
                    // Handle About item click
                    // Add your code here
                    return true;
                } else if (item.getItemId() == R.id.learnItem) {
                    // Handle Learn item click
                    // Add your code here
                    return true;
                } else if (item.getItemId() == R.id.communityItem) {
                    // Handle Community item click
                    // Add your code here
                    return true;
                } else if (item.getItemId() == R.id.servicesItem) {
                    // Handle Services item click
                    // Add your code here
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu01.show();
    }
    private void showProfilePopup() {
        PopupMenu popupMenu02 = new PopupMenu(HomePage.this, profileButton);
        popupMenu02.getMenuInflater().inflate(R.menu.user_items, popupMenu02.getMenu());
        popupMenu02.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item clicks
                if (item.getItemId() == R.id.Login) {
                    // Handle About item click
                    // Add your code here
                    return true;
                } else if (item.getItemId() == R.id.Register) {
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
