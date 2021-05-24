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
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public EditText full_name, email, phone, address, password, confirm_password;
    int cond = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        full_name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        password = findViewById(R.id.password_set);
        confirm_password = findViewById(R.id.password_confirm);

    }

    public void Create_user(View view) {
        cond = 0;
        Register_User();
        if (cond == 0)
        {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void Register_User() {
        String name, em, ph, add, pass, com_pass;

        name = full_name.getText().toString();
        em = email.getText().toString();
        ph = phone.getText().toString();
        add = address.getText().toString();
        pass = password.getText().toString();
        com_pass = confirm_password.getText().toString();

        if (name.isEmpty())
        {
            full_name.setError("Please provide your full name!");
            full_name.requestFocus();
            cond = 1;
            return;
        }

        if (em.isEmpty())
        {
            email.setError("Please provide your email!");
            email.requestFocus();
            cond = 1;
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(em).matches())
        {
            email.setError("Please provide a valid email!");
            email.requestFocus();
            cond = 1;
            return;
        }

        if (ph.isEmpty())
        {
            phone.setError("Please provide your phone!");
            phone.requestFocus();
            cond = 1;
            return;
        }

        if (add.isEmpty())
        {
            address.setError("Please provide your address!");
            address.requestFocus();
            cond = 1;
            return;
        }

        if (pass.isEmpty())
        {
            password.setError("Please create a password!");
            password.requestFocus();
            cond = 1;
            return;
        }

        if (pass.length() < 6)
        {
            password.setError("Password should contain more than 6 characters!");
            password.requestFocus();
            cond = 1;
            return;
        }

        if (com_pass.isEmpty())
        {
            confirm_password.setError("Please confirm your password!");
            confirm_password.requestFocus();
            cond = 1;
            return;
        }

        mAuth.createUserWithEmailAndPassword(em, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            User user = new User(name, em, ph, add);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(sign_up.this, "User has been resgistered Successfully", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(sign_up.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(sign_up.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
}