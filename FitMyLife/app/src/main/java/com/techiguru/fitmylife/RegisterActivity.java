package com.techiguru.fitmylife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {
    //declare
    EditText et_emailID,et_Password,et_fullName,et_confirmPassword;
    TextView tv_alreadyAccount;
    Button bt_register;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        //to create or open the shrared preference file with mode
        sp=getSharedPreferences("myPref", Context.MODE_PRIVATE);
        ed=sp.edit();



        bt_register = findViewById(R.id.bt_register);
        tv_alreadyAccount = findViewById(R.id.tv_alreadyAccount);
        et_Password = findViewById(R.id.et_password);
        et_emailID = findViewById(R.id.et_emailID);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        et_fullName = findViewById(R.id.et_fullName);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to main
                // activity
                Intent rlogin = new Intent(RegisterActivity.this, MainActivity.class);
                //to start the intent
                startActivity(rlogin);
                finish();
                //to save the user details in sharedpreference
                ed.putString("name",et_fullName.getText().toString());
                ed.putString("emailid",et_emailID.getText().toString());
                ed.putString("password",et_Password.getText().toString());
                ed.commit();
            }
        });
        tv_alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to main
                // activity
                Intent clogin = new Intent(RegisterActivity.this, LoginActivity.class);
                //to start the intent
                startActivity(clogin);
                finish();
            }
        });
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
    }