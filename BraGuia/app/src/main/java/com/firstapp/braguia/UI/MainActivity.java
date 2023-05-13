package com.firstapp.braguia.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.firstapp.braguia.Model.Api;
import com.firstapp.braguia.R;

public class MainActivity extends AppCompatActivity  {
        @Override
        protected void onCreate(Bundle savedInstanceState) {

            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            getWindow().setNavigationBarColor(getResources().getColor(R.color.our_black));


        }

    }
