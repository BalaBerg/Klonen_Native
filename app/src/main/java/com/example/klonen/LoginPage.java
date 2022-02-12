package com.example.klonen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                if(validate_credentials()){
                    Toast.makeText(LoginPage.this, "Validated", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginPage.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
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


    private boolean validate_credentials(){


        if(email.getText().toString().isEmpty()){
            emailLayout.setError("This Field must not be empty");
            return false;
        }else {
            if(pass.getText().toString().isEmpty()){
                passLayout.setError("This field must not be empty");
                return false;
            }
            else{
                passLayout.setErrorEnabled(false
                );
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                emailLayout.setError("Not a valid mail ID");
                return false;
            }
            else{
                emailLayout.setErrorEnabled(false);
                return true;
            }

        }
    }

}