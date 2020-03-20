package com.example.artstlens.Fragments.HomeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artstlens.Fragments.AdapterRV;
import com.example.artstlens.R;
import com.example.artstlens.Fragments.Upload;
import com.example.artstlens.ShowConvertedImg;
import com.example.artstlens.ShowSelectedPic;
import com.example.artstlens.UploadPhoto.UploadPhoto_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements Contract.mainView ,AdapterRV.OnItemClickListener{

    @BindView(R.id.ChoosePhoto)
    FloatingActionButton imgChooser;
    @BindView(R.id.progressBar3)
    ProgressBar progressBar;
    @BindView(R.id.message)
    TextView message;
    Contract.presenter presenter;
    RecyclerView recyclerView;
    AdapterRV adapterRV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Home");

        presenter = new Presenter(HomeFragment.this);
        recyclerView = view.findViewById(R.id.recycler_view);

        imgChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getContext(),UploadPhoto_Activity.class));
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        presenter.getList();
        return view;
    }

    @Override
    public void setRecyclerView(ArrayList<Upload> list) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapterRV = new AdapterRV(list,getContext());
        recyclerView.setAdapter(adapterRV);
        adapterRV.setOnItemClickListener(HomeFragment.this);
    }

    @Override
    public void progressBarVisibility(int a) {
        progressBar.setVisibility(a);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setText(String s) {
        message.setText(s);
        message.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateAdapter() {
        adapterRV.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ShowSelectedPic.class);
        intent.putExtra("urlString",presenter.getUrlStr(position));
        startActivity(intent);
    }

    @Override
    public void convert(int position) {
        Intent intent = new Intent(getContext(), ShowConvertedImg.class);
        intent.putExtra("urlString",presenter.getUrlStr(position));
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroyExcessValueEventListener();
    }

    @Override
    public void onDeleteClick(int position) {
        presenter.deletePhoto(position);
    }
}



