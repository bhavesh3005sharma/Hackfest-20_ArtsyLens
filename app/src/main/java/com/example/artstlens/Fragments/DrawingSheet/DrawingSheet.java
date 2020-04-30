package com.example.artstlens.Fragments.DrawingSheet;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.artstlens.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawingSheet extends Fragment implements OnClickListener{
    @BindView(R.id.new_btn)
    ImageButton newBtn;
    @BindView(R.id.draw_btn)
    ImageButton brush;
    @BindView(R.id.erase_btn)
    ImageButton erase;
    @BindView(R.id.save_btn)
    ImageButton save;

    PaintViewClass drawView;
    View rootView;
    private int lastColorIdSelected;
    private int lastSelecetdChoice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.drawing_fragment,container,false);
        rootView = view;
        getActivity().setTitle("Drawing Sheet");
        drawView = (PaintViewClass) view.findViewById(R.id.drawing);
        ButterKnife.bind(this,view);
        newBtn.setOnClickListener(this);
        brush.setOnClickListener(this);
        erase.setOnClickListener(this);
        save.setOnClickListener(this);
        lastColorIdSelected = R.id.color1;
        setColorSelected(lastColorIdSelected,lastColorIdSelected);
        lastSelecetdChoice = R.id.draw_btn;
        setChoiceSelected(lastSelecetdChoice,lastSelecetdChoice ,2);
        view.findViewById(R.id.color1).setOnClickListener(this);
        view.findViewById(R.id.color2).setOnClickListener(this);
        view.findViewById(R.id.color3).setOnClickListener(this);
        view.findViewById(R.id.color4).setOnClickListener(this);
        view.findViewById(R.id.color5).setOnClickListener(this);
        view.findViewById(R.id.color6).setOnClickListener(this);
        view.findViewById(R.id.color7).setOnClickListener(this);
        view.findViewById(R.id.color8).setOnClickListener(this);
        view.findViewById(R.id.color9).setOnClickListener(this);
        view.findViewById(R.id.color10).setOnClickListener(this);
        view.findViewById(R.id.color11).setOnClickListener(this);
        view.findViewById(R.id.color12).setOnClickListener(this);
        view.findViewById(R.id.color13).setOnClickListener(this);
        view.findViewById(R.id.color14).setOnClickListener(this);
        drawView.setColor("#6200EE");
        return view;
    }

    private void setChoiceSelected(int lastSelecetdChoice_1, int lastSelecetdChoice_2 ,int a) {
        ImageButton btn1 = rootView.findViewById(lastSelecetdChoice_1);
        ImageButton btn2 = rootView.findViewById(lastSelecetdChoice_2);
        int clr = Color.parseColor("#D9D3D3");
        btn2.setBackgroundColor(clr);
        btn1.setElevation(10);
        btn1.setBackgroundResource(R.drawable.button_background);
        if(a==1)
            btn1.setImageResource(R.drawable.new_drawing);
        else if(a==2)
            btn1.setImageResource(R.drawable.drawing);
        else if(a==3)
            btn1.setImageResource(R.drawable.erase);
        else if(a==4)
            btn1.setImageResource(R.drawable.ic_save_black_24dp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.draw_btn:
                drawView.setupDrawing();
                setChoiceSelected(R.id.draw_btn,lastSelecetdChoice,2);
                lastSelecetdChoice = R.id.draw_btn;
                break;
            case R.id.new_btn:
                setChoiceSelected(R.id.new_btn,lastSelecetdChoice,1);
                lastSelecetdChoice = R.id.new_btn;
                AlertDialog.Builder newDialog = new AlertDialog.Builder(getContext());
                newDialog.setTitle("New drawing");
                newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
                newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        drawView.startNew();
                        dialog.dismiss();
                    }
                });
                newDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                newDialog.show();
               break;
            case R.id.erase_btn:
                setChoiceSelected(R.id.erase_btn,lastSelecetdChoice,3);
                lastSelecetdChoice = R.id.erase_btn;
                drawView.setErase(true);
                drawView.setBrushSize(drawView.getLastBrushSize());
                break;
            case R.id.save_btn :
                setChoiceSelected(R.id.save_btn,lastSelecetdChoice,4);
                lastSelecetdChoice = R.id.save_btn;
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {CheckForRequiredPermissions();}
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(getContext());
                saveDialog.setTitle("Save drawing");
                saveDialog.setMessage("Save drawing to device Gallery?");
                saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        drawView.setDrawingCacheEnabled(true);
                        Log.d("DrawingCache ",""+drawView.getDrawingCache());
                        Uri uri = getImageUri(getContext(),drawView.getDrawingCache());
                        Log.d("uri ",""+uri);
                        if(uri!=null){
                            Toast savedToast = Toast.makeText(getContext(),
                                    "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                            savedToast.show();
                        }
                        else{
                            Toast unsavedToast = Toast.makeText(getContext(),
                                    "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                            unsavedToast.show();
                        }
                        drawView.destroyDrawingCache();

                    }
                });
                saveDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                saveDialog.show();
                break;
            case R.id.color1 :
                drawView.setColor("#6200EE");
                setColorSelected(R.id.color1,lastColorIdSelected);
                lastColorIdSelected = R.id.color1;
                break;
            case R.id.color2:
                drawView.setColor("#03DAC5");
                setColorSelected(R.id.color2,lastColorIdSelected);
                lastColorIdSelected = R.id.color2;
                break;
            case R.id.color3:
                drawView.setColor("#91EC27");
                setColorSelected(R.id.color3,lastColorIdSelected);
                lastColorIdSelected = R.id.color3;
                break;
            case R.id.color4 :
                drawView.setColor("#FFFF6600");
                setColorSelected(R.id.color4,lastColorIdSelected);
                lastColorIdSelected = R.id.color4;
                break;
            case R.id.color5 :
                drawView.setColor("#FFFFCC00");
                setColorSelected(R.id.color5,lastColorIdSelected);
                lastColorIdSelected = R.id.color5;
                break;
            case R.id.color6 :
                drawView.setColor("#FF009900");
                setColorSelected(R.id.color6,lastColorIdSelected);
                lastColorIdSelected = R.id.color6;
                break;
            case R.id.color7 :
                drawView.setColor("#EC2516");
                setColorSelected(R.id.color7,lastColorIdSelected);
                lastColorIdSelected = R.id.color7;
                break;
            case R.id.color8 :
                drawView.setColor("#FF009999");
                setColorSelected(R.id.color8,lastColorIdSelected);
                lastColorIdSelected = R.id.color8;
                break;
            case R.id.color9 :
                drawView.setColor("#FF0000FF");
                setColorSelected(R.id.color9,lastColorIdSelected);
                lastColorIdSelected = R.id.color9;
                break;
            case R.id.color10 :
                drawView.setColor("#FF990099");
                setColorSelected(R.id.color10,lastColorIdSelected);
                lastColorIdSelected = R.id.color10;
                break;
            case R.id.color11:
                drawView.setColor("#E91E63");
                setColorSelected(R.id.color11,lastColorIdSelected);
                lastColorIdSelected = R.id.color11;
                break;
            case R.id.color12:
                drawView.setColor("#FFFFFFFF");
                setColorSelected(R.id.color12,lastColorIdSelected);
                lastColorIdSelected = R.id.color12;
                break;
            case R.id.color13:
                drawView.setColor("#FF787878");
                setColorSelected(R.id.color13,lastColorIdSelected);
                lastColorIdSelected = R.id.color13;
                break;
            case R.id.color14 :
                drawView.setColor("#170202");
                setColorSelected(R.id.color14,lastColorIdSelected);
                lastColorIdSelected = R.id.color14;
                break;
        }
    }

    private void setColorSelected(int clr, int lastColorIdSelected_) {
        ImageButton selectedImg = rootView.findViewById(clr);
        ImageButton unSelectedImg = rootView.findViewById(lastColorIdSelected_);
        unSelectedImg.setImageResource(R.drawable.paint);
        selectedImg.setImageResource(R.drawable.paint_pressed);
        selectedImg.setElevation(10);
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void CheckForRequiredPermissions(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

        TedPermission.with(getContext())
            .setPermissionListener(permissionlistener)
                .setDeniedMessage("Give Storage Permission\nPlease turn on permissions at\n [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
}
