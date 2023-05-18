package com.example.aplikasinotulensidanabsensirapat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuAdmin extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        drawerLayout = findViewById(R.id.drawer_admin);
    }

    public void Menubaradmin(View view){
        opeDrawer(drawerLayout);
    }

    private void opeDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    private void closDrawer(DrawerLayout drawerLayout){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void Biodata(View view){
        closDrawer(drawerLayout);
    }
    public void Keluar(View view){
    }

    public void Logout(View view){
        Intent intent = new Intent(MenuAdmin.this,Login.class);
        startActivity(intent);
    }
}