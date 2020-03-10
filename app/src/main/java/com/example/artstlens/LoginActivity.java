package com.example.artstlens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginContract.View {

    EditText editTextUsername, editTextPassword;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new LoginPresenter(this);
        editTextUsername = (EditText) findViewById(R.id.loginUsername);
        editTextPassword = (EditText) findViewById(R.id.loginPassword);
        progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signupActivate).setOnClickListener(this);
        findViewById(R.id.loginButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signupActivate:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.loginButton:
                UserLogin();
        }
    }

    private void UserLogin() {
        String email = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextUsername.setError("Email is required.");
            editTextUsername.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextUsername.setError("Invalid email. Please enter a valid email ID.");
            editTextUsername.requestFocus();
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

        presenter.signIn(mAuth, email, password);


    }

    @Override
    public void setProgressBarVisibility(int visible) {
        progressBar.setVisibility(visible);
    }

    @Override
    public void startProfileActivity() {
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
