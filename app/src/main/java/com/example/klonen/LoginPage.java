package com.example.klonen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class LoginPage extends AppCompatActivity {


    private TextInputLayout emailLayout, passLayout;
    private EditText email, pass;
    private TextView forgot_pass, register;
    private Button login;
    private ProgressDialog dialog;

    private FirebaseAuth mauth;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!= null){
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

       initializeVariables();
        mAuth = FirebaseAuth.getInstance();

       createRequest();

       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                if(validate_credentials()){
                    //Toast.makeText(LoginPage.this, "Validated", Toast.LENGTH_SHORT).show();
                    login();
                }

           }
       });

       register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(LoginPage.this,Signup.class));
               finish();
           }
       });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this,ForgotPassword.class));

            }
        });

        findViewById(R.id.layout_gg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ProgressDialog(LoginPage.this);
                dialog.show();
                dialog.setContentView(R.layout.progress_bar);
                dialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                signIn();
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
    private void alert_builder(String error){
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginPage.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);
        materialAlertDialogBuilder.setMessage(error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }


    private void login(){
        String email_str = email.getText().toString();
        String password = pass.getText().toString();

        passLayout.setErrorEnabled(false);

        mauth = FirebaseAuth.getInstance();

        mauth.signInWithEmailAndPassword(email_str,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mauth.getCurrentUser();
                    //assert user != null;
                    String UUID = user.getUid();
                    String DisplayName = user.getDisplayName();

                    boolean emailVerified = user.isEmailVerified();

                    if(emailVerified){
                        startActivity(new Intent(LoginPage.this,Home.class));
                        finish();
                    }else{
                        Intent log_home = new Intent(LoginPage.this,Home.class);
                        log_home.putExtra("isverified",false);
                        startActivity(log_home);
                        finish();
//                        Toast.makeText(LoginPage.this, "E-Mail not verified", Toast.LENGTH_SHORT).show();
//                        alert_builder("E-Mail not verifier, Please Verify your E-mail to Login");
                           // mauth.signOut();
                    }





                }else{

                    String[] error = String.valueOf(task.getException()).split((":"));
                    //Toast.makeText(LoginPage.this,error[1], Toast.LENGTH_LONG).show();
                    alert_builder(error[1]);
                }
            }
        });




    }


    // Gmail :

    private void createRequest(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.dismiss();
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("check", "Hello---------------------------------------------------------------------------------------------------------------------------------firebaseAuthWithGoogle:" + account.getId());
                dialog.show();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            dialog.dismiss();

                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "auth failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }




}