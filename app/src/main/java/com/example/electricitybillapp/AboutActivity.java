package com.example.electricitybillapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    TextView textInfo, linkGithub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textInfo = findViewById(R.id.textInfo);
        linkGithub = findViewById(R.id.linkGithub);

        textInfo.setText(
                "Name: Amirul Iman Bin Shahdan\n" +
                        "Student ID: 2021123456\n" +
                        "Course: ICT602 - Mobile Technology and Development\n" +
                        "© 2025 All rights reserved."
        );

        linkGithub.setText("https://github.com/amiruliman/electricity-bill-app");
    }
}
