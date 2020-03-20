package com.example.artstlens.Fragments.ProfileFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.artstlens.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment implements Contract.mainView, View.OnClickListener {

    FirebaseUser  currentUser = FirebaseAuth.getInstance().getCurrentUser();

    Contract.Presenter presenter;
    @BindView(R.id.textName)
    EditText profile_name;
    @BindView(R.id.textEmail)
    TextView Email;
    @BindView(R.id.textPassword)
    TextView password;
    @BindView(R.id.saveChanges)
    Button save;
    @BindView(R.id.profile_image)
    ImageView profileImg;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    TextView gallery;
    TextView camera;
    TextView cancel;
    AlertDialog alertDialogue;
    Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.profile_fragment,container,false);
        getActivity().setTitle("Profile");
        presenter = new Presenter(ProfileFragment.this);
        ButterKnife.bind(this,view);

        profile_name.setText(currentUser.getDisplayName());
        Email.setText(currentUser.getEmail());
        //progressBar.setVisibility(View.VISIBLE);
        //presenter.downloadProfilePic();
        save.setOnClickListener(this);
        //profileImg.setOnClickListener(this);

        return view;
    }

    @Override
    public void setProfilePic(String url) {
        Picasso.get().load(Uri.parse(url)).placeholder(R.drawable.profile_image_icon).into(profileImg);
    }

    @Override
    public void setProgressbar(int visible) {
        progressBar.setVisibility(visible);
    }

    @Override
    public void Toast(String s) {
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveChanges :
                presenter.SaveChanges(profile_name.getText().toString().trim());
                break;
//            case R.id.profile_image :
//                openAlertDialogue();
//                break;
            case R.id.gallery:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
                break;
            case R.id.camera:
                Intent camera_intent = new Intent(MediaStore
                        .ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent,2);
                break;
            case R.id.cancel:
                alertDialogue.dismiss();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            alertDialogue.dismiss();
            if(requestCode == 1 ) {
                imageUri = data.getData();
                presenter.uploadProfilePic(imageUri);
            }else if(requestCode == 2){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                profileImg.setImageBitmap(photo);
//                imageUri = data.getData();
//                Picasso.get().load(imageUri).into(imageView);
            }
        }
    }

    private void openAlertDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialogue_choose,null));
        alertDialogue = builder.create();
        alertDialogue.show();

        gallery = alertDialogue.findViewById(R.id.gallery);
        camera =  alertDialogue.findViewById(R.id.camera);
        cancel = alertDialogue.findViewById(R.id.cancel);

        gallery.setOnClickListener(this);
        camera.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
}
