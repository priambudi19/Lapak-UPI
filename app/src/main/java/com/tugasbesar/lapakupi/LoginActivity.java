package com.tugasbesar.lapakupi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tugasbesar.lapakupi.model.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    private static final String SESSION = "session";
    private static final String ID_AKUN = "id_akun";
    EditText et_username, et_password;
    Button btn_register;
    Button btn_login;
    ApiInterface apiInterface;
    String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                apiInterface = ApiService.getRetrofitInstance().create(ApiInterface.class);
                Call<Response> call = apiInterface.login(username,password);
                call.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//                        Toast.makeText(LoginActivity.this,response.body().getData().get(0).getId(),Toast.LENGTH_LONG).show();
//                        String id_akun = response.body().getData().get(0).getId();
//                        loginSession(id_akun);
//                        Log.d(SESSION, getSession());
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {

                    }
                });

            }
        });
    }

    public void loginSession(String id_akun){
        SharedPreferences sharedPreferences = getSharedPreferences(SESSION,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ID_AKUN,id_akun);
    }

    public String getSession(){
        SharedPreferences sharedPreferences = getSharedPreferences(SESSION,MODE_PRIVATE);
        return sharedPreferences.getString(ID_AKUN,"");
    }
}