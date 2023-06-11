package com.example.aplikasinotulensidanabsensirapat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Absensi_menu extends AppCompatActivity {
    String qrcoderapat;
    List<Absensi> listabsensi;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AdapterAbsensi adapterAbsensi;
    Absensi absensi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_menu);

        if(getIntent().getExtras() != null){
            qrcoderapat = getIntent().getExtras().getString("id_rapat");
        }else{
            qrcoderapat = "1";
        }
        ImageView imageView = findViewById(R.id.qr_code);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(qrcoderapat, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        }catch (WriterException e){
            throw new RuntimeException(e);
        }


    }

    private void showMessage(String msg) {
        FancyToast.makeText(this, msg, FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
    }


    public void Back(View view){
        Intent intent = new Intent(this, RapatView.class);
        startActivity(intent);
    }
    public void Detail(View view){
        Intent intent = new Intent(this, AbsensiView.class);
        intent.putExtra("id_rapat",qrcoderapat);
        startActivity(intent);
    }
    public void Batalkan(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("BATALKAN ABSENSI");
        builder.setMessage("Apakah anda yakin akan membatalkan absensi ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.batalkanabsensi, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Intent intent = new Intent(Absensi_menu.this, RapatView.class);
                            //intent.putExtra("username", stn);
                            showMessagesucces("Pembatalan Absensi berhasil");
                            startActivity(intent);
                        }
                        if (response.equals("fail")) {
                            showMessage("Pembatalan Absensi Gagal");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMessage("gagal terhubung ke internet");
                        Log.d("VOLLEY",error.getMessage());
                    }
                }){
                    protected Map<String,String> getParams() throws AuthFailureError{
                        Map<String,String> params = new HashMap<>();
                        params.put("id_rapat",qrcoderapat);
                        return params;
                    }
                };
                VolleySingleton.getInstance(Absensi_menu.this).addToRequestQueue(stringRequest);
                Intent intent = new Intent(Absensi_menu.this, RapatView.class);
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
    public void Ulangi(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ULANGI ABSENSI");
        builder.setMessage("Apakah anda yakin akan mengulangi absensi ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.ulangiabsensi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) {
                        Intent intent = new Intent(Absensi_menu.this, RapatView.class);
                        //intent.putExtra("username", stn);
                        showMessagesucces("Pengulangan Absensi berhasil");
                        startActivity(intent);
                    }
                    if (response.equals("fail")) {
                        showMessage("Pengulangan Absensi Gagal");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showMessage("gagal terhubung ke internet");
                    Log.d("VOLLEY",error.getMessage());
                }
            }){
                protected Map<String,String> getParams() throws AuthFailureError{
                    Map<String,String> params = new HashMap<>();
                    params.put("id_rapat",qrcoderapat);
                    return params;
                }
            };
                VolleySingleton.getInstance(Absensi_menu.this).addToRequestQueue(stringRequest);
                Intent intent = new Intent(Absensi_menu.this, RapatView.class);
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
    private void showMessagesucces(String msg) {
        FancyToast.makeText(this, msg, FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
    }
    public void Tutup(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("TUTUP ABSENSI");
        builder.setMessage("Apakah anda yakin akan menutup absensi ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.tutupabsensi, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Intent intent = new Intent(Absensi_menu.this, RapatView.class);
                            //intent.putExtra("username", stn);
                            showMessagesucces("Penutupan Absensi berhasil");
                            startActivity(intent);
                        }
                        if (response.equals("fail")) {
                            showMessage("Penutupan Absensi Gagal");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMessage("gagal terhubung ke internet");
                        Log.d("VOLLEY",error.getMessage());
                    }
                }){
                    protected Map<String,String> getParams() throws AuthFailureError{
                        Map<String,String> params = new HashMap<>();
                        params.put("id_rapat",qrcoderapat);
                        return params;
                    }
                };
                VolleySingleton.getInstance(Absensi_menu.this).addToRequestQueue(stringRequest);
                Intent intent = new Intent(Absensi_menu.this, RapatView.class);
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
}