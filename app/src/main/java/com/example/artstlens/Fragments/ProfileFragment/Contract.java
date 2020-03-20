package com.example.artstlens.Fragments.ProfileFragment;

import android.net.Uri;

public class Contract {
    interface Presenter{

        void SaveChanges(String name);

        void uploadProfilePic(Uri imageUri);

        void downloadProfilePic();
    }
    interface mainView{
        void setProfilePic(String url);
        void setProgressbar(int visible);

        void Toast(String profile_updated_successfully);
    }
}
