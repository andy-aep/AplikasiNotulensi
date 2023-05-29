package com.example.aplikasinotulensidanabsensirapat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    String stn;
    EditText etname, etpassword;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession",MODE_PRIVATE);
        if(sharedPreferences.getString("Username",null) != null)
        {
            if(sharedPreferences.getString("Role",null).matches("2")) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(Login.this, MenuAdmin.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etname = findViewById(R.id.editTextTextPersonName);
        etpassword = findViewById(R.id.editTextTextPassword);
    }

    public void login(View view){
        String name = etname.getText().toString();
        String pass = etpassword.getText().toString();

        if(benar(name,pass)){
            signin(name,pass);
        }

    }

    private boolean benar(String name, String pass) {
        if(name.isEmpty()){
            showMessage("Username belum di isi");
            etname.setText(null);
            return false;
        }
        if (pass.isEmpty()){
            showMessage("Password belum di isi");
            etname.setText(null);
            return false;
        }
        return true;
    }
    private void  Simpansession(String uname, String nama , String prodi , String kelas , String email , String foto, String role){
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username",uname);
        editor.putString("Name",nama);
        editor.putString("Prodi",prodi);
        editor.putString("Kelas",kelas);
        editor.putString("Email",email);
        editor.putString("Foto",foto);
        editor.putString("Role",role);
        editor.apply();
        //showMessagesucces("sesion  tersimpan");
    }
    private void showMessagesucces(String msg) {
        FancyToast.makeText(this, msg, FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
    }
    private void showMessage(String msg) {
        FancyToast.makeText(this, msg, FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
    }

    private void signin(final String name,final String pass) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                stn = etname.getText().toString();
                if (response.equals("success1")) {
                    Intent intent = new Intent(Login.this, MenuAdmin.class);
                    //intent.putExtra("username", stn);
                    Simpansession(stn,"admin","admin","admin","admin@gmail.com","http://192.168.1.4/api_app/gambar/avatar/","1");
                    showMessagesucces("Login Berhasil");
                    startActivity(intent);
                    etname.setText(null);
                    etpassword.setText(null);
                }
                if (response.equals("success2")) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    //intent.putExtra("username", stn);
                    Simpansession(stn,"user","user","user","user@gmail.com","http://192.168.1.4/api_app/gambar/avatar/","2");
                    showMessagesucces("Login Berhasil");
                    startActivity(intent);
                    etname.setText(null);
                    etpassword.setText(null);
                }
                if (response.equals("fail")) {
                    Intent intent = new Intent(Login.this, Login.class);
                    intent.putExtra("username", stn);
                    showMessage("username atau password salah");
                    startActivity(intent);
                    etname.setText(null);
                    etpassword.setText(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage("gagal terhubung ke internet");
                Log.d("VOLLEY",error.getMessage());
                Intent intent = new Intent(Login.this,Login.class);
                startActivity(intent);
            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("u_name",name);
                params.put("pass",pass);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}