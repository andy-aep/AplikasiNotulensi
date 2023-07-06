package com.example.aplikasinotulensidanabsensirapat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NotulensiView extends AppCompatActivity {
    List<Notulensi> listnotulensi;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AdapterNotulensi adapterNotulensi;
    Notulensi notulensi;
    private Bitmap bitmap;
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

    public void Cetak(View view) throws FileNotFoundException {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.getallnotulensi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Random random = new Random();
                    String pdfPath = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS.toString()));
                    File file = new File(pdfPath,getIntent().getExtras().getString("id_rapat")+"_"+getIntent().getExtras().getString("nama_rapat")+"_"+random.nextInt(1000)+".pdf");
                    OutputStream outputStream = new FileOutputStream(file);

                    PdfWriter writer = new PdfWriter(file);
                    PdfDocument pdfDocument = new PdfDocument(writer);
                    Document document = new Document(pdfDocument);
                    pdfDocument.setDefaultPageSize(PageSize.A4);
                    Paragraph judul = new Paragraph(("Notulensi Rapat "+ getIntent().getExtras().getString("nama_rapat")).toUpperCase()).setFontSize(20).setBold().setTextAlignment(TextAlignment.CENTER);
                    document.add(judul);
                    Paragraph spasi = new Paragraph("").setFontSize(12);
                    document.add(spasi);
                    float col = 50f;
                    float col2 = 500f;
                    float lebarkolom[] = {col,col2};
                    Table tableinfo = new Table(lebarkolom);
                    tableinfo.addCell(new Cell().add(new Paragraph("No").setTextAlignment(TextAlignment.CENTER).setBold()));
                    tableinfo.addCell(new Cell().add(new Paragraph("Notulensi").setTextAlignment(TextAlignment.CENTER).setBold()));
                    //showMessage("berhasil");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        String no = Integer.toString(i+1);
                        tableinfo.addCell(new Cell().add(new Paragraph(no).setTextAlignment(TextAlignment.CENTER)));
                        tableinfo.addCell(new Cell().add(new Paragraph(data.getString("pembahasan")).setTextAlignment(TextAlignment.LEFT)));
                    }
                    document.add(tableinfo);
                    document.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
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
                params.put("id_rapat",id);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



        Toast.makeText(NotulensiView.this,"Print Pdf Berhasil Dibuat",Toast.LENGTH_SHORT).show();
    }


    public void Back(View view){
        Intent intent = new Intent(this, RapatDetail.class);
        intent.putExtra("id_rapat",getIntent().getExtras().getString("id_rapat"));
        intent.putExtra("nama_rapat",getIntent().getExtras().getString("nama_rapat"));
        intent.putExtra("nama_pinpinan",getIntent().getExtras().getString("nama_pinpinan"));
        intent.putExtra("tanggal",getIntent().getExtras().getString("tanggal"));
        intent.putExtra("waktu_mulai",getIntent().getExtras().getString("waktu_mulai"));
        intent.putExtra("waktu_selesai",getIntent().getExtras().getString("waktu_selesai"));
        startActivity(intent);
    }
    public void Tambahbtn(View view){
        Intent intent = new Intent(this, InputNotulensi.class);
        intent.putExtra("id_rapat",id);
        intent.putExtra("nama_rapat",getIntent().getExtras().getString("nama_rapat"));
        intent.putExtra("nama_pinpinan",getIntent().getExtras().getString("nama_pinpinan"));
        intent.putExtra("tanggal",getIntent().getExtras().getString("tanggal"));
        intent.putExtra("waktu_mulai",getIntent().getExtras().getString("waktu_mulai"));
        intent.putExtra("waktu_selesai",getIntent().getExtras().getString("waktu_selesai"));
        startActivity(intent);
    }
}