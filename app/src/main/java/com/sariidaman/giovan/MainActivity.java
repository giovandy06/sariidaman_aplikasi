package com.sariidaman.giovan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sariidaman.giovan.adapter.AdapterKategoriMasakan;
import com.sariidaman.giovan.adapter.AdapterMasakan;
import com.sariidaman.giovan.helper.ApiClient;
import com.sariidaman.giovan.helper.ApiService;
import com.sariidaman.giovan.helper.DatabaseHandler;
import com.sariidaman.giovan.helper.MyFunction;
import com.sariidaman.giovan.helper.Widget;
import com.sariidaman.giovan.model.BasicListResponse;
import com.sariidaman.giovan.model.BasicResponse;
import com.sariidaman.giovan.model.KategoriMasakan;
import com.sariidaman.giovan.model.Masakan;
import com.sariidaman.giovan.model.Order;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    //buat nyiapin variable
    RecyclerView rv_kategori,rv_masakan;
    public static MainActivity MA;
    String id_kategori_global = "All";
    ProgressDialog loading;
    ApiService mApiService;
    String ordermeja_id;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    public void init(){
        MA = this;
        SharedPreferences settings = getSharedPreferences("myPref", MODE_PRIVATE);
        ordermeja_id = settings.getString("ordermeja_id","0");
        //ngenalin varieble yang dibuat ke xml yang dipakai
        rv_kategori = findViewById(R.id.rv_kategori);
        rv_masakan = findViewById(R.id.rv_masakan);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_items);
        mApiService = ApiClient.getAPIService();
        getDataKategoriMasakan();
        getDataMasakan();
        cekStatusTransaksi();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataMasakan(id_kategori_global);
            }
        });
    }
    public void cekStatusTransaksi(){
        mApiService.cekStatusTransaksi(ordermeja_id).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if(response.isSuccessful()){
                 if(response.body().isSuccess()){
                     DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
                     databaseHandler.deleteAllData();
                     Widget.showMessageBoxLogout(MainActivity.this,"Info",response.body().getMessage());
                 }
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });

    }


    public void getDataKategoriMasakan(){
        mApiService.getDataKategoriMasakan().enqueue(new Callback<BasicListResponse<KategoriMasakan>>() {
            @Override
            public void onResponse(Call<BasicListResponse<KategoriMasakan>> call, Response<BasicListResponse<KategoriMasakan>> response) {
                if(response.body().isSuccess()){
                    ArrayList<KategoriMasakan> kategoriMasakans= response.body().getDatas();
                    AdapterKategoriMasakan adapterKategoriMasakan = new AdapterKategoriMasakan(MainActivity.this,kategoriMasakans);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    rv_kategori.setLayoutManager(layoutManager);
                    rv_kategori.setAdapter(adapterKategoriMasakan);
                }
            }

            @Override
            public void onFailure(Call<BasicListResponse<KategoriMasakan>> call, Throwable t) {

            }
        });
    }

    public void getDataMasakan(){ getDataMasakan("All"); }
    public void getDataMasakan(String id_kategori){
        id_kategori_global = id_kategori;
        loading = ProgressDialog.show(MainActivity.this, null, "Please Wait...", true, false);
        mApiService.getDataMasakan(id_kategori).enqueue(new Callback<BasicListResponse<Masakan>>() {
            @Override
            public void onResponse(Call<BasicListResponse<Masakan>> call, Response<BasicListResponse<Masakan>> response) {
                if(response.body().isSuccess()){
                    loading.dismiss();
                    ArrayList<Masakan> masakans = response.body().getDatas();
                    AdapterMasakan adapterMasakan = new AdapterMasakan(MainActivity.this,masakans);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv_masakan.setLayoutManager(layoutManager);
                    rv_masakan.setAdapter(adapterMasakan);
                    if(mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicListResponse<Masakan>> call, Throwable t) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.order:
                startActivity(new Intent(MainActivity.this, com.sariidaman.giovan.Order.class));
                return true;
            case R.id.logout:
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Info").setNegativeButton(Html.fromHtml("<font color='#000000'>Tidak</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton(Html.fromHtml("<font color='#000000'>Iya</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
                        databaseHandler.deleteAllData();
                        Widget.showMessageBoxLogout(MainActivity.this,"Info","Terima kasih");
                    }
                });
                builder.setMessage("Apakah anda yakin ingin logout?");
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void showModalAddProduct(Masakan masakan){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = MA.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.modal_add_product, null);
        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog;
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        TextView tv_count = dialogView.findViewById(R.id.tv_count);
        ImageView img_plus = dialogView.findViewById(R.id.img_plus);
        ImageView img_minus = dialogView.findViewById(R.id.img_minus);
        ImageView img_item = dialogView.findViewById(R.id.img_item);
        ImageView img_close = dialogView.findViewById(R.id.img_close);

        Button btn_tambah = dialogView.findViewById(R.id.btn_tambah);
        TextView txt_nama = dialogView.findViewById(R.id.txt_nama);
        TextView txt_harga = dialogView.findViewById(R.id.txt_harga);

        String id_masakan = masakan.getId();
        String nama_masakan = masakan.getNama();
        int harga = Integer.parseInt(masakan.getHarga());
        String gambar = masakan.getGambar();


        txt_nama.setText(nama_masakan);
        txt_harga.setText(MyFunction.formatRupiah(harga));
        Glide.with(MainActivity.this).load(ApiClient.BASE_URL + "uploads/" +  gambar).into(img_item);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(tv_count.getText().toString());
                tv_count.setText(String.valueOf(count + 1));
            }
        });
        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(tv_count.getText().toString());
                if(count > 1){
                    tv_count.setText(String.valueOf(count - 1));
                }
            }
        });

        //etDate.setText(day +  "-" + month + "-" + year);
        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(tv_count.getText().toString());

                Order order = new Order();
                order.setId_masakan(id_masakan);
                order.setNama_masakan(nama_masakan);
                order.setHarga(harga);
                order.setGambar(gambar);
                order.setQty(qty);
                DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
                if(databaseHandler.checkIfDataExist(id_masakan)){
                    Toast.makeText(MainActivity.this, "berhasil mengupdate " + String.valueOf(qty) + " " + nama_masakan, Toast.LENGTH_SHORT).show();
                    databaseHandler.updateData(order);
                }else{
                    Toast.makeText(MainActivity.this, "berhasil menambahkan " + String.valueOf(qty) + " " + nama_masakan, Toast.LENGTH_SHORT).show();
                    databaseHandler.addData(order);
                }
                alertDialog.dismiss();
            }
        });
    }
}