package com.example.scannercollege;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

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
}