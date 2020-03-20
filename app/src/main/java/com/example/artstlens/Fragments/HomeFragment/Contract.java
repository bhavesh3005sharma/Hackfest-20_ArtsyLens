package com.example.artstlens.Fragments.HomeFragment;

import com.example.artstlens.Fragments.Upload;

import java.util.ArrayList;

public class Contract  {
    public interface presenter{
        void getList();

        void deletePhoto(int a);

        void destroyExcessValueEventListener();

        String getUrlStr(int position);
    }

    public interface mainView{
        void setRecyclerView(ArrayList<Upload> list);

        void progressBarVisibility(int a);

        void showToast(String message);

        void setText(String no_file_found_in_database);

        void updateAdapter();
    }
}
