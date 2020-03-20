package com.example.artstlens.UploadPhoto;

import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.example.artstlens.Fragments.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class Presenter implements Contract.Presenter {
    DatabaseReference mDatabaseRef;
    StorageReference mStorageRef;
    Contract.mainView mainView;
    private StorageTask mUploadTask;

    public Presenter(Contract.mainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void upload2Firebase(String category, Uri imageUri) {
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads/"+ FirebaseAuth.getInstance().getUid());
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads/"+ FirebaseAuth.getInstance().getUid());

        if (mUploadTask != null && mUploadTask.isInProgress())
            mainView.showToast("Upload is in Progress");
        else{
            if (imageUri != null) {
                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                        + "." + mainView.getFileExtension(imageUri));

                mUploadTask = fileReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mainView.setProgressBarProgress(0);
                                    }
                                }, 500);
                                mainView.showToast("Upload successful");

                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();
                                        Upload upload = new Upload(category,url);
                                        String uploadId = mDatabaseRef.push().getKey();
                                        mDatabaseRef.child(uploadId).setValue(upload);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mainView.showToast(e.getMessage());
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                mainView.setProgressBarProgress((int) progress);
                            }
                        });
            } else {
                mainView.showToast("No File Selected");
            }
        }
    }
}
