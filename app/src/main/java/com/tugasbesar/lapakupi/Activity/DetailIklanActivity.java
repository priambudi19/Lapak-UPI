package com.tugasbesar.lapakupi.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.tugasbesar.lapakupi.model.Barang;
import com.tugasbesar.lapakupi.model.GetListBarang;
import com.tugasbesar.lapakupi.model.StatMessRespond;
import com.tugasbesar.lapakupi.R;
import com.tugasbesar.lapakupi.api.ApiInterface;
import com.tugasbesar.lapakupi.api.ApiService;
import com.tugasbesar.lapakupi.session.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailIklanActivity extends AppCompatActivity {

    private String namab,namapenjual;
    private String hargab;
    private String deskripsib;
    private String fotobarang,fotopenjual;
    private String idkategori, idbarang;
    private String idakun; //ambil di shared preference

    TextView tvNamaakun, tvNama, tvHarga, tvDeskripsi, tvKategori;
    ImageView imgBarang, imgProfil;
    Button btnEdit, btnDelete;

    private Akun penjual;

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
        setContentView(R.layout.activity_detail_iklan);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detail Iklan");


        Intent aintent = getIntent();
        idbarang = aintent.getStringExtra("idBarang");
        idakun = aintent.getStringExtra("idAkun");

        tvNamaakun = findViewById(R.id.txt_namaakun);
        tvNama = findViewById(R.id.txt_namabarang);
        tvHarga = findViewById(R.id.txt_hargabarang);
        tvKategori = findViewById(R.id.txt_kategori);
        tvDeskripsi = findViewById(R.id.txt_deskripsibarang);
        imgBarang = findViewById(R.id.img_barang);
        imgProfil = findViewById(R.id.fotoProfil);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        apiInterface = ApiService.getRetrofitInstance().create(ApiInterface.class);


    }

    @Override
    protected void onStart() {
        super.onStart();

        Call<AkunRespond> callPenjual = apiInterface.getAkunById(idakun);
        callPenjual.enqueue(new Callback<AkunRespond>() {
            @Override
            public void onResponse(Call<AkunRespond> call, Response<AkunRespond> response) {
                ArrayList<Akun> listakun;
                listakun = response.body().getDataAkun();
                penjual = listakun.get(0);

                namapenjual = penjual.getNama();
                fotopenjual = penjual.getFoto_profil();

            }

            @Override
            public void onFailure(Call<AkunRespond> call, Throwable t) {
                Toast.makeText(DetailIklanActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Call<GetListBarang> call = apiInterface.getBarangById(idbarang);
        call.enqueue(new Callback<GetListBarang>() {
            @Override
            public void onResponse(Call<GetListBarang> call, Response<GetListBarang> response) {
                Barang barang = response.body().getData().get(0);
                namab = barang.getNama_barang();
                hargab = barang.getHarga();
                idkategori =barang.getId_kategori();
                deskripsib = barang.getDeskripsi();
                fotobarang = barang.getFoto_barang();


                lanjut();
            }

            @Override
            public void onFailure(Call<GetListBarang> call, Throwable t) {

            }
        });

    }

    private void lanjut() {
        String kategori;
        switch (idkategori) {
            case "1":
                kategori = "Makanan";
                break;
            case "2":
                kategori = "Minuman";
                break;
            case "3":
                kategori = "Buku";
                break;
            case "4":
                kategori = "Elektronik";
                break;
            case "5":
                kategori = "Pakaian";
                break;
            default:
                kategori = "Lainnya";
                break;
        }

        Glide.with(this)
                .load(Config.IMAGE_URL + "foto_barang/" + fotobarang)
                .fitCenter()
                .into(imgBarang);

        Glide.with(this)
                .load(Config.IMAGE_URL + "foto_profile/" +fotopenjual)
                .fitCenter()
                .circleCrop()
                .into(imgProfil);

        tvNamaakun.setText(namapenjual);
        tvNama.setText(namab);
        tvHarga.setText(hargab);
        tvKategori.setText(kategori);
        tvDeskripsi.setText(deskripsib);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailIklanActivity.this, EditBarangActivity.class);
                intent.putExtra("idBarang", idbarang);
                intent.putExtra("idAkun", idakun);
                intent.putExtra("namaBarang", namab);
                intent.putExtra("harga", hargab);
                intent.putExtra("id_kategori", idkategori);
                intent.putExtra("deskripsi", deskripsib);
                intent.putExtra("fotoBarang", fotobarang);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(20);
            }
        });
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == 10;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan iklan?";
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus iklan ini?";
            dialogTitle = "Hapus Iklan";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isDialogClose) {
                            finish();
                        } else {

                            Call<StatMessRespond> delete = apiInterface.delleteBarang(idbarang);
                            delete.enqueue(new Callback<StatMessRespond>() {
                                @Override
                                public void onResponse(Call<StatMessRespond> call, Response<StatMessRespond> response) {
                                    Toast.makeText(DetailIklanActivity.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(DetailIklanActivity.this, IklanSayaActivity.class);
                                    startActivity(i);
                                }

                                @Override
                                public void onFailure(Call<StatMessRespond> call, Throwable t) {
                                    Toast.makeText(DetailIklanActivity.this, "Iklan Gagal Dihapus!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}