package com.example.artstlens.UploadPhoto;

import android.net.Uri;

public class Contract {
    interface mainView{
        void availableMode(int a);

        void showToast(String upload_is_in_progress);

        String getFileExtension(Uri imageUri);

        void setProgressBarProgress(int i);
    }
    interface Presenter{

        void upload2Firebase(String category, Uri imageUri);
    }
}
