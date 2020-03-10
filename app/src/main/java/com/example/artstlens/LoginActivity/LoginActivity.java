package com.example.artstlens.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artstlens.ForgotPasswordActivity;
import com.example.artstlens.MainActivity;
import com.example.artstlens.R;
import com.example.artstlens.SignUpActivity.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginContract.View {

    @BindView(R.id.loginUsername)
    EditText editTextUsername;
    @BindView(R.id.loginPassword)
    EditText editTextPassword;
    @BindView(R.id.loginProgressBar)
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LoginPresenter(this);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.ResetPasswordText).setOnClickListener(this);
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
                break;
            case R.id.ResetPasswordText:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
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
            editTextUsername.setError("Invalid email.Please enter valid email ID.");
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
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        if(mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().isEmailVerified()) {
            startProfileActivity();
        }
        super.onStart();
    }
}
