package com.example.aplikasinotulensidanabsensirapat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.Map;

public class InputUser extends AppCompatActivity {
    private EditText etuser,etpassword,etnamauser,etprodi,etkelas,etemail;
    private CheckBox cbrole;
    private Button btnsimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_user);
        etuser = findViewById(R.id.editTextTextPersonName);
        etpassword = findViewById(R.id.editTextTextPassword);
        etnamauser = findViewById(R.id.editTextTextName);
        etprodi = findViewById(R.id.editTextTextProdi);
        etkelas = findViewById(R.id.editTextTextKelas);
        etemail = findViewById(R.id.editTextTextEmail);
        cbrole = findViewById(R.id.cb_role);
        btnsimpan = findViewById(R.id.btnsimpanuser);
        if(getIntent().getExtras() != null){
            btnsimpan.setText("Edit");
            etuser.setText(getIntent().getExtras().getString("username"));
            etpassword.setText(getIntent().getExtras().getString("password"));
            etnamauser.setText(getIntent().getExtras().getString("nama"));
            etprodi.setText(getIntent().getExtras().getString("prodi"));
            etkelas.setText(getIntent().getExtras().getString("kelas"));
            etemail.setText(getIntent().getExtras().getString("email"));
            etuser.setEnabled(false);
            if(getIntent().getExtras().getString("role").matches("1")){
                cbrole.setChecked(true);
            }else {
                cbrole.setChecked(false);
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
                Intent intent = new Intent(InputUser.this, UserView.class);
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
    public void BtnSimpanUser(View view){
        String username,password,nama,prodi,kelas,email,role;
        username = etuser.getText().toString();
        password = etpassword.getText().toString();
        nama = etnamauser.getText().toString();
        prodi = etprodi.getText().toString();
        kelas = etkelas.getText().toString();
        email = etemail.getText().toString();
        if(cbrole.isChecked()){
            role = "1";
        }else {
            role = "2";
        }
        if(getIntent().getExtras() != null){
            edituser(username,password,nama,prodi,kelas,email,role);
        }else {
            simpan(username,password,nama,prodi,kelas,email,role);
        }
        Intent intent = new Intent(this, UserView.class);
        startActivity(intent);
    }
    private void edituser(String username, String password, String nama, String prodi, String kelas, String email, String role) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.edituser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    showMessagesucces("Berhasil mengedit User");
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
                params.put("username",username);
                params.put("password",password);
                params.put("nama",nama);
                params.put("prodi",prodi);
                params.put("kelas",kelas);
                params.put("email",email);
                params.put("role",role);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void simpan(String username, String password, String nama, String prodi, String kelas, String email, String role) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.adduser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    showMessagesucces("Berhasil menambah User");
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
                params.put("username",username);
                params.put("password",password);
                params.put("nama",nama);
                params.put("prodi",prodi);
                params.put("kelas",kelas);
                params.put("email",email);
                params.put("role",role);
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