package com.example.klonen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class LoginPage extends AppCompatActivity {


    private TextInputLayout emailLayout, passLayout;
    private EditText email, pass;
    private TextView forgot_pass, register;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

       initializeVariables();

       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               loginValidation();
           }
       });

    }

    private void initializeVariables(){
        emailLayout = findViewById(R.id.material_email);
        passLayout = findViewById(R.id.material_pass);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);

        forgot_pass = findViewById(R.id.forgot);
        register = findViewById(R.id.register);

        login = findViewById(R.id.login);
    }

    private void loginValidation(){
        if(email.getText().toString().isEmpty()){
            emailLayout.setError("This filed should not be empty!");
        } else{
            emailLayout.setErrorEnabled(false);        }
        if(pass.getText().toString().isEmpty()){
            passLayout.setError("This Field should not be empty!");
        } else{
            passLayout.setErrorEnabled(false);
        }
        if( (email.getText().toString().equals("admin")) && (pass.getText().toString().equals("1234")) ){
            Intent intent = new Intent(LoginPage.this, Home.class);
            startActivity(intent);
        }
    }

}