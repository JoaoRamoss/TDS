package com.firstapp.braguia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.our_black));

        Button btn = (Button) findViewById(R.id.trailListButton);
        btn.setOnClickListener(v -> startActivity(new Intent(this, TrailListFragment.class)));
    }
}