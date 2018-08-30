package com.legue.axel.myfavoritesmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    @BindView(R.id.button)
    Button button;
    private MyFavoritesMoviesApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initClickListener();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_order:
                Toast.makeText(this, "order selected", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initClickListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(application.getRetrofitManager() != null){
                 Call<ResponseBody> call =  application.getRetrofitManager().getPopularMovies("1","en_US");
                    try {
                        ResponseBody responseBody1 = call.execute().body();
                        Log.i("TAG", "onClick: " + responseBody1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void initData() {
        application = (MyFavoritesMoviesApplication) getApplication();
    }
}
