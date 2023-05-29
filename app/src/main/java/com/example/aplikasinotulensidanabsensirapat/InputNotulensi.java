package com.example.aplikasinotulensidanabsensirapat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.Map;

public class InputNotulensi extends AppCompatActivity {
    private EditText etPembahasan,etidrapat,etidnotulensi;
    private Button btninputnotulensi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_notulensi);
        etPembahasan = findViewById(R.id.editTextTextCatatan);
        etidrapat = findViewById(R.id.editTextTextidrapat);
        etidnotulensi = findViewById(R.id.editTextTextidnotulensi);
        btninputnotulensi = findViewById(R.id.btn_input_notulensi);
        if(getIntent().getExtras() != null){
            //btninputnotulensi.setText(getIntent().getExtras().getString("id_notulensi"));
            etidrapat.setText(getIntent().getExtras().getString("id_rapat"));
            etidnotulensi.setText(getIntent().getExtras().getString("id_notulensi"));
            etPembahasan.setText(getIntent().getExtras().getString("pembahasan"));
            if(etidnotulensi.getText().toString().matches("")){
                btninputnotulensi.setText("Simpan");
            }else{
                btninputnotulensi.setText("Edit");
            }
        }
    }

    public void Back(View view){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Keluar");
            builder.setMessage("Apakah anda yakin akan keluar tanpa menyimpan data?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(InputNotulensi.this, RapatView.class);
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
    public void Simpaninputnotulensi(View view){
        String id_rapat,id_notulensi,pembahasan;
        id_rapat = etidrapat.getText().toString();
        id_notulensi = etidnotulensi.getText().toString();
        pembahasan = etPembahasan.getText().toString();
        //simpan(id_rapat,pembahasan);
        if(id_notulensi.matches("")){
            simpan(id_rapat,pembahasan);
        }else {
            editrapat(id_rapat,id_notulensi,pembahasan);
        }
        Intent intent = new Intent(this, RapatView.class);
        startActivity(intent);
    }

    private void editrapat(String id_rapat, String id_notulensi, String pembahasan) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.editnotulensi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    showMessagesucces("Berhasil mengedit Notulensi");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage("gagal terhubung ke internet");
                Log.d("VOLLEY",error.getMessage());
            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_rapat",id_rapat);
                params.put("id_notulensi",id_notulensi);
                params.put("pembahasan",pembahasan);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void simpan(String id_rapat, String pembahasan) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.simpannotulensi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    showMessagesucces("Berhasil menambah Notulensi");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage("gagal terhubung ke internet");
                Log.d("VOLLEY",error.getMessage());
            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_rapat",id_rapat);
                params.put("pembahasan",pembahasan);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void showMessagesucces(String msg) {
        FancyToast.makeText(this, msg, FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
    }
    private void showMessage(String msg) {
        FancyToast.makeText(this, msg, FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
    }
}