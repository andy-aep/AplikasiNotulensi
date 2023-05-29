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

public class UserView extends AppCompatActivity {
    List<User> listuser;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AdapterUser adapterUser;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        recyclerView = findViewById(R.id.rcv_user);
        loaddata();
    }

    private void loaddata() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.getalluser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listuser = new ArrayList<>();
                try {

                    //showMessage("berhasil");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i=0; i<jsonArray.length();i++){
                        user = new User();
                        JSONObject data = jsonArray.getJSONObject(i);
                        user.setUsername(data.getString("username"));
                        user.setPassword(data.getString("password"));
                        user.setNama(data.getString("nama"));
                        user.setRole(data.getString("role"));
                        user.setProdi(data.getString("prodi"));
                        user.setKelas(data.getString("kelas"));
                        user.setEmail(data.getString("email"));
                        listuser.add(user);
                        linearLayoutManager = new  LinearLayoutManager(UserView.this, LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        adapterUser = new AdapterUser(UserView.this, listuser, new AdapterUser.ItemClickListener() {
                            @Override
                            public void onItemClick(User user) {
                                Intent intent = new Intent(UserView.this,InputUser.class);
                                intent.putExtra("username",user.getUsername());
                                intent.putExtra("password",user.getPassword());
                                intent.putExtra("nama",user.getNama());
                                intent.putExtra("role",user.getRole());
                                intent.putExtra("prodi",user.getProdi());
                                intent.putExtra("kelas",user.getKelas());
                                intent.putExtra("email",user.getEmail());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapterUser);
                        adapterUser.notifyDataSetChanged();
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

    public void Back(View view){
        Intent intent = new Intent(this,MenuAdmin.class);
        startActivity(intent);
    }
    public void Tambahbtn(View view){
        Intent intent = new Intent(this, InputUser.class);
        startActivity(intent);
    }
    private void showMessage(String msg) {
        FancyToast.makeText(this, msg, FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
    }
}