package com.example.artstlens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.example.artstlens.Fragments.CycleganFragment.CycleganFragment;
import com.example.artstlens.Fragments.DrawingSheet.DrawingSheet;
import com.example.artstlens.Fragments.HomeFragment.HomeFragment;
import com.example.artstlens.Fragments.PixToPixFragment.Pix2PixFragment.Pix2PixFragment;
import com.example.artstlens.Fragments.ProfileFragment.ProfileFragment;
import com.example.artstlens.LoginActivity.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("MainActivity","created");
        Intent intent = getIntent();
        int check = intent.getIntExtra("check",1);
        switch (check){
            case 2 : StartFragment(new Pix2PixFragment());
            break;
            case 3 : StartFragment(new CycleganFragment());
            break;
            default: StartFragment(new HomeFragment());
        }

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            StartFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.home);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ActivityResultMain1","entered");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.profile);
        fragment.onActivityResult(requestCode, resultCode, data);
        Log.d("ActivityResultMain2","entered");
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                StartFragment( new HomeFragment());
                break;
            case R.id.profile:
                StartFragment(new ProfileFragment());
                break;
            case R.id.drawing:
                StartFragment(new DrawingSheet());
                break;
            case R.id.pix2pix:
                StartFragment(new Pix2PixFragment());
                break;
            case R.id.cyclegan:
                StartFragment(new CycleganFragment());
                break;
            case R.id.signOut :
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Do you want to LOGOUT?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoge, int id) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                builder.create().show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void StartFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
}
