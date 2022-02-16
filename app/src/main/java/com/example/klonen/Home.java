package com.example.klonen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    MaterialButton SignOut;
    private FirebaseAuth mauth;
    TextView title;

    String G_name, G_mail;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        title = findViewById(R.id.textView);
        SignOut = findViewById(R.id.sign_out);

        // Data => Gmail Login :

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            G_name = signInAccount.getDisplayName();
            G_mail = signInAccount.getEmail();

            Log.d(G_mail,G_name);
            title.setText("Welcome "+ G_name);

        }else{
            try {
                boolean isverified = (boolean) getIntent().getExtras().get("isverified");
                String DPName = (String) getIntent().getExtras().get("DPname");

                title.setText("Welcome "+ DPName);
                //Toast.makeText(Home.this, (String.valueOf(isverified)), Toast.LENGTH_SHORT).show();
            }catch (NullPointerException  e){
                Log.d("Exception",String.valueOf(e));
            }

        }

        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Welcome ");
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this,LoginPage.class));

            }
        });
    }
}