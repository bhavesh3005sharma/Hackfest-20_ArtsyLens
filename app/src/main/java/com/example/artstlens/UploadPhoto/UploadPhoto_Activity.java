package com.example.artstlens.UploadPhoto;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.artstlens.MainActivity;
import com.example.artstlens.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadPhoto_Activity extends AppCompatActivity implements Contract.mainView, View.OnClickListener {

    @BindView(R.id.selectedImage)
    ImageView imageView;
    @BindView(R.id.btn_choose)
    Button choose;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    TextView gallery;
    TextView camera;
    TextView cancel;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;
    Uri imageUri;
    Contract.Presenter presenter;
    String category;
    int position;
    AlertDialog alertDialogue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadphoto);
        setTitle("Upload A Photo");
        ButterKnife.bind(this);
        presenter =new Presenter(UploadPhoto_Activity.this);

         btnUpload.setOnClickListener(this);
         choose.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UploadPhoto_Activity.this,MainActivity.class));
    }

    public void availableMode(int a) {
        switch (a){
            case 1 :
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
                break;
            case 2 :
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 2);
                }
                break;
        }

    }

    @Override
    public void showToast(String s) {
        Toast.makeText(UploadPhoto_Activity.this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        alertDialogue.dismiss();
        if (resultCode == RESULT_OK && data != null) {
            if(requestCode == 1 && data.getData()!=null) {
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(imageView);
            }else if(requestCode == 2 && data.getExtras()!=null){
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(imageBitmap);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.gallery:
                availableMode(1);
                break;
            case R.id.camera:
                availableMode(2);
                break;
            case R.id.cancel:
                alertDialogue.dismiss();
                break;
            case R.id.btn_upload :
                    category = spinner.getSelectedItem().toString();
                    position = spinner.getSelectedItemPosition();
                if(position == 0){
                    showToast("Category not Selected");
                }else
                presenter.upload2Firebase(category,imageUri);
                break;
            case R.id.btn_choose :
                openAlertDialogue();
                break;
        }
    }

    private void openAlertDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void setProgressBarProgress(int i) {
        progressBar.setProgress(i);
    }
}
