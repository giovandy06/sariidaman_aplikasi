package com.sariidaman.giovan.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sariidaman.giovan.MainActivity;
import com.sariidaman.giovan.R;
import com.sariidaman.giovan.helper.ApiClient;
import com.sariidaman.giovan.helper.ApiService;
import com.sariidaman.giovan.helper.MyFunction;
import com.sariidaman.giovan.model.Masakan;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMasakan extends RecyclerView.Adapter<AdapterMasakan.ViewHolder>  {
    Context ctx;
    ArrayList<Masakan> Masakans;

    ApiService mApiService;

    public AdapterMasakan(@NonNull Context context, ArrayList<Masakan> Masakans_) {
        ctx = context;
        this.Masakans = Masakans_;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_item;
        public TextView txt_nama;
        public TextView txt_harga;


        public ViewHolder(View v) {
            super(v);
            img_item = v.findViewById(R.id.img_item);
            txt_nama = v.findViewById(R.id.txt_nama);
            txt_harga = v.findViewById(R.id.txt_harga);
            mApiService = ApiClient.getAPIService();
        }
    }



    @NonNull
    @Override
    public AdapterMasakan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_masakan, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterMasakan.ViewHolder vh = new AdapterMasakan.ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMasakan.ViewHolder holder, int position) {
        final Masakan masakan = Masakans.get(position);
        holder.txt_nama.setText(masakan.getNama());
        holder.txt_harga.setText(MyFunction.formatRupiah(Integer.parseInt(masakan.getHarga())));
        Glide.with(ctx).load(ApiClient.BASE_URL + "uploads/" +  masakan.getGambar()).into(holder.img_item);
        if(masakan.getStatus() == 1){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.MA.showModalAddProduct(masakan);
                }
            });
        }else{
            holder.txt_harga.setText("STOK HABIS");
        }
    }



    public int getItemCount() {
        return Masakans.size();
    }

}
