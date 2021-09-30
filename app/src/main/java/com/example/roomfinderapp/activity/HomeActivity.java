package com.example.roomfinderapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomfinderapp.R;
import com.example.roomfinderapp.adapter_classes.AdapterSideMenu;
import com.example.roomfinderapp.fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    ImageView imageView;
    RecyclerView rvSideMenuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setHomeFragmet();
        imageView = findViewById(R.id.iv_humberger_image_view);
        imageView.setVisibility(View.VISIBLE);
        ImageView backArrow = findViewById(R.id.iv_back_arrow);
        backArrow.setVisibility(View.GONE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNaviGationDrawer();
            }
        });

        setUpNavigationDrawer();

    }

    private void setHomeFragmet() {

        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, homeFragment);
        fragmentTransaction.commit();
    }

    private void openNaviGationDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void setUpNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        rvSideMenuList = findViewById(R.id.rv_nav_menu);
        rvSideMenuList.setLayoutManager(new LinearLayoutManager(this));
        List<String> arrayList = new ArrayList<>();
        arrayList.add("About US");
        arrayList.add("Settings");
        arrayList.add("Contact US");
        AdapterSideMenu sideMenuAdapter = new AdapterSideMenu(arrayList);
        rvSideMenuList.setAdapter(sideMenuAdapter);
        navigationView = findViewById(R.id.slide_menu_navigation);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeNavigationDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void closeNavigationDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        }
    }
}
