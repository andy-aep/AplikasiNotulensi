package com.example.aplikasinotulensidanabsensirapat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RapatDetail extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapat_detail);

    }
    public void Back(View view){
        Intent intent = new Intent(this,RapatView.class);
        startActivity(intent);
    }
    public void Editbtn(View view){
        Intent intent = new Intent(this,InputRapat.class);
        intent.putExtra("id_rapat",getIntent().getExtras().getString("id_rapat"));
        intent.putExtra("nama_rapat",getIntent().getExtras().getString("nama_rapat"));
        intent.putExtra("nama_pinpinan",getIntent().getExtras().getString("nama_pinpinan"));
        intent.putExtra("tanggal",getIntent().getExtras().getString("tanggal"));
        intent.putExtra("waktu_mulai",getIntent().getExtras().getString("waktu_mulai"));
        intent.putExtra("waktu_selesai",getIntent().getExtras().getString("waktu_selesai"));
        startActivity(intent);
    }
    public void Absensi(View view){
        Intent intent = new Intent(this, Absensi_menu.class);
        intent.putExtra("id_rapat",getIntent().getExtras().getString("id_rapat"));
        startActivity(intent);
    }
    public void Notulensi(View view){
        Intent intent = new Intent(this, NotulensiView.class);
        intent.putExtra("id_rapat",getIntent().getExtras().getString("id_rapat"));
        startActivity(intent);
    }
}