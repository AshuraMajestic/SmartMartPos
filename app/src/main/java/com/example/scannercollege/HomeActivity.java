package com.example.scannercollege;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.scannercollege.Domain.UserData;
import com.google.android.material.textfield.TextInputLayout;

public class HomeActivity extends AppCompatActivity {
private AppCompatButton submitBtn;
private TextInputLayout NameLayout,NumberLayout,MailLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        submitBtn=findViewById(R.id.submitButton);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve data from EditText fields
                EditText nameEditText = findViewById(R.id.enterName);
                EditText numberEditText = findViewById(R.id.enterNumber);
                EditText emailEditText = findViewById(R.id.enterMail);

                // Get text from EditTexts
                String name = nameEditText.getText().toString();
                String phoneNumber = numberEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                if(name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()){
                    Toast.makeText(HomeActivity.this, "Please Enter the Detail", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidPhoneNumber(phoneNumber)) {
                    Toast.makeText(HomeActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(HomeActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Store data in a temporary storage (You can use SharedPreferences, Bundle, Singleton, etc.)
                UserData userData = UserData.getInstance();
                userData.setName(name);
                userData.setPhoneNumber(phoneNumber);
                userData.setEmail(email);

                // Navigate to another activity
                Intent intent = new Intent(HomeActivity.this, ScanItemActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Regular expression for validating a phone number (10 digits)
        String phoneNumberPattern = "^[0-9]{10}$";
        return phoneNumber.matches(phoneNumberPattern);
    }

    private boolean isValidEmail(String email) {
        // Using Android's built-in Patterns class to validate email
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}