package com.sariidaman.giovan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sariidaman.giovan.adapter.AdapterOrder;
import com.sariidaman.giovan.adapter.AdapterOrderApi;
import com.sariidaman.giovan.helper.ApiClient;
import com.sariidaman.giovan.helper.ApiService;
import com.sariidaman.giovan.helper.DatabaseHandler;
import com.sariidaman.giovan.helper.MyFunction;
import com.sariidaman.giovan.helper.Widget;
import com.sariidaman.giovan.model.BasicListResponse;
import com.sariidaman.giovan.model.BasicResponse;
import com.sariidaman.giovan.model.KategoriMasakan;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sariidaman.giovan.helper.GlobalVariabel.PICK_IMAGE;

public class Order extends AppCompatActivity {
    public static Order order_activity;
    String dana_link = "https://link.dana.id/qr/31aa6hb7";
    ProgressDialog loading;
    RecyclerView rv_order,rv_order_api;
    List<com.sariidaman.giovan.model.Order> orderList;
    TextView tv_total,tv_order_new,tv_order_before;
    int total = 0,total_api = 0;
    ApiService mApiService;
    String ordermeja_id;
    Button btn_proses_pesanan,btn_bayar_ovo,btn_bayar_dana;
    String id_masakan,harga,qty,total_harga_bayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        init();
    }
    public void init(){
        order_activity = this;
        mApiService = ApiClient.getAPIService();
        /*btn_bayar_ovo = findViewById(R.id.btn_bayar_ovo);*/
        btn_proses_pesanan = findViewById(R.id.btn_proses_pesanan);
        tv_order_new = findViewById(R.id.tv_order_new);
        tv_order_before = findViewById(R.id.tv_order_before);
        btn_bayar_dana = findViewById(R.id.btn_bayar_dana);
        SharedPreferences settings = getSharedPreferences("myPref", MODE_PRIVATE);
        ordermeja_id = settings.getString("ordermeja_id","0");
        rv_order = findViewById(R.id.rv_order);
        rv_order_api = findViewById(R.id.rv_order_api);

        tv_total = findViewById(R.id.tv_total);
        getOrderAPI();
        cekStatusTransaksi();
        btn_proses_pesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(Order.this);
                builder.setCancelable(false);
                builder.setTitle("Order");
                builder.setPositiveButton(Html.fromHtml("<font color='#000000'>Ya</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //((Activity) ctx).finish();
                        setProsesPesanan();
                    }
                }).setNegativeButton(Html.fromHtml("<font color='#000000'>Belum</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //((Activity) ctx).finish();
                    }
                });
                builder.setMessage("Apakah anda sudah yakin dengan pesanan anda?");
                builder.show();
            }
        });
        /*btn_bayar_ovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchOvo = getPackageManager().getLaunchIntentForPackage("ovo.id");
                startActivity(launchOvo);
            }
        });*/
        btn_bayar_dana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dana_link));
                startActivity(browserIntent);
            }
        });

    }
///memanggil pesanan sebelumnya
    public void getOrderAPI(){
        total_api = 0;
        mApiService.getDataOrder(ordermeja_id).enqueue(new Callback<BasicListResponse<com.sariidaman.giovan.model.Order>>() {
            @Override
            public void onResponse(Call<BasicListResponse<com.sariidaman.giovan.model.Order>> call, Response<BasicListResponse<com.sariidaman.giovan.model.Order>> response) {
                if(response.body().isSuccess()) {
                    ArrayList<com.sariidaman.giovan.model.Order> orders = response.body().getDatas();
                    AdapterOrderApi adapterOrder = new AdapterOrderApi(Order.this, orders);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(Order.this, LinearLayoutManager.VERTICAL, false);
                    rv_order_api.setLayoutManager(layoutManager);
                    rv_order_api.setAdapter(adapterOrder);
                    for(int i = 0; i< orders.size(); i++){
                        total_api += (orders.get(i).getHarga() * orders.get(i).getQty());

                    }
                    if(orders.size() > 0){
                        tv_order_before.setVisibility(View.VISIBLE);
                    }
                }
                tv_total.setText(MyFunction.formatRupiah(total_api));
                getOrder();
            }

            @Override
            public void onFailure(Call<BasicListResponse<com.sariidaman.giovan.model.Order>> call, Throwable t) {

            }
        });

    }
    // manggil pesanan baru
    public void getOrder(){
        DatabaseHandler databaseHandler = new DatabaseHandler(Order.this);
        orderList = databaseHandler.getData();
        AdapterOrder adapterOrder = new AdapterOrder(Order.this, orderList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Order.this, LinearLayoutManager.VERTICAL, false);
        rv_order.setLayoutManager(layoutManager);
        rv_order.setAdapter(adapterOrder);
        id_masakan = "";
        harga = "";
        qty = "";
        total = total_api;
        for(int i = 0; i< orderList.size(); i++){
            id_masakan += orderList.get(i).getId_masakan()  + ",";
            harga += String.valueOf(orderList.get(i).getHarga()) + ",";
            qty += String.valueOf(orderList.get(i).getQty()) + ",";
            total += (orderList.get(i).getHarga() * orderList.get(i).getQty());
            Log.d("TOTAL",orderList.get(i).getHarga() + " " +  orderList.get(i).getQty());
        }
        total_harga_bayar=String.valueOf(total);
        if(orderList.size() == 0){
            //btn_bayar_ovo.setEnabled(false);
            btn_proses_pesanan.setEnabled(false);
            btn_proses_pesanan.setBackgroundColor(getResources().getColor(R.color.grey));
        }else{
            tv_order_new.setVisibility(View.VISIBLE);
        }
        tv_total.setText(MyFunction.formatRupiah(total));
    }
    public void setProsesPesanan(){
        loading = ProgressDialog.show(Order.this, null, "Please Wait...", true, false);
        mApiService.setProsesPesanan(ordermeja_id,id_masakan,harga,qty,total_harga_bayar).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if(response.body().isSuccess()){
                    loading.dismiss();
                    DatabaseHandler databaseHandler = new DatabaseHandler(Order.this);
                    databaseHandler.deleteAllData();
                    getOrderAPI();

                    Widget.showMessageBox(Order.this,"Info pesanan",response.body().getMessage());
                }else{
                    loading.dismiss();
                    Widget.showMessageBoxErrorServer(Order.this);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });
    }

    public void cekStatusTransaksi(){
        mApiService.cekStatusTransaksi(ordermeja_id).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().isSuccess()){
                        DatabaseHandler databaseHandler = new DatabaseHandler(Order.this);
                        databaseHandler.deleteAllData();
                        Widget.showMessageBoxLogout(Order.this,"Info",response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });

    }



}