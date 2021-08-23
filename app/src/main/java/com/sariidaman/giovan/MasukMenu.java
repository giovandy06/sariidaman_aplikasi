package com.sariidaman.giovan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sariidaman.giovan.helper.ApiClient;
import com.sariidaman.giovan.helper.ApiService;
import com.sariidaman.giovan.helper.Widget;
import com.sariidaman.giovan.model.BasicListResponse;
import com.sariidaman.giovan.model.BasicResponse;
import com.sariidaman.giovan.model.Meja;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasukMenu extends AppCompatActivity {
    //untuk penyiapan variable yang akan dipakai
    Spinner sp_meja;
    EditText et_nama;
    Button btn_masuk;
    ArrayList<String> dataMeja;
    ArrayList<String> dataIdMeja;

    ProgressDialog loading;
    ApiService mApiService;
    private static final String CHANNEL_ID = "101";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk_menu);
        init();
        createNotificationChannel();

        /*SharedPreferences settings = getSharedPreferences("myPref", MODE_PRIVATE);
        String ordermeja_id = settings.getString("ordermeja_id","0");
        if(Integer.parseInt(ordermeja_id) > 0){
            Intent intent = new Intent(MasukMenu.this,MainActivity.class);
            startActivity(intent);
            finish();
        }*/
    }
    public void init(){
        et_nama = findViewById(R.id.et_nama);
        sp_meja = findViewById(R.id.sp_meja);
        btn_masuk = findViewById(R.id.btn_masuk);
        mApiService = ApiClient.getAPIService();
        getDataMeja();
        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MasukMenu.this);
                builder.setCancelable(false);
                builder.setTitle("Info").setNegativeButton(Html.fromHtml("<font color='#000000'>Tidak</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton(Html.fromHtml("<font color='#000000'>Iya</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setMeja();
                    }
                });
                builder.setMessage("Apakah meja yang di pilih sudah benar?");
                builder.show();
            }
        });

    }

    public void getDataMeja(){
        loading = ProgressDialog.show(MasukMenu.this, null, "Please Wait...", true, false);
        mApiService.getDataMeja().enqueue(new Callback<BasicListResponse<Meja>>() {
            @Override
            public void onResponse(Call<BasicListResponse<Meja>> call, Response<BasicListResponse<Meja>> response) {
                if(response.body().isSuccess()){
                    ArrayList<Meja> mejas= response.body().getDatas();
                    dataMeja = new ArrayList<>();
                    dataIdMeja = new ArrayList<>();
                    for(int i=0;i<mejas.size();i++){
                        dataMeja.add("Meja no " + mejas.get(i).getNo_meja() + " ( kapasitas " + mejas.get(i).getJumlah_kursi() + " orang )");
                        dataIdMeja.add(mejas.get(i).getId());
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,dataMeja);
                    sp_meja.setAdapter(arrayAdapter);
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BasicListResponse<Meja>> call, Throwable t) {

            }
        });
    }
    public void setMeja(){
        loading = ProgressDialog.show(MasukMenu.this, null, "Please Wait...", true, false);
        String id_meja = dataIdMeja.get(sp_meja.getSelectedItemPosition());
        String nama = et_nama.getText().toString();
        String token = FirebaseInstanceId.getInstance().getToken();
        mApiService.setMeja(id_meja,nama,token).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if(response.body().isSuccess()){

                    SharedPreferences settings = getSharedPreferences("myPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("ordermeja_id", response.body().getMessage());
                    editor.commit();
                    loading.dismiss();
                    Intent intent = new Intent(MasukMenu.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    loading.dismiss();
                    Widget.showMessageBoxErrorServer(MasukMenu.this);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "firebaseNotifChannel";
            String description = "Receive Firebase notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}