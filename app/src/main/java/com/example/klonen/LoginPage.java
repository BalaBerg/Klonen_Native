package com.example.klonen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {


    private TextInputLayout emailLayout, passLayout;
    private EditText email, pass;
    private TextView forgot_pass, register;
    private Button login;

    private FirebaseAuth mauth;


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
                    login();
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

    private void login(){
        String email_str = email.getText().toString();
        String password = pass.getText().toString();

        mauth = FirebaseAuth.getInstance();

        mauth.signInWithEmailAndPassword(email_str,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mauth.getCurrentUser();
                    assert user != null;
                    String UUID = user.getUid();
                    String DisplayName = user.getDisplayName();

                    boolean emailVerified = user.isEmailVerified();

                    if(emailVerified){
                        startActivity(new Intent(LoginPage.this,Home.class));
                    }else{
                        Toast.makeText(LoginPage.this, "E-Mail not verified", Toast.LENGTH_SHORT).show();
                    }





                }else{
                    Toast.makeText(LoginPage.this,String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

}