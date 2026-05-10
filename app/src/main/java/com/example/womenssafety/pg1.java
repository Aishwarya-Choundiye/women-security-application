package com.example.womenssafety;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class pg1 extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    public String mobile1, mobile2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg1);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        // Check if user info is already saved in the database
        String[] savedNumbers = databaseHelper.getUserInfo();
        if (savedNumbers != null && savedNumbers.length == 2) {
            // If numbers are found in the database, auto-redirect to pg2
            mobile1 = savedNumbers[0];
            mobile2 = savedNumbers[1];

            // Automatically redirect to pg2 with the saved numbers
            Intent intent = new Intent(pg1.this, pg2.class);
            intent.putExtra("MOB1", mobile1);
            intent.putExtra("MOB2", mobile2);
            startActivity(intent);
            finish();  // Close pg1 activity immediately
            return;  // Exit the onCreate method early
        }

        // Continue with normal flow if no data is found in the database
        EditText etName = findViewById(R.id.ei4);  // User name input
        EditText etMobile = findViewById(R.id.ei6);  // User mobile number input
        Button b = findViewById(R.id.btn1);  // Save button

        // Handle save button click event
        b.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Please fill the field", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(mobile)) {
                Toast.makeText(this, "Please fill in the mobile number", Toast.LENGTH_SHORT).show();
            } else if (mobile.length() != 10 || !mobile.matches("\\d+")) {
                Toast.makeText(this, "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
            } else {
                // Log the mobile numbers before insertion
                Log.d("pg1", "Mobile 1: " + mobile + ", Mobile 2: " + name);

                mobile1 = mobile; // Store mobile number in mobile1
                mobile2 = name;   // You can choose to store another number or name for mobile2

                // Insert data into the database
                databaseHelper.insertUser(mobile1, mobile2);

                // Log after insertion
                Log.d("pg1", "Data inserted into the database.");

                // Redirect to pg2
                Intent intent = new Intent(pg1.this, pg2.class);
                intent.putExtra("MOB1", mobile1);
                intent.putExtra("MOB2", mobile2);
                startActivity(intent);
                finish();
            }
        });
        Button b1 = findViewById(R.id.btn3);  // Cancel button
        b1.setOnClickListener(view -> {
            // Log to check if the button click is registered
            Log.d("pg1", "Cancel button clicked");

            // Navigate to pg3 activity
            Intent intent1 = new Intent(pg1.this, pg3.class);
            startActivity(intent1);  // Start pg3 activity first

            // Exit the app completely
            System.exit(0);  // Force close the app (not recommended for production use)
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("pg1", "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("pg1", "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("pg1", "onDestroy called");
    }
}
