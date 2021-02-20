package com.tugasbesar.lapakupi.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.tugasbesar.lapakupi.model.AkunRespond;
import com.tugasbesar.lapakupi.R;
import com.tugasbesar.lapakupi.api.ApiInterface;
import com.tugasbesar.lapakupi.api.ApiService;
import com.tugasbesar.lapakupi.session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    SessionManager sessionManager;
    SearchView svQuery;
    Toolbar toolbar;
    CardView cardView1,cardView2,cardView3,cardView4,cardView5,cardView6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        svQuery = findViewById(R.id.searchView);
        svQuery.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Info", query);
                Intent intent = new Intent(HomeActivity.this, ListSearchActivity.class);
                intent.putExtra("EXTRA", "Search");
                intent.putExtra("QUERY", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        cardView1 = findViewById(R.id.cardView1);
        cardView2 = findViewById(R.id.cardView2);
        cardView3 = findViewById(R.id.cardview3);
        cardView4 = findViewById(R.id.cardView4);
        cardView5 = findViewById(R.id.cardView5);
        cardView6 = findViewById(R.id.cardView6);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListSearchActivity.class);
                intent.putExtra("EXTRA", "Kategori");
                intent.putExtra("QUERY", "1");
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListSearchActivity.class);
                intent.putExtra("EXTRA", "Kategori");
                intent.putExtra("QUERY", "2");
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListSearchActivity.class);
                intent.putExtra("EXTRA", "Kategori");
                intent.putExtra("QUERY", "3");
                startActivity(intent);
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListSearchActivity.class);
                intent.putExtra("EXTRA", "Kategori");
                intent.putExtra("QUERY", "4");
                startActivity(intent);
            }
        });
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListSearchActivity.class);
                intent.putExtra("EXTRA", "Kategori");
                intent.putExtra("QUERY", "5");
                startActivity(intent);
            }
        });
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListSearchActivity.class);
                intent.putExtra("EXTRA", "Kategori");
                intent.putExtra("QUERY", "6");
                startActivity(intent);
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
        sessionManager = new SessionManager(HomeActivity.this);
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
                intent = getIntent();
                finish();
                startActivity(intent);

                break;
            case android.R.id.home:
                onBackPressed();
                break;


        }

        return super.onOptionsItemSelected(item);
    }
}