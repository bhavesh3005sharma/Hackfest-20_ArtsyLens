package com.example.artstlens.LoginActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginContract {
    public interface View{

        void setProgressBarVisibility(int visible);

        void startProfileActivity();

        void makeToast(String message);
    }
    public interface Presenter{
        public void signIn(FirebaseAuth mAuth, String email, String password);
    }
}
