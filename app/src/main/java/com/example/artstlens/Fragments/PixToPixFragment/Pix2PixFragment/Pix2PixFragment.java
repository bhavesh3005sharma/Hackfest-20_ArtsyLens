package com.example.artstlens.Fragments.PixToPixFragment.Pix2PixFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.artstlens.Fragments.Upload;
import com.example.artstlens.R;
import com.example.artstlens.ShowConvertedImg;
import com.example.artstlens.ShowSelectedPic;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Pix2PixFragment extends Fragment implements Contract.mainView, AdapterRV.OnItemClickListener{
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.message)
    TextView message;
    AdapterRV adapterRV;
    Contract.presenter presenter;
    @BindView(R.id.progressBar4)
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.pics2pics_fragment,container,false);
        ButterKnife.bind(this,view);
        getActivity().setTitle("Pics To Pics Collection");
        presenter = new Presenter(Pix2PixFragment.this);

        progressBar.setVisibility(View.VISIBLE);
        presenter.getList();
        return view;
    }

    @Override
    public void setRecyclerView(ArrayList<Upload> list) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterRV = new AdapterRV(list,getContext());
        recyclerView.setAdapter(adapterRV);
        adapterRV.setOnItemClickListener(Pix2PixFragment.this);
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
        intent.putExtra("check",2);
        startActivity(intent);
    }

    @Override
    public void convert(int position) {
        Intent intent = new Intent(getContext(), ShowConvertedImg.class);
        intent.putExtra("urlString",presenter.getUrlStr(position));
        intent.putExtra("check",2);
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
