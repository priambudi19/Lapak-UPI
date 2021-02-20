package com.tugasbesar.lapakupi.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tugasbesar.lapakupi.R;
import com.tugasbesar.lapakupi.api.ApiInterface;
import com.tugasbesar.lapakupi.api.ApiService;
import com.tugasbesar.lapakupi.model.Login;
import com.tugasbesar.lapakupi.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String SESSION = "session";
    private static final String ID_AKUN = "id_akun";
    EditText et_username, et_password;
    Button btn_register;
    Button btn_login;
    ApiInterface apiInterface;
    String username,password;
    SessionManager sessionManager;
    Toolbar toolbar;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Login");

        sessionManager = new SessionManager(LoginActivity.this);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                apiInterface = ApiService.getRetrofitInstance().create(ApiInterface.class);
                Call<Login> call = apiInterface.login(username,password);
                call.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        String id_akun = null;
                        if(response.isSuccessful()){
                            id_akun = response.body().getId();
                            Toast.makeText(LoginActivity.this,id_akun,Toast.LENGTH_LONG).show();
                        }

                        if(id_akun!=null) {
                            sessionManager.loginSession(id_akun);
                            //cek session
                            Log.d("TAG", "id_akun: "+sessionManager.getSession());
                            finish();


                        }else {
                            Toast.makeText(LoginActivity.this,"Salah password atau user tidak terdaftar",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,"Error Server",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }


}
