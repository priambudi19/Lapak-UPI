package com.tugasbesar.lapakupi.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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


public class DetailBarangActivity extends AppCompatActivity {
    private String namab, hargab, deskripsib, fotobarang, idbarang, idakun, idkategori;
    TextView tvNamaakun, tvNama, tvHarga, tvDeskripsi, tvKategori;
    ImageView imgBarang, imgProfil;
    Button btnContact;
    Akun penjual;
    SessionManager sessionManager;
    ApiInterface apiInterface;
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
        setContentView(R.layout.activity_detail_barang);
        Intent intent = getIntent();
        idbarang = intent.getStringExtra("idBarang");
        idakun = intent.getStringExtra("idAkun");
        namab = intent.getStringExtra("namaBarang");
        hargab = intent.getStringExtra("harga");
        idkategori = intent.getStringExtra("id_kategori");
        deskripsib = intent.getStringExtra("deskripsi");
        fotobarang = intent.getStringExtra("fotoBarang");
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detail Produk");
        tvNamaakun = (TextView) findViewById(R.id.txt_namaakun);
        tvNama = (TextView) findViewById(R.id.txt_namabarang);
        tvHarga = (TextView) findViewById(R.id.txt_hargabarang);
        tvKategori = (TextView) findViewById(R.id.txt_kategori);
        tvDeskripsi = (TextView) findViewById(R.id.txt_deskripsibarang);
        imgBarang = (ImageView) findViewById(R.id.img_barang);
        imgProfil = (ImageView) findViewById(R.id.fotoProfil);
        btnContact = (Button) findViewById(R.id.btncontact);

        if (idakun != null) {

            apiInterface = ApiService.getRetrofitInstance().create(ApiInterface.class);
            Call<AkunRespond> callPenjual = apiInterface.getAkunById(idakun);
            callPenjual.enqueue(new Callback<AkunRespond>() {
                @Override
                public void onResponse(Call<AkunRespond> call, Response<AkunRespond> response) {
                    ArrayList<Akun> listakun;
                    listakun = response.body().getDataAkun();
                    penjual = listakun.get(0);

                    lanjut();
                }

                @Override
                public void onFailure(Call<AkunRespond> call, Throwable t) {
                    Toast.makeText(DetailBarangActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(DetailBarangActivity.this, "Id akun tidak ada", Toast.LENGTH_SHORT).show();
        }
    }

    private void lanjut() {
        String kategori;
        if (idkategori.equals("1")) {
            kategori = "Makanan";
        } else if (idkategori.equals("2")) {
            kategori = "Minuman";
        } else if (idkategori.equals("3")) {
            kategori = "Buku";
        } else if (idkategori.equals("4")) {
            kategori = "Elektronik";
        } else if (idkategori.equals("5")) {
            kategori = "Pakaian";
        } else {
            kategori = "Lainnya";
        }

        if (penjual != null) {
            Glide.with(this)
                    .load(Config.IMAGE_URL + "foto_barang/" + fotobarang)
                    .fitCenter()
                    .into(imgBarang);

            Glide.with(this)
                    .load(Config.IMAGE_URL + "foto_profile/" + penjual.getFoto_profil())
                    .fitCenter()
                    .circleCrop()
                    .into(imgProfil);

            tvNamaakun.setText(penjual.getNama());
            tvNama.setText(namab);
            tvHarga.setText(hargab);
            tvKategori.setText(kategori);
            tvDeskripsi.setText(deskripsib);

            btnContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PackageManager pm = getPackageManager();
                    try {
                        Intent waIntent = new Intent(Intent.ACTION_VIEW);
                        String text = "Saya tertarik untuk membeli " + namab + " pada anda (" + penjual.getNama() + ") dengan harga Rp. " +
                                hargab + ",-. Apakah barang tersebut masih tersedia?";

                        PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//                        waIntent.setPackage("com.whatsapp");
//                        waIntent.setType("text/plain");
                        waIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + penjual.getNo_hp() + "&text=" + text));
                        startActivity(waIntent);

                    } catch (PackageManager.NameNotFoundException e) {
                        Toast.makeText(DetailBarangActivity.this, "WhatsApp belum terinstall", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(DetailBarangActivity.this, "Penjual Null", Toast.LENGTH_SHORT).show();
        }
    }
}