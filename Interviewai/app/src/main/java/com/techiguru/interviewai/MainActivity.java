package com.techiguru.interviewai;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    CardView cardJava, cardTechnical, cardHR,
            cardDSA, cardLogout;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        cardJava = findViewById(R.id.cardJava);
        cardTechnical = findViewById(R.id.cardTechnical);
        cardHR = findViewById(R.id.cardHR);
        cardDSA = findViewById(R.id.cardDSA);

        cardLogout = findViewById(R.id.cardLogout);

        cardJava.setOnClickListener(v -> openInterview("Java"));

        cardTechnical.setOnClickListener(v -> openInterview("Technical"));

        cardHR.setOnClickListener(v -> openInterview("HR"));

        cardDSA.setOnClickListener(v -> openInterview("DSA"));



        cardLogout.setOnClickListener(v -> {

            auth.signOut();

            startActivity(
                    new Intent(MainActivity.this,
                            LoginActivity.class));

            finish();
        });
    }

    private void openInterview(String category) {

        Intent intent = new Intent(
                MainActivity.this,
                InterviewActivity.class);

        intent.putExtra("category", category);

        startActivity(intent);
    }
}