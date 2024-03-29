package com.firstapp.braguia.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.firstapp.braguia.Utils.CookieValidation;
import com.firstapp.braguia.R;
import com.firstapp.braguia.ViewModel.ViewModel;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Map;

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Assign variables to each View element
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

        // Checks if cookies are valid
        // Valid cookies -> Skips to MainActivity (We can use the stored cookies to make requests)
        // Non valid cookies -> Forces user to login
        Map<String, ?> cookies = viewModel.getCookies();
        if (!cookies.isEmpty()) {
            if(CookieValidation.validateCookies(cookies.get("csrftoken").toString(), cookies.get("sessionid").toString())){
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }
        }

    }

    public void onClick(View v){

        switch(v.getId())
        {
            case R.id.btn_login:
                viewModel.login(String.valueOf(editTextEmail.getText()), String.valueOf(editTextPassword.getText()))
                        .observe(Login.this, loginResult -> {
                            // On successful login, starts MainActivity
                            if (loginResult) {
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
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