package com.firstapp.braguia.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.braguia.R;
import com.firstapp.braguia.ViewModel.ViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class Login extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    ProgressBar progressBar;
    TextView textView;
    ViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = (Button) findViewById(R.id.btn_login);
        buttonLogin.setEnabled(true);
        buttonLogin.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.registerNow);
        textView.setEnabled(true);
        textView.setOnClickListener(this);
        viewModel = new ViewModelProvider(this).get(ViewModel.class);

    }

    public void onClick(View v){

        switch(v.getId())
        {
            case R.id.btn_login:
                viewModel.login(String.valueOf(editTextEmail.getText()), String.valueOf(editTextPassword.getText()))
                        .observe(Login.this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean loginResult) {
                                if (loginResult) {
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
                break;

            case R.id.registerNow:
                Intent intent = new Intent(v.getContext(), Register.class);
                startActivity(intent);
                break;


        }
    }
}