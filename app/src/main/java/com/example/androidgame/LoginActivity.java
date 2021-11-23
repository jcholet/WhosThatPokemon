package com.example.androidgame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private MaterialButton signIn;
    private TextInputEditText signInEmail;
    private TextInputEditText signInPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signInEmail = (TextInputEditText) findViewById(R.id.signInEmail);
        signInPassword = (TextInputEditText) findViewById(R.id.signInPassword);
        signIn = (MaterialButton) findViewById(R.id.signIn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LobbyActivity.class));
        finish();
    }

    private void userLogin(){
        String email = signInEmail.getText().toString().trim();
        String password = signInPassword.getText().toString().trim();

        if(email.isEmpty()){
            signInEmail.setError("Email required");
            signInEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signInEmail.setError("Not a valid item");
            signInEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            signInPassword.setError("Password required");
            signInPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            signInPassword.setError("Min length is 6");
            signInPassword.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
