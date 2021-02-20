package com.tugasbesar.lapakupi.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.tugasbesar.lapakupi.Adapter.BarangAdapter;
import com.tugasbesar.lapakupi.model.AkunRespond;
import com.tugasbesar.lapakupi.model.Barang;
import com.tugasbesar.lapakupi.model.GetListBarang;
import com.tugasbesar.lapakupi.R;
import com.tugasbesar.lapakupi.api.ApiInterface;
import com.tugasbesar.lapakupi.api.ApiService;
import com.tugasbesar.lapakupi.session.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IklanSayaActivity extends AppCompatActivity {
    ArrayList<Barang> barangList;
    Toolbar toolbar;
    ApiInterface apiInterface;
    private RecyclerView rvBarang;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SessionManager sessionManager;
    String idakun; //diambil dari sharedPreferences - id akun yang login



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iklan_saya);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Iklan Saya");

        barangList = new ArrayList<>();

        rvBarang = (RecyclerView) findViewById(R.id.rv_barang);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBarang.setLayoutManager(mLayoutManager);

        apiInterface = ApiService.getRetrofitInstance().create(ApiInterface.class);
        SessionManager sessionManager = new SessionManager(IklanSayaActivity.this);
        idakun = sessionManager.getSession();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            Intent i = getIntent();
            finish();
            startActivity(i);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Call<GetListBarang> cari = apiInterface.getBarangByAkun(idakun);
        cari.enqueue(new Callback<GetListBarang>() {
            @Override
            public void onResponse(Call<GetListBarang> call, Response<GetListBarang> response) {
                barangList = response.body().getData();
                Log.d("Retrofit Get", "Jumlah data Barang: " +
                        String.valueOf(barangList.size()));
                mAdapter = new BarangAdapter(barangList,DetailIklanActivity.class);
                rvBarang.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetListBarang> call, Throwable t) {

            }
        });
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem login, profil,iklan,logout,username ;
        login = menu.findItem(R.id.login);
        profil = menu.findItem(R.id.profil);
        username = menu.findItem(R.id.username);
        iklan = menu.findItem(R.id.iklan);
        logout = menu.findItem(R.id.logout);
        sessionManager = new SessionManager(IklanSayaActivity.this);
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