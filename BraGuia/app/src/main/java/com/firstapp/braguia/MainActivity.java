package com.firstapp.braguia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.our_black));

        Button btn = (Button) findViewById(R.id.trailListButton);
        btn.setOnClickListener(v -> startActivity(new Intent(this, TrailListFragment.class)));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        //button = findViewById(R.id.logout);
        //textView = findViewById(R.id.user_details);
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else textView.setText(user.getEmail());

        //button.setOnClickListener(new View.OnClickListener() {
        //  @Override
        // public void onClick(View view) {
        //   FirebaseAuth.getInstance().signOut();
        //}
        //});
        }
    }