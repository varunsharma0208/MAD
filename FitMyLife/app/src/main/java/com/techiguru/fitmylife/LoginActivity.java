package com.techiguru.fitmylife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
//declare the java objects based on UI widgets
    EditText et_emailID,et_Password;
    TextView tv_forgotPassword,tv_createAccount;
    Button bt_login;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        sp=getSharedPreferences("myPref",0);


        bt_login=findViewById(R.id.bt_login);
        tv_createAccount=findViewById(R.id.tv_createAccount);
        tv_forgotPassword=findViewById(R.id.tv_forgotPassword);
        et_emailID=findViewById(R.id.et_emailID);
        et_Password=findViewById(R.id.et_password);
        //to click on login button
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the data from shared preference and pass into string
                String email=sp.getString("emailid",null);
                String password=sp.getString("password",null);
                //to validate the email and password
                if (et_emailID.getText().toString().equals(email)
                && et_Password.getText().toString().equals(password))
                {
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putBoolean("isLoggedIn", true);
                    ed.apply();
                    //switch to main activity
                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                    //to start the intent
                    startActivity(main);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Please fill the correct details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //to click on create account textview and redirect to register activity
        tv_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                //to start the intent
                startActivity(register);
                finish();
            }
        });
        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot = new Intent(LoginActivity.this, ForgotActivity.class);
                //to start the intent
                startActivity(forgot);
                finish();
            }
        });
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}