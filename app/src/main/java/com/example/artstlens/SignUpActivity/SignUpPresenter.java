package com.example.artstlens.SignUpActivity;

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
                if(task.isSuccessful()){
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> it) {
                            mvpview.setProgressBarVisibility(View.GONE);
                            mAuth.signOut();
                            if(it.isSuccessful())
                            mvpview.makeToast("User registered successfully\n Verification Email sent Please Verify First");
                            else
                                mvpview.makeToast(it.getException().getMessage());
                        }
                    });
                }
                else{
                    mAuth.signOut();
                    mvpview.setProgressBarVisibility(View.GONE);
                    mvpview.makeToast(task.getException().getMessage());
                }
            }
        });
    }
}
