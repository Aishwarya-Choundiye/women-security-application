package com.example.womenssafety;
import android.location.LocationManager;
import android.provider.Settings;
import android.content.Context;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class pg2 extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 101;

    private String mobile1, mobile2;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg2);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Retrieve mobile numbers from intent
        Intent intent = getIntent();
        mobile1 = intent.getStringExtra("MOB1");
        mobile2 = intent.getStringExtra("MOB2");

        if (mobile1 == null || mobile2 == null) {
            Toast.makeText(this, "No mobile numbers found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Request permissions if not already granted
        checkAndRequestPermissions();

        // Set up Women Laws button
        AppCompatImageButton btnWomenLaws = findViewById(R.id.womenLawsButton);
        btnWomenLaws.setOnClickListener(view -> {
            String url = "https://www.lexisnexis.in/blogs/laws-for-women-in-india/";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });

        // Set up SOS button
        AppCompatImageButton btnSOS = findViewById(R.id.btn_sos);
        btnSOS.setOnClickListener(v -> sendSOS());
    }

    private void checkAndRequestPermissions() {
        boolean smsPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        boolean locationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!smsPermissionGranted || !locationPermissionGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE
            );
        } else {
            Toast.makeText(this, "All permissions already granted!", Toast.LENGTH_SHORT).show();
            checkLocationServicesEnabled(); // Check if location services are enabled
        }
    }

    private void checkLocationServicesEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGpsEnabled && !isNetworkEnabled) {
            // Prompt the user to enable location services
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent); // Open the location settings
        }
    }




    private void sendSOS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    String locationUrl = "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                    String message = "SOS Alert! I need help immediately. Location: " + locationUrl;

                    // Send SMS in a new thread
                    new Thread(() -> {
                        sendSMS(mobile1, message);
                        sendSMS(mobile2, message);
                    }).start();

                    runOnUiThread(() ->
                            Toast.makeText(pg2.this, "SOS message sent with location", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    Toast.makeText(pg2.this, "Failed to retrieve location.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() ->
                    Toast.makeText(this, "Failed to send SMS.", Toast.LENGTH_SHORT).show()
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean smsPermissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean locationPermissionGranted = grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (smsPermissionGranted && locationPermissionGranted) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
                checkLocationServicesEnabled(); // Ensure location services are enabled
            } else {
                Toast.makeText(this, "Permissions denied. App functionality may be limited.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
