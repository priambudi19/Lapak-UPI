package com.tugasbesar.lapakupi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tugasbesar.lapakupi.model.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    EditText et_username, et_password, et_nama, et_prodi, et_nowa;
    Button btn_register, btn_pilihfoto, btn_login;
    ApiInterface apiInterface;
    ImageView profil;
    String username, password, nama, prodi, no_hp;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_nama = findViewById(R.id.et_nama);
        et_prodi = findViewById(R.id.et_prodi);
        et_nowa = findViewById(R.id.et_nohp);
        btn_register = findViewById(R.id.btn_register);
        btn_pilihfoto = findViewById(R.id.btn_pilihfoto);
        profil = findViewById(R.id.img_profile);
        btn_register.setOnClickListener(new View.OnClickListener() {
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

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
//        call.enqueue(new Callback<Response>()

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
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_image.jpeg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100, bos);
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
    }

    private void register() {
        username = et_username.getText().toString();
        password = et_password.getText().toString();
        nama = et_nama.getText().toString();
        prodi = et_prodi.getText().toString();
        no_hp = et_nowa.getText().toString();
        apiInterface = ApiService.getRetrofitInstance().create(ApiInterface.class);
        File imagefile = createTempFile(bitmap);
        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imagefile);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("foto_profil", imagefile.getName(), reqBody);


        Call<Response> call = apiInterface.register(toRequestBody(username), toRequestBody(password), toRequestBody(nama), toRequestBody(prodi), toRequestBody(no_hp), partImage);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("TAG", t.getMessage());
                Toast.makeText(MainActivity.this, "Gagal", Toast.LENGTH_LONG).show();
            }
        });
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