package com.example.klonen;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    TextInputLayout email_layout,pass_layout,user_layout;
    EditText email,psw,uname;
    Button register_btn;
    ImageView google;

    private final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    private final int activ_code = 4789;

    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email_layout = findViewById(R.id.signup_email);
        pass_layout = findViewById(R.id.signup_pass);
        user_layout = findViewById(R.id.signup_user);

        email = findViewById(R.id.signup_email_address);
        psw = findViewById(R.id.signup_password);
        uname = findViewById(R.id.signup_user_name);

        register_btn = findViewById(R.id.signup_reg);





        auth = FirebaseAuth.getInstance();

        findViewById(R.id.go_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate_inputs()) {
                    signup_user();
                }
            }

        });

    }





    private void alert_builder(String error){
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(Signup.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);
        materialAlertDialogBuilder.setMessage(error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }


    private void DBAddData(String UID, String username, String Email, String Password){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> user_details = new HashMap<>();

        user_details.put("Username",username);
        user_details.put("E-Mail",Email);
        user_details.put("Password",Password);

        db.collection("users").document(UID).set(user_details)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("FireStore","data stored to firestore");
                        }else{
                            Log.d("Firestore",String.valueOf(task.getException()));
                        }


                    }
                });

    }

    private void signup_user(){
        String username = uname.getText().toString();
        String EMAIL = email.getText().toString();
        String password = psw.getText().toString();

        Log.d("Email",EMAIL);
        Log.d("Password",password);


        auth.createUserWithEmailAndPassword(EMAIL,password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                alert_builder(String.valueOf(e));

            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    auth.getCurrentUser().sendEmailVerification();
                    String UUID = auth.getCurrentUser().getUid();
                    DBAddData(UUID,username,EMAIL,password);
                    boolean isverified = auth.getCurrentUser().isEmailVerified();
                    String dpname = auth.getCurrentUser().getDisplayName();
                    Intent reg_home = new Intent(Signup.this,Home.class);
                    reg_home.putExtra("isverified",isverified);
                    reg_home.putExtra("DPname",dpname);
                    startActivity(reg_home);
                }else{
                    alert_builder(String.valueOf(task.getException()));
                }


            }
        });


    }



    private boolean validate_inputs(){

        boolean user_flag = uname_validator();
        boolean email_flag = email_validator();
        boolean psw_flag = password_validator();


        if(user_flag && email_flag && psw_flag){
            return true;
        }

        return false;


    }

    private boolean email_validator(){
        if(email.getText().toString().isEmpty()){
            email_layout.setError("This Field can't be empty");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email_layout.setError("Not a valid E-Mail");
            return false;

        }else{
            email_layout.setErrorEnabled(false);
        }




        return true;
    }

    private boolean password_validator(){
        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(psw.getText().toString());

        if(psw.getText().toString().isEmpty()){
            pass_layout.setError("This Field can't be empty");
            return false;
        }else if(!matcher.matches()) {
            pass_layout.setError("The password doesn't meet the standard");
            return false;

        }else{
            pass_layout.setErrorEnabled(false);
        }


        return true;

    }

    private boolean uname_validator(){
        if(uname.getText().toString().isEmpty()){
            user_layout.setError("This Field can't be empty");
            return false;
        }else if (uname.getText().toString().length()<8){
            user_layout.setError("Username must be atleast 8 in size");
            return false;

        }else{
            user_layout.setErrorEnabled(false);
        }



        return true;
    }

}