package com.example.aplikasinotulensidanabsensirapat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public void Absensi(View view){
        Intent intent = new Intent(MenuAdmin.this, AbsensiView.class);
        startActivity(intent);
    }
    public void User(View view){
        Intent intent = new Intent(MenuAdmin.this, UserView.class);
        startActivity(intent);
    }
    public void Rapat(View view){
        Intent intent = new Intent(MenuAdmin.this, RapatView.class);
        startActivity(intent);
    }
    public void home(View view){
        closDrawer(drawerLayout);
    }


    public void Logout(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Keluar");
        builder.setMessage("Apakah anda yakin akan Logout ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear().apply();
                Intent intent = new Intent(MenuAdmin.this,Login.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void Keluar(View view){
        keluaraplikasi(MenuAdmin.this);
    }
    public void keluaraplikasi(MenuAdmin menuAdmin){
        AlertDialog.Builder builder = new AlertDialog.Builder(menuAdmin);
        builder.setTitle("Keluar");
        builder.setMessage("Apakah anda yakin akan keluar ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                //finish();
                System.exit(1);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}