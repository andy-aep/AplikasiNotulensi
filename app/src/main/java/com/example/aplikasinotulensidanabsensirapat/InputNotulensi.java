package com.example.aplikasinotulensidanabsensirapat;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class InputNotulensi extends AppCompatActivity {
    private EditText etPembahasan,etidrapat,etidnotulensi;
    private Button btninputnotulensi,btnpilihgambar;
    private String encodeImageString;
    private Bitmap bitmap;
    private ImageView gmbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_notulensi);
        etPembahasan = findViewById(R.id.editTextTextCatatan);
        etidrapat = findViewById(R.id.editTextTextidrapat);
        etidnotulensi = findViewById(R.id.editTextTextidnotulensi);
        btninputnotulensi = findViewById(R.id.btn_input_notulensi);
        gmbr = findViewById(R.id.gmbr);
        btnpilihgambar = findViewById(R.id.btnpilihgambar);
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Uri filepath = result.getData().getData();
                    try
                    {
                        InputStream inputStream=getContentResolver().openInputStream(filepath);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        //gmbr.setImageBitmap(bitmap);
                        Glide.with(InputNotulensi.this).load(bitmap).into(gmbr);
                        encodeBitmapString(bitmap);
                    }catch (Exception ex){

                    }
                }
            }
        });
        btnpilihgambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                someActivityResultLauncher.launch(Intent.createChooser(intent,"Pilih gambar"));
            }
        });
        if(getIntent().getExtras() != null){
            //btninputnotulensi.setText(getIntent().getExtras().getString("id_notulensi"));
            etidrapat.setText(getIntent().getExtras().getString("id_rapat"));
            etidnotulensi.setText(getIntent().getExtras().getString("id_notulensi"));
            etPembahasan.setText(getIntent().getExtras().getString("pembahasan"));
            Glide.with(this).load(Endpoint.base_url+"gambar/notulen/"+getIntent().getExtras().getString("foto")).placeholder(R.drawable.ic_baseline_image_24).into(gmbr);

            if(etidnotulensi.getText().toString().matches("")){
                btninputnotulensi.setText("Simpan");
            }else{
                btninputnotulensi.setText("Edit");
            }
        }
    }

    private void encodeBitmapString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimages= byteArrayOutputStream.toByteArray();
        encodeImageString= android.util.Base64.encodeToString(bytesofimages, Base64.DEFAULT);
    }

    public void Back(View view){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Keluar");
            builder.setMessage("Apakah anda yakin akan keluar tanpa menyimpan data?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(InputNotulensi.this, NotulensiView.class);
                    intent.putExtra("id_rapat",getIntent().getExtras().getString("id_rapat"));
                    intent.putExtra("nama_rapat",getIntent().getExtras().getString("nama_rapat"));
                    intent.putExtra("nama_pinpinan",getIntent().getExtras().getString("nama_pinpinan"));
                    intent.putExtra("tanggal",getIntent().getExtras().getString("tanggal"));
                    intent.putExtra("waktu_mulai",getIntent().getExtras().getString("waktu_mulai"));
                    intent.putExtra("waktu_selesai",getIntent().getExtras().getString("waktu_selesai"));
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
        String id_rapat,id_notulensi,pembahasan,foto;
        id_rapat = etidrapat.getText().toString();
        id_notulensi = etidnotulensi.getText().toString();
        pembahasan = etPembahasan.getText().toString();

        //simpan(id_rapat,pembahasan);
        if(id_notulensi.matches("")){
            simpan(id_rapat,pembahasan,encodeImageString);
        }else {
            if(encodeImageString==null){
                foto = getIntent().getExtras().getString("foto");
                editrapat(id_rapat,id_notulensi,pembahasan,foto);
            }else {
                foto = encodeImageString;
                editrapat(id_rapat,id_notulensi,pembahasan,foto);
            }
        }
        Intent intent = new Intent(this, RapatView.class);
        startActivity(intent);
    }

    private void editrapat(String id_rapat, String id_notulensi, String pembahasan, String foto) {
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
                params.put("foto",foto);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void simpan(String id_rapat, String pembahasan,String foto) {
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
                params.put("foto",foto);
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