package com.tugasbesar.lapakupi.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tugasbesar.lapakupi.Config;
import com.tugasbesar.lapakupi.model.Akun;
import com.tugasbesar.lapakupi.model.AkunRespond;
import com.tugasbesar.lapakupi.R;
import com.tugasbesar.lapakupi.api.ApiInterface;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tugasbesar.lapakupi.api.ApiService;
import com.tugasbesar.lapakupi.model.Response;
import com.tugasbesar.lapakupi.session.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
public class EditProfilActivity extends AppCompatActivity {
    EditText et_username, et_password, et_nama, et_prodi, et_nowa;
    Button btn_editprofile, btn_pilihfoto, btn_login;
    ApiInterface apiInterface;
    ImageView profil;
    private String username, password, nama, prodi, no_hp;
    Bitmap bitmap;
    SessionManager sessionManager;
    Akun akun;
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
        setContentView(R.layout.activity_edit_profil);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Profil");
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_nama = findViewById(R.id.et_nama);
        et_prodi = findViewById(R.id.et_prodi);
        et_nowa = findViewById(R.id.et_nohp);
        btn_editprofile = findViewById(R.id.btn_editprofil);
        btn_pilihfoto = findViewById(R.id.btn_pilihfoto);
        profil = findViewById(R.id.img_profile);
        sessionManager = new SessionManager(EditProfilActivity.this);
        Log.d("Session", sessionManager.getSession());
        getLastData();

        btn_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


        btn_pilihfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                if (data != null) {
                    // Ambil Image Dari Galeri dan Foto
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    profil.setImageURI(data.getData());
                    RequestOptions myOptions = new RequestOptions()
                            .fitCenter() // or centerCrop
                            .circleCrop()
                            .override(200, 200);

                    Glide.with(this).load(data.getData()).apply(myOptions).into(profil);
                    cursor.close();

                }
            }
        }
    }
    private File createTempFile(Bitmap bitmap) {
        if(bitmap!=null) {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    , System.currentTimeMillis() + "_image.jpeg");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            //write the bytes in file

            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }else {
            return null;
        }

    }


  private void getLastData(){
      apiInterface = ApiService.getRetrofitInstance().create(ApiInterface.class);

      Call<AkunRespond> callPenjual = apiInterface.getAkunById(sessionManager.getSession());
      callPenjual.enqueue(new Callback<AkunRespond>() {
          @Override
          public void onResponse(Call<AkunRespond> call, retrofit2.Response<AkunRespond> response) {
              ArrayList<Akun> listakun;
              listakun = response.body().getDataAkun();
              akun = listakun.get(0);
              bind(akun.getNama(),akun.getUsername(),akun.getPassword(),akun.getProdi(),akun.getNo_hp(),akun.getFoto_profil());

          }

          @Override
          public void onFailure(Call<AkunRespond> call, Throwable t) {
              Toast.makeText(EditProfilActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();

          }

      });
  }

    private void bind(String nama, String username, String password, String prodi, String no_hp, String foto_profil) {
        try {
            this.et_nama.setText(nama);
            this.et_username.setText(username);
            this.et_password.setText(password);
            this.et_prodi.setText(prodi);
            this.et_nowa.setText(no_hp);
            if(foto_profil!=null){
                RequestOptions myOptions = new RequestOptions()
                        .fitCenter() // or centerCrop
                        .circleCrop()
                        .override(200, 200);
                Glide.with(getApplicationContext()).load(Config.FOTO_PROFIL+foto_profil).apply(myOptions).into(this.profil);
            }

        }catch (Exception e){
            e.getMessage();
        }

    }

    private void register() {
        username = et_username.getText().toString();
        password = et_password.getText().toString();
        Log.d("Register", "register: "+password);
        nama = et_nama.getText().toString();
        prodi = et_prodi.getText().toString();
        no_hp = et_nowa.getText().toString();
        apiInterface = ApiService.getRetrofitInstance2().create(ApiInterface.class);
        File imagefile = createTempFile(bitmap);

        if(imagefile!=null) {
            RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imagefile);
            MultipartBody.Part partImage = MultipartBody.Part.createFormData("foto_profil", imagefile.getName(), reqBody);
            Call<Response> call = apiInterface.editAkun(toRequestBody(sessionManager.getSession()), toRequestBody(username), toRequestBody(password), toRequestBody(nama), toRequestBody(prodi), toRequestBody(no_hp), partImage);
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    Toast.makeText(EditProfilActivity.this, "Berhasil Diupdate", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    t.printStackTrace();
                    Log.d("Retrofit", t.getMessage());
                    Toast.makeText(EditProfilActivity.this, "Gagal", Toast.LENGTH_LONG).show();
                }
            });
        }else {
            String id_akun = sessionManager.getSession();
            Call<Response> call = apiInterface.editAkunNoImage(toRequestBody(id_akun),
                    toRequestBody(username),toRequestBody(password),toRequestBody(nama),toRequestBody(prodi),toRequestBody(no_hp));
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    Toast.makeText(EditProfilActivity.this, "Berhasil Diupdate", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    t.printStackTrace();
                    Log.d("Retrofit", t.getLocalizedMessage());
                    Toast.makeText(EditProfilActivity.this, "Gagal", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }


}