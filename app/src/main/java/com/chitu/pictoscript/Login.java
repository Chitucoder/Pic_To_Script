package com.chitu.pictoscript;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextInputLayout username,password;
    ProgressBar bar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=(TextInputLayout)findViewById(R.id.username);
        password=(TextInputLayout)findViewById(R.id.password);
        bar=(ProgressBar) findViewById(R.id.progressBar);


    }

    public void signuphere(View view) {

        bar.setVisibility(View.VISIBLE);
        String email=username.getEditText().getText().toString();
        String pass=password.getEditText().getText().toString();

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            bar.setVisibility(View.INVISIBLE);
                            username.getEditText().setText("");
                            password.getEditText().setText("");
                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            bar.setVisibility(View.INVISIBLE);
                            username.getEditText().setText("");
                            password.getEditText().setText("");
                            Toast.makeText(getApplicationContext(), "Process Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}