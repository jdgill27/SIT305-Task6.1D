package com.example.food_rescue_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private EditText userEmail, userPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void Sign_Up(View view) {
        Intent intent = new Intent(this, sign_up.class);
        startActivity(intent);
    }

    public void Login(View view) {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (email.isEmpty())
        {
            userEmail.setError("Please provide your full name!");
            userEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            userEmail.setError("Please provide a valid email!");
            userEmail.requestFocus();
            return;
        }

        if (password.isEmpty())
        {
            userPassword.setError("Please provide your email!");
            userPassword.requestFocus();
            return;
        }

        if (password.length() < 6)
        {
            userPassword.setError("Password should contain more than 6 characters!");
            userPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(MainActivity.this, home.class);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(MainActivity.this, "Failed to Login!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}