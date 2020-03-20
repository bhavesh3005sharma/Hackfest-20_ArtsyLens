package com.example.artstlens.Fragments.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import androidx.annotation.NonNull;
import com.example.artstlens.Fragments.Upload;
import com.example.artstlens.ShowSelectedPic;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Presenter  implements Contract.presenter{
    Contract.mainView mainView;
    DatabaseReference databaseReference;
    ArrayList<Upload> list;
    FirebaseStorage mstorage;
    ValueEventListener mDbListener;

    public Presenter(Contract.mainView mainView){
        this.mainView=mainView;
    }

    @Override
    public void getList() {
        list = new ArrayList<>();
        mainView.setRecyclerView(list);
        mstorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads/"+ FirebaseAuth.getInstance().getUid());
        mDbListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot groupsnapshot : dataSnapshot.getChildren()){
                    Upload upload = new Upload();
                    upload.setmKey(groupsnapshot.getKey());
                    upload.setUri(groupsnapshot.child("uri").getValue().toString());
                    list.add(upload);
                }
                mainView.updateAdapter();
                mainView.progressBarVisibility(View.GONE);
                if(list.isEmpty())
                    mainView.setText("No File Found in Database");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mainView.progressBarVisibility(View.GONE);
                mainView.showToast(databaseError.getMessage());
            }
        });
        return;
    }

    @Override
    public void deletePhoto(int position) {
        Upload selectedItem = list.get(position);
        final String selectedKey = selectedItem.getmKey();

        StorageReference imageRef = mstorage.getReferenceFromUrl(selectedItem.getUri());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(selectedKey).removeValue();
                mainView.showToast("Photo Deleted");
            }
        });
    }

    @Override
    public void destroyExcessValueEventListener() {
        databaseReference.removeEventListener(mDbListener);
    }

    @Override
    public String getUrlStr(int position) {
        Upload selectedItem = list.get(position);
        final String url = selectedItem.getUri();
        return url;
    }
}
