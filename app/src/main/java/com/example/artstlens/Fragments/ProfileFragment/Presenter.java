package com.example.artstlens.Fragments.ProfileFragment;

import android.net.Uri;
import android.view.View;
import androidx.annotation.NonNull;

import com.example.artstlens.Fragments.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class Presenter implements Contract.Presenter {
    Contract.mainView mainView;
    private StorageTask mUploadTask;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    StorageReference fileReference = FirebaseStorage.getInstance().getReference("profile/"+ FirebaseAuth.getInstance().getUid()+"--"+currentUser.getDisplayName());
    DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("profile/"+ FirebaseAuth.getInstance().getUid()+"--"+currentUser.getDisplayName());;

    public Presenter(Contract.mainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void SaveChanges(String name) {
        if(name.isEmpty())
            name="No Name is Set";
        if (currentUser != null){
            mainView.setProgressbar(View.VISIBLE);
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name).build();

            currentUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mainView.setProgressbar(View.GONE);
                    mainView.Toast("Profile Updated Successfully");
                }
            });
        }

    }

    @Override
    public void uploadProfilePic(Uri imageUri) {

        if (mUploadTask != null && mUploadTask.isInProgress())
            mainView.Toast("Upload is in Progress");
        else {
            if (imageUri != null) {
                mainView.setProgressbar(View.VISIBLE);
                mUploadTask = fileReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mainView.setProgressbar(View.GONE);
                                mainView.Toast("Upload successful");

                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();
                                        mDatabaseRef.setValue(url);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mainView.Toast(e.getMessage());
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                mainView.setProgressbar(View.VISIBLE);
                            }
                        });
            } else {
                mainView.Toast("No File Selected");
            }
        }
    }

    @Override
    public void downloadProfilePic() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.getValue().toString().equals(""))
                    mainView.setProfilePic(dataSnapshot.getValue().toString());
                mainView.setProgressbar(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mainView.setProgressbar(View.GONE);
                mainView.Toast(databaseError.getMessage());
            }
        });
    }
}

