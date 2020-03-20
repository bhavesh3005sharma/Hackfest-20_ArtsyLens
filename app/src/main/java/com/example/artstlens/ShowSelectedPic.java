package com.example.artstlens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class ShowSelectedPic extends AppCompatActivity {
    String url;
    int check;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Your Selected Image");
        setContentView(R.layout.activity_show_selected_pic);
        Intent intent = getIntent();
        url = intent.getStringExtra("urlString");
        check = intent.getIntExtra("check",1);

        img =  findViewById(R.id.shw_selectedImg);
        Picasso.get().load(Uri.parse(url)).placeholder(R.mipmap.ic_launcher).into(img);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShowSelectedPic.this, MainActivity.class);
        intent.putExtra("check",check);
        startActivity(intent);
        //super.onBackPressed();
    }
}

