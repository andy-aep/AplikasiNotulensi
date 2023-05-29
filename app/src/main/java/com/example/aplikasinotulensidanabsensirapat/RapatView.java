package com.example.aplikasinotulensidanabsensirapat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RapatView extends AppCompatActivity {
    List<Rapat> listrapat;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AdapterRapat adapterRapat;
    Rapat rapat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapat);

        recyclerView = findViewById(R.id.rcv_rapat);
        loaddata();
    }

    public void Back(View view){
        Intent intent = new Intent(this,MenuAdmin.class);
        startActivity(intent);
    }
    public void Tambahbtn(View view){
        Intent intent = new Intent(this,InputRapat.class);
        startActivity(intent);
    }
    private void showMessage(String msg) {
        FancyToast.makeText(this, msg, FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
    }
    private void loaddata() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.getallrapat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listrapat = new ArrayList<>();
                try {

                    //showMessage("berhasil");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray  jsonArray = jsonObject.getJSONArray("result");
                    for (int i=0; i<jsonArray.length();i++){
                        rapat = new Rapat();
                        JSONObject data = jsonArray.getJSONObject(i);
                        rapat.setId_rapat(data.getString("id_rapat"));
                        rapat.setNama_rapat(data.getString("nama_rapat"));
                        rapat.setWaktu_mulai(data.getString("waktu_mulai"));
                        //rapat.setWaktu_mulai(data.getString("waktu_mulai").substring(11));
                        rapat.setNama_pinpinan(data.getString("nama_pinpinan"));
                        rapat.setWaktu_selesai(data.getString("waktu_selesai"));
                        listrapat.add(rapat);
                        linearLayoutManager = new  LinearLayoutManager(RapatView.this, LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        adapterRapat = new AdapterRapat(RapatView.this, listrapat, new AdapterRapat.ItemClickListener() {
                            @Override
                            public void onItemClick(Rapat rapat) {
                                Intent intent = new Intent(RapatView.this,RapatDetail.class);
                                //showMessage(rapat.getWaktu_mulai().substring(0,10));
                                intent.putExtra("id_rapat",rapat.getId_rapat());
                                intent.putExtra("nama_rapat",rapat.getNama_rapat());
                                intent.putExtra("nama_pinpinan",rapat.getNama_pinpinan());
                                intent.putExtra("tanggal",rapat.getWaktu_mulai().substring(0,10));
                                intent.putExtra("waktu_mulai",rapat.getWaktu_mulai().substring(11));
                                intent.putExtra("waktu_selesai",rapat.getWaktu_selesai().substring(11));
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapterRapat);
                        adapterRapat.notifyDataSetChanged();
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
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}