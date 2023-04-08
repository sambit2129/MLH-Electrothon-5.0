package com.example.file_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton storageBtn = findViewById(R.id.storage_btn);

        storageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()){
                    //permission allowed
                    Intent intent = new Intent(MainActivity.this,FileListActivity.class);
                    String path = Environment.getExternalStorageDirectory().getPath();
                    startActivity(intent);
                }else {
                    //permission not allowed
                    requestPermission();
                }
            }
        });
    }
    private boolean checkPermission(){
       int result =  ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
   if (result == PackageManager.PERMISSION_GRANTED){
       return true;
   }else
       return false;
    }
    @SuppressLint("SuspiciousIndentation")
    private void requestPermission(){

        if( ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this,"Storage Permission is Required, please allow from settings",Toast.LENGTH_SHORT).show();
        }else
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},111);

    }
}