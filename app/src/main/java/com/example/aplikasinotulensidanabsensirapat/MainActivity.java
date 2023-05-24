package com.example.aplikasinotulensidanabsensirapat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Cancelled scan");
                        FancyToast.makeText(MainActivity.this, "Cancelled", FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
                    } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        FancyToast.makeText(MainActivity.this, "Cancelled due to missing camera permission", FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    }
                } else {
                    Log.d("MainActivity", "Scanned");
                    //FancyToast.makeText(MainActivity.this, "Scanned: " + result.getContents(), FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                    FancyToast.makeText(MainActivity.this, "Berhasil scan", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                    absenscan(result.getContents(),"user","user");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_user);
    }
    public void Menubaruser(View view){
        opeDrawer(drawerLayout);
    }
    private void opeDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    private void closDrawer(DrawerLayout drawerLayout){
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    public void home(View view){
        closDrawer(drawerLayout);
    }
    public void Logout(View view){
        Intent intent = new Intent(MainActivity.this,Login.class);
        startActivity(intent);
    }
    private void showMessagesucces(String msg) {
        FancyToast.makeText(this, msg, FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
    }
    private void showMessage(String msg) {
        FancyToast.makeText(this, msg, FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
    }
    public void Keluar(View view){
        keluaraplikasi(MainActivity.this);
    }
    public void keluaraplikasi(MainActivity mainActivity){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Keluar");
        builder.setMessage("Apakah anda yakin akan keluar ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
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
    public void scanToolbar(View view) {
        ScanOptions options = new ScanOptions().setCaptureActivity(ToolbarCaptureActivity.class);
        barcodeLauncher.launch(options);
    }
    private void absenscan(final String id_rapat,final String username,final String nama ) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.scanabsen, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    showMessagesucces("Absen berhasil");
                }
                if (response.equals("sudah absen")) {
                    showMessage("Sudah absen");
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
                params.put("username",username);
                params.put("nama",nama);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}