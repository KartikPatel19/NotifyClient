package com.deucate.kartik.notifyclient;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String link = intent.getStringExtra("Link");
        Uri uri = Uri.parse(link);
        Intent intent1 = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent1);
        finish();

        setContentView(R.layout.activity_temp);

    }
}
