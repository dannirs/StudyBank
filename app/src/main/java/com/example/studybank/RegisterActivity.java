package com.example.studybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

public class RegisterActivity extends AppCompatActivity {

    Button btnBack;
    EditText fullName;
    EditText email;
    EditText password;
    EditText confirmpassword;
    Button btnRegister;
    private FirebaseAuth mAuth;
    String strEmail;
    String strPassword;
    String strName;
    String strConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        fullName = findViewById(R.id.c_name);
        email = findViewById(R.id.c_email);
        password = findViewById(R.id.c_password);
        confirmpassword = findViewById(R.id.c_confirm);
        btnRegister = findViewById(R.id.regbtn);
        btnBack = findViewById(R.id.btnback);
    }

    public void onClickBack(View view) {

        Intent myIntent = new Intent(this, LoginActivity.class);
        this.startActivity(myIntent);

    }

    public void onClickRegister(View view) {

        strEmail = email.getText().toString().trim();
        strPassword = password.getText().toString().trim();
        strName = fullName.getText().toString().trim();
        strConfirmPassword = confirmpassword.getText().toString().trim();

        if (strName.length()>0 && strEmail.length()>0 && strPassword.length()>0 && strConfirmPassword.length()>0 && strConfirmPassword.equals(strPassword)) {
            // goes to next activity
            if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                email.setError("Invalid email");
                email.requestFocus();
            }
            else {
                registerUser();
                Intent myIntent = new Intent(this, LoginActivity.class);
                this.startActivity(myIntent);
                Toast toast_2 = Toast.makeText(getApplicationContext(), "Account Registered",Toast.LENGTH_SHORT);
                toast_2.show();
            }
        } else {
            if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches() && strEmail.length() >0) {
                email.setError("Invalid email");
                email.requestFocus();
            }
            if(!strConfirmPassword.equals(strPassword)) {
                password.setError("The password confirmation does not match");
                password.requestFocus();
            }

            if(strName.length()==0 || strEmail.length()==0||strConfirmPassword.length()==0 && strPassword.length()==0) {
                Toast toast_2 = Toast.makeText(getApplicationContext(), "Please fill in the highlighted areas.", Toast.LENGTH_SHORT);
                toast_2.show();
                errors();
            }
//            errors();
        }
    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(strName, strEmail);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "User has been registered", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register user, please try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Failed to register user, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void errors() {
        if (strName.length()==0) {
            fullName.setBackgroundColor(RED);
        } else {
            fullName.setBackgroundColor(WHITE);
        }
        if (strEmail.length()==0) {
            email.setBackgroundColor(RED);
        } else {
            email.setBackgroundColor(WHITE);
        }
//        if (strPassword.length()==0 || strConfirmPassword.length()==0 || strConfirmPassword.equals(strPassword)) {
//            password.setBackgroundColor(RED);
//            confirmpassword.setBackgroundColor(RED);
        if (strPassword.length()==0 || strConfirmPassword.length() == 0) {
            password.setBackgroundColor(RED);
            confirmpassword.setBackgroundColor(RED);
        } else {
            password.setBackgroundColor(WHITE);
            confirmpassword.setBackgroundColor(WHITE);
        }

    }
}