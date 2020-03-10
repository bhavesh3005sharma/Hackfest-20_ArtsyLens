package com.example.artstlens;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpPresenter implements SignUpContract.Presenter{
    SignUpContract.View mvpview;

    public SignUpPresenter(SignUpContract.View mvpview) {
        this.mvpview = mvpview;
    }

    @Override
    public void signUpUser(FirebaseAuth mAuth, String email, String password) {
        mvpview.setProgressBarVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mvpview.setProgressBarVisibility(View.GONE);
                if(task.isSuccessful()){
                    mvpview.makeToast("User registered successfully");
                    mvpview.startProfileActivity();
                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        mvpview.makeToast("You are already registered.");
                    }
                    else{
                        mvpview.makeToast(task.getException().getMessage());
                    }
                }
            }
        });
    }
}
