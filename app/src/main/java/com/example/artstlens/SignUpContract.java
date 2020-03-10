package com.example.artstlens;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpContract {
    public interface View{

        void setProgressBarVisibility(int visible);

        void startProfileActivity();

        void makeToast(String message);
    }
    public interface Presenter{

        void signUpUser(FirebaseAuth mAuth, String email, String password);
    }
}
