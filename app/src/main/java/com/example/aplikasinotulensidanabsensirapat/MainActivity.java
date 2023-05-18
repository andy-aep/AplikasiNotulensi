package com.example.aplikasinotulensidanabsensirapat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.shashank.sony.fancytoastlib.FancyToast;

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
                    FancyToast.makeText(MainActivity.this, "Scanned: " + result.getContents(), FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
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

    public void scanToolbar(View view) {
        ScanOptions options = new ScanOptions().setCaptureActivity(ToolbarCaptureActivity.class);
        barcodeLauncher.launch(options);
    }
}