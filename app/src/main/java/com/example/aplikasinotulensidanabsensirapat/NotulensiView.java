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

public class NotulensiView extends AppCompatActivity {
    List<Notulensi> listnotulensi;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AdapterNotulensi adapterNotulensi;
    Notulensi notulensi;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notulensi);
        recyclerView = findViewById(R.id.rcv_notulensi);
        if(getIntent().getExtras() != null){
            id = getIntent().getExtras().getString("id_rapat");
        }
        loaddata(id);
    }

    private void loaddata(String id_rapat) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.getallnotulensi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listnotulensi = new ArrayList<>();
                try {

                    //showMessage("berhasil");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i=0; i<jsonArray.length();i++){
                        notulensi    = new Notulensi();
                        JSONObject data = jsonArray.getJSONObject(i);
                        notulensi.setId_rapat(data.getString("id_rapat"));
                        notulensi.setPembahasan(data.getString("pembahasan"));
                        notulensi.setId_notulensi(data.getString("id_notulensi"));
                        notulensi.setFoto(data.getString("foto"));
                        listnotulensi.add(notulensi);
                        linearLayoutManager = new  LinearLayoutManager(NotulensiView.this, LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        adapterNotulensi = new AdapterNotulensi(NotulensiView.this, listnotulensi , new AdapterNotulensi.ItemClickListener() {
                            @Override
                            public void onItemClick(Notulensi notulensi) {
                                Intent intent = new Intent(NotulensiView.this,InputNotulensi.class);
                                //showMessage(rapat.getWaktu_mulai().substring(0,10));
                                intent.putExtra("id_rapat",notulensi.getId_rapat());
                                intent.putExtra("pembahasan",notulensi.getPembahasan());
                                intent.putExtra("id_notulensi",notulensi.getId_notulensi());
                                intent.putExtra("foto",notulensi.getFoto());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapterNotulensi);
                        adapterNotulensi.notifyDataSetChanged();
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
    public void Tambahbtn(View view){
        Intent intent = new Intent(this, InputNotulensi.class);
        intent.putExtra("id_rapat",id);
        startActivity(intent);
    }
}