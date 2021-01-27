package com.example.studybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText email;
    private EditText password;
    Button btnRegister;
    private FirebaseAuth mAuth;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.registerBtn);
        btnLogin = findViewById(R.id.loginBtn);

        sharedPreferences = getSharedPreferences("loginref",MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void onClickLogin(View view) {

    logIn();

    }

    public void logIn() {
        //Intent myIntent = new Intent(this, **CLASS GOES HERE**.class);
        //this.startActivity(myIntent);

        String strEmail = email.getText().toString().trim();
        String strPassword = password.getText().toString().trim();

        if (strEmail.isEmpty()) {
            email.setError("Enter email address");
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()){
            email.setError("Enter a valid email");
            return;
        }
        if (strPassword.isEmpty()) {
            password.setError("Enter password");
            return;
        }

        mAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, HomepageActivity.class));
                }
                else {
                    Toast.makeText(LoginActivity.this, "Incorrect email and password combination, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });







    }
    public void onClickRegister(View view) {

        Intent myIntent = new Intent(this, RegisterActivity.class);
        this.startActivity(myIntent);


        //will open up new activity that ask for user information and adds it to the firebase
    }
}
