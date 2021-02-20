package com.tugasbesar.lapakupi.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.tugasbesar.lapakupi.Config;
import com.tugasbesar.lapakupi.model.GetPostPutBarang;
import com.tugasbesar.lapakupi.R;
import com.tugasbesar.lapakupi.api.ApiInterface;
import com.tugasbesar.lapakupi.api.ApiService;
import com.tugasbesar.lapakupi.session.SessionManager;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditBarangActivity extends AppCompatActivity {
    private String namab;
    private String hargab;
    private String deskripsib;
    private String fotobarang;
    private String id_kategori;
    private String idbarang;
    private String withImg = "Tidak";
    private String idakun;


    EditText etNama, etHarga, etDeskripsi;
    Spinner spKategori;
    ImageView fotoBarang;
    Button btnGaleri, btnSimpan;
    Toolbar toolbar;
    ImageView imgBarang,imgProfil;

    private String mediaPath, postPath;

    ApiInterface apiInterface;
    private static final int REQUEST_PICK_PHOTO = 2;
    private static final int REQUEST_WRITE_PERMISSION = 786;

    // Akses Izin Ambil Gambar dari Storage
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveImageUpload();
        }
    }
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
        setContentView(R.layout.activity_edit_barang);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Iklan");
        Intent intent = getIntent();
        idbarang = intent.getStringExtra("idBarang");
        namab = intent.getStringExtra("namaBarang");
        hargab = intent.getStringExtra("harga");
        id_kategori = intent.getStringExtra("id_kategori");
        deskripsib = intent.getStringExtra("deskripsi");
        fotobarang = intent.getStringExtra("fotoBarang");
        SessionManager sessionManager = new SessionManager(EditBarangActivity.this);
        idakun = sessionManager.getSession();

        apiInterface = ApiService.getRetrofitInstance().create(ApiInterface.class);

        etNama = (EditText) findViewById(R.id.et_nama);
        spKategori = (Spinner) findViewById(R.id.sp_kategori);
        etHarga = (EditText) findViewById(R.id.et_harga);
        etDeskripsi = (EditText) findViewById(R.id.et_deskripsi);
        fotoBarang = (ImageView) findViewById(R.id.fotoBarang);

        //isi semuanya
        etNama.setText(namab);
        spKategori.setSelection(Integer.parseInt(id_kategori)-1);
        etHarga.setText(hargab);
        etDeskripsi.setText(deskripsib);

        Glide.with(this)
                .load(Config.IMAGE_URL+"foto_barang/"+fotobarang)
                .into(fotoBarang);

        btnGaleri = (Button) findViewById(R.id.btn_galery);
        btnSimpan = (Button) findViewById(R.id.btn_simpan);

        apiInterface = ApiService.getRetrofitInstance().create(ApiInterface.class);

        btnGaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withImg = "ya";
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
            }
        });

        // Fungsi Tombol Simpan
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
    }

    // Akses Izin Ambil Gambar dari Storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    // Ambil Image Dari Galeri dan Foto
                    Uri selectedImage = data.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPath = cursor.getString(columnIndex);

                    File file = new File(mediaPath);
                    long size = file.length();
                    if (size > 2097151){
                        Toast.makeText(this,"Ukuran Gambar Terlalu Besar, Maksimal 2 Mb",Toast.LENGTH_LONG).show();
                        mediaPath = null;
                    }else {
                        Glide.with(this)
                                .load(data.getData())
                                .into(fotoBarang);
                        postPath = mediaPath;
                    }

                    cursor.close();
                }
            }
        }
    }

    // Cek Versi Android Tuk Minta Izin
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            saveImageUpload();
        }
    }

    // Simpan Gambar
    private void saveImageUpload(){
        final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            if (validasi()) {
            String s = String.valueOf(spKategori.getSelectedItem());
            if (s.equals("Makanan")) {
                id_kategori = "1";
            } else if (s.equals("Minuman")) {
                id_kategori = "2";
            } else if (s.equals("Buku")) {
                id_kategori = "3";
            } else if (s.equals("Elektronik")) {
                id_kategori = "4";
            } else if (s.equals("Pakaian")) {
                id_kategori = "5";
            } else {
                id_kategori = "6";
            }

            if (withImg == "ya") {
                File imagefile = new File(postPath);
                RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imagefile);
                MultipartBody.Part partImage = MultipartBody.Part.createFormData("foto_barang", imagefile.getName(), reqBody);

                Call<GetPostPutBarang> putBarangCall = apiInterface.putBarangWithImg(
                        partImage, RequestBody.create(MediaType.parse("text/plain"), idakun),
                        RequestBody.create(MediaType.parse("text/plain"), idbarang),
                        RequestBody.create(MediaType.parse("text/plain"), etNama.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), etHarga.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), id_kategori),
                        RequestBody.create(MediaType.parse("text/plain"), etDeskripsi.getText().toString()));

                putBarangCall.enqueue(new Callback<GetPostPutBarang>() {
                    @Override
                    public void onResponse(Call<GetPostPutBarang> call, Response<GetPostPutBarang> response) {
                        Toast.makeText(getApplicationContext(), "Pesan : " + response.body().getPesan(), Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<GetPostPutBarang> call, Throwable t) {
                        Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                        Toast.makeText(getApplicationContext(), "Pesan : " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else {
                Call<GetPostPutBarang> putBarangCall = apiInterface.putBarangNoImg(
                        RequestBody.create(MediaType.parse("text/plain"), idakun),
                        RequestBody.create(MediaType.parse("text/plain"), idbarang),
                        RequestBody.create(MediaType.parse("text/plain"), etNama.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), etHarga.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), id_kategori),
                        RequestBody.create(MediaType.parse("text/plain"), etDeskripsi.getText().toString()));

                putBarangCall.enqueue(new Callback<GetPostPutBarang>() {
                    @Override
                    public void onResponse(Call<GetPostPutBarang> call, Response<GetPostPutBarang> response) {
                        Toast.makeText(getApplicationContext(), "Pesan : " + response.body().getPesan(), Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<GetPostPutBarang> call, Throwable t) {
                        Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                        Toast.makeText(getApplicationContext(), "Pesan : " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private boolean validasi() {
        String nama,harga,des;

        nama = etNama.getText().toString();
        harga = etHarga.getText().toString();
        des = etDeskripsi.getText().toString();
        if (nama.equals("") || harga.equals("") || des.equals("")){
            Toast.makeText(this,"Semua kolom tidak boleh kosong...",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}