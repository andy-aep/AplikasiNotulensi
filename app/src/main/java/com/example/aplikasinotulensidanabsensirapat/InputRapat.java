package com.example.aplikasinotulensidanabsensirapat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InputRapat extends AppCompatActivity {
    private EditText ettgl,etwktmulai,etwktsls,etNameR,etPrapat,etid_rapat;
    private String id;
    private Button btninputrapat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_rapat);
        //id = getIntent().getExtras().getString("id_rapat");
        ettgl = findViewById(R.id.editTextTextTGL);
        etNameR = findViewById(R.id.editTextTextNameR);
        etwktmulai = findViewById(R.id.editTextTextWktMulai);
        etwktsls = findViewById(R.id.editTextTextWktSelesai);
        etPrapat = findViewById(R.id.editTextTextPRapat);
        etid_rapat = findViewById(R.id.editTextTextid_rapat);
        btninputrapat = findViewById(R.id.btn_input_rapat);
        if(getIntent().getExtras() != null){
            btninputrapat.setText("Edit");
            etid_rapat.setText(getIntent().getExtras().getString("id_rapat"));
            etPrapat.setText(getIntent().getExtras().getString("nama_pinpinan"));
            etNameR.setText(getIntent().getExtras().getString("nama_rapat"));
            ettgl.setText(getIntent().getExtras().getString("tanggal"));
            etwktmulai.setText(getIntent().getExtras().getString("waktu_mulai"));
            etwktsls.setText(getIntent().getExtras().getString("waktu_selesai"));
        }

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int mount = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        ettgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(InputRapat.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = mount+1;
                        String date = year+"-"+month+"-"+dayOfMonth;
                        ettgl.setText(date);
                    }
                },year, mount, day );
                dialog.show();
            }
        });
        etwktmulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(InputRapat.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay+":"+minute+":00";
                        etwktmulai.setText(time);
                    }
                },0,0,true);
                dialog.show();
            }
        });
        etwktsls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(InputRapat.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay+":"+minute+":00";
                        etwktsls.setText(time);
                    }
                },0,0,true);
                dialog.show();
            }
        });
    }
    public void Back(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Keluar");
        builder.setMessage("Apakah anda yakin akan keluar tanpa menyimpan data?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(InputRapat.this, RapatView.class);
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
    public void BtnSimpan(View view){
        String id_rapat,nama_pinpinan,nama_rapat,tanggal,waktu_mulai,waktu_selesai;
        id_rapat = etid_rapat.getText().toString();
        nama_pinpinan = etPrapat.getText().toString();
        nama_rapat = etNameR.getText().toString();
        tanggal = ettgl.getText().toString();
        waktu_mulai = etwktmulai.getText().toString();
        waktu_selesai = etwktsls.getText().toString();
        //simpan(nama_rapat,nama_pinpinan,tanggal,waktu_mulai,waktu_selesai);
        if(getIntent().getExtras() != null){
            editrapat(id_rapat,nama_rapat,nama_pinpinan,tanggal,waktu_mulai,waktu_selesai);
        }else {
            simpan(nama_rapat,nama_pinpinan,tanggal,waktu_mulai,waktu_selesai);
        }
        Intent intent = new Intent(this, RapatView.class);
        startActivity(intent);
    }
    private void editrapat(String id_rapat,String nama_rapat, String nama_pinpinan, String tanggal, String waktu_mulai, String waktu_selesai) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.editrapat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    showMessagesucces("Berhasil mengedit rapat");
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
                params.put("nama_rapat",nama_rapat);
                params.put("nama_pinpinan",nama_pinpinan);
                params.put("tanggal",tanggal);
                params.put("waktu_mulai",waktu_mulai);
                params.put("waktu_selesai",waktu_selesai);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void simpan(String nama_rapat, String nama_pinpinan, String tanggal, String waktu_mulai, String waktu_selesai) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.simpanrapat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    showMessagesucces("Berhasil menyimpan rapat");
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
                params.put("nama_rapat",nama_rapat);
                params.put("nama_pinpinan",nama_pinpinan);
                params.put("tanggal",tanggal);
                params.put("waktu_mulai",waktu_mulai);
                params.put("waktu_selesai",waktu_selesai);
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