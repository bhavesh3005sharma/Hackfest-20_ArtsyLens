package com.example.artstlens.SignUpActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.artstlens.LoginActivity.LoginActivity;
import com.example.artstlens.MainActivity;
import com.example.artstlens.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, SignUpContract.View {

    EditText editTextEmail, editTextPassword;
    Button buttonSignUp;
    ProgressBar progressBar;
    SignUpPresenter presenter;
    
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        presenter = new SignUpPresenter(this);

        editTextEmail = (EditText) findViewById(R.id.signupEmail);
        editTextPassword = (EditText) findViewById(R.id.signupPassword);
        buttonSignUp = (Button) findViewById(R.id.signupButton);
        progressBar = (ProgressBar) findViewById(R.id.signupProgressBar);

        buttonSignUp.setOnClickListener(this);
        findViewById(R.id.loginActivate).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signupButton:
                RegisterUser();
                break;
            case R.id.loginActivate:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    public void RegisterUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required.");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Invalid email.Please enter valid email ID.");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            editTextPassword.setError("Please enter a password of length more than 6.");
            editTextPassword.requestFocus();
            return;
        }

        presenter.signUpUser(mAuth, email, password);

    }

    @Override
    public void setProgressBarVisibility(int visible) {
        progressBar.setVisibility(visible);
    }

    @Override
    public void startProfileActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
