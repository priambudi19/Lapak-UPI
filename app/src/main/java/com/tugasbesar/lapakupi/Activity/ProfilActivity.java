package com.tugasbesar.lapakupi.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tugasbesar.lapakupi.Config;
import com.tugasbesar.lapakupi.model.Akun;
import com.tugasbesar.lapakupi.model.AkunRespond;
import com.tugasbesar.lapakupi.R;
import com.tugasbesar.lapakupi.api.ApiInterface;
import com.tugasbesar.lapakupi.api.ApiService;
import com.tugasbesar.lapakupi.session.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilActivity extends AppCompatActivity {
    TextView nama, username, prodi, nowa;
    ImageView fotoprofil;
    Toolbar toolbar;
    Button btn_edit;
    SessionManager sessionManager;
    ApiInterface apiInterface;
    Akun akun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Profil");
        nama = findViewById(R.id.txt_nama);
        username = findViewById(R.id.txt_username);
        prodi = findViewById(R.id.txt_prodi);
        nowa = findViewById(R.id.txt_nohp);
        fotoprofil = findViewById(R.id.fotoProfil);
        btn_edit = findViewById(R.id.btn_editprofil);
        sessionManager = new SessionManager(ProfilActivity.this);
        apiInterface = ApiService.getRetrofitInstance().create(ApiInterface.class);
        Call<AkunRespond> callPenjual = apiInterface.getAkunById(sessionManager.getSession());
        callPenjual.enqueue(new Callback<AkunRespond>() {
            @Override
            public void onResponse(Call<AkunRespond> call, Response<AkunRespond> response) {
                ArrayList<Akun> listakun;
                listakun = response.body().getDataAkun();
                akun = listakun.get(0);
                bind(akun.getNama(), akun.getUsername(), akun.getProdi(), akun.getNo_hp(), akun.getFoto_profil());
            }

            @Override
            public void onFailure(Call<AkunRespond> call, Throwable t) {
                Toast.makeText(ProfilActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, EditProfilActivity.class);
                startActivity(intent);
            }
        });

    }

    private void bind(String nama, String username, String prodi, String nowa, String foto) {
        try {
            this.nama.setText(nama);
            this.username.setText(username);
            this.prodi.setText(prodi);
            this.nowa.setText(nowa);
            RequestOptions myOptions = new RequestOptions()
                    .fitCenter() // or centerCrop
                    .circleCrop()
                    .override(200, 200);
            Glide.with(getApplicationContext()).load(Config.FOTO_PROFIL + foto).apply(myOptions).into(this.fotoprofil);
        } catch (Exception e) {
            e.getMessage();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    @Override
    protected void onStart() {
        super.onStart();
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem login, profil,iklan,logout,username ;
        login = menu.findItem(R.id.login);
        profil = menu.findItem(R.id.profil);
        username = menu.findItem(R.id.username);
        iklan = menu.findItem(R.id.iklan);
        logout = menu.findItem(R.id.logout);
        sessionManager = new SessionManager(ProfilActivity.this);
        ApiInterface apiInterface = ApiService.getRetrofitInstance().create(ApiInterface.class);
        Call<AkunRespond> call = apiInterface.getAkunById(sessionManager.getSession());
        call.enqueue(new Callback<AkunRespond>() {
            @Override
            public void onResponse(Call<AkunRespond> call, Response<AkunRespond> response) {
                String s = response.body().getDataAkun().get(0).getUsername();
                username.setTitle(s);
            }

            @Override
            public void onFailure(Call<AkunRespond> call, Throwable t) {

            }
        });

        if(sessionManager.getSession()!=null){
            login.setVisible(false);
            profil.setVisible(true);
            profil.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            iklan.setVisible(true);
            logout.setVisible(true);
        }else{
            login.setVisible(true);
            login.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            profil.setVisible(false);
            username.setVisible(false);
            iklan.setVisible(false);
            logout.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.profil:
            case R.id.username:
                intent = new Intent(getApplicationContext(),ProfilActivity.class);
                startActivity(intent);
                break;
            case R.id.iklan:
                intent = new Intent(getApplicationContext(),IklanSayaActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                sessionManager.logout();
                intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;


        }

        return super.onOptionsItemSelected(item);
    }
}