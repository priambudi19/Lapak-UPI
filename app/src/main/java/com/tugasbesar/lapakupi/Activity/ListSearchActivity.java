package com.tugasbesar.lapakupi.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.tugasbesar.lapakupi.Adapter.BarangAdapter;
import com.tugasbesar.lapakupi.model.Barang;
import com.tugasbesar.lapakupi.model.GetListBarang;
import com.tugasbesar.lapakupi.R;
import com.tugasbesar.lapakupi.api.ApiInterface;
import com.tugasbesar.lapakupi.api.ApiService;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSearchActivity extends AppCompatActivity {
    String kataKunci ,kategori;
    ArrayList<Barang> barangList;

    ApiInterface apiInterface;
    private RecyclerView rvBarang;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
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
        setContentView(R.layout.activity_list_search);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        barangList = new ArrayList<>();

        rvBarang = (RecyclerView) findViewById(R.id.rv_barang);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBarang.setLayoutManager(mLayoutManager);

        apiInterface = ApiService.getRetrofitInstance().create(ApiInterface.class);

        if (Objects.equals(getIntent().getStringExtra("EXTRA"), "Search")){
            kataKunci = getIntent().getStringExtra("QUERY");
            getSupportActionBar().setTitle("Search");
            getSupportActionBar().setSubtitle("Hasil Pencarian : "+kataKunci);
            Call<GetListBarang> cari = apiInterface.getBarangBySearch(kataKunci);
            cari.enqueue(new Callback<GetListBarang>() {
                @Override
                public void onResponse(Call<GetListBarang> call, Response<GetListBarang> response) {
                    barangList = response.body().getData();
                    Log.d("Retrofit Get", "Jumlah data Barang: " +
                            String.valueOf(barangList.size()));
                    mAdapter = new BarangAdapter(barangList,DetailBarangActivity.class);
                    rvBarang.setAdapter(mAdapter);
                }

                @Override
                public void onFailure(Call<GetListBarang> call, Throwable t) {

                }
            });

        }
        if (Objects.equals(getIntent().getStringExtra("EXTRA"), "Kategori")){
            String [] namakategori = {"Makanan","Minuman","Buku","Elektronik","Pakaian","Lainnya"};
            kategori = getIntent().getStringExtra("QUERY");
            String title = null;
            switch (Integer.parseInt(kategori)){
                case 1:
                    title = namakategori[0];
                    break;
                case 2:
                    title = namakategori[1];
                    break;
                case 3:
                    title = namakategori[2];
                    break;
                case 4:
                    title = namakategori[3];
                    break;
                case 5:
                    title = namakategori[4];
                    break;
                case 6:
                    title = namakategori[5];
                    break;


            }


            getSupportActionBar().setTitle(title);
            Call<GetListBarang> cari = apiInterface.getBarangByKategori(kategori);
            cari.enqueue(new Callback<GetListBarang>() {
                @Override
                public void onResponse(Call<GetListBarang> call, Response<GetListBarang> response) {
                    barangList = response.body().getData();
                    Log.d("Retrofit Get", "Jumlah data Barang: " +
                            String.valueOf(barangList.size()));
                    mAdapter = new BarangAdapter(barangList,DetailBarangActivity.class);
                    rvBarang.setAdapter(mAdapter);
                }

                @Override
                public void onFailure(Call<GetListBarang> call, Throwable t) {

                }
            });

        }
    }

}