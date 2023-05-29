package com.example.aplikasinotulensidanabsensirapat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

public class AbsensiView extends AppCompatActivity {
    String qrcoderapat;
    List<Absensi> listabsensi;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AdapterAbsensi adapterAbsensi;
    Absensi absensi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi);
        recyclerView = findViewById(R.id.rcv_absensi);

        if(getIntent().getExtras() != null){
            qrcoderapat = getIntent().getExtras().getString("id_rapat");
        }else{
            qrcoderapat = "1";
        }
        loaddata(qrcoderapat);
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

    private void loaddata(String id_rapat) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.getallabsensi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listabsensi = new ArrayList<>();
                try {

                    //showMessage("berhasil");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i=0; i<jsonArray.length();i++){
                        absensi = new Absensi();
                        JSONObject data = jsonArray.getJSONObject(i);
                        absensi.setNama(data.getString("nama"));
                        absensi.setNip(data.getString("nip"));
                        absensi.setWaktu(data.getString("waktu"));
                        listabsensi.add(absensi);
                        linearLayoutManager = new  LinearLayoutManager(AbsensiView.this, LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        adapterAbsensi = new AdapterAbsensi(AbsensiView.this, listabsensi);
                        recyclerView.setAdapter(adapterAbsensi);
                        adapterAbsensi.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void showMessage(String msg) {
        FancyToast.makeText(this, msg, FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
    }


    public void Back(View view){
        Intent intent = new Intent(this, RapatView.class);
        startActivity(intent);
    }
}