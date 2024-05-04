package com.example.eightballbooking;

import android.os.Bundle;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {

    private TextView helloText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

//        TextView helloText = findViewById(R.id.textView);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.navigation_home) {
                // Do nothing, we're already on the home page
                selectedFragment = new Home();
//                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
//                Intent intent = new Intent(this, ProfileActivity.class);
//                startActivity(intent);
                selectedFragment = new Profile();
//                return true;
            } else if (item.getItemId() == R.id.navigation_settings) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
                selectedFragment = new Settings();
//                    return true;
            }
            System.out.println("Selected Fragment: " + selectedFragment.getView());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();
        }


        // setContentView(R.layout.activity_home_page); // Use this line if not using View Binding

//        helloText = findViewById(R.id.helloWorldTextView);

//        helloText.setText("Hello, " + User.name + "!");
        // Your additional setup here, if necessary
    }

    // Add any additional methods your activity needs here
}