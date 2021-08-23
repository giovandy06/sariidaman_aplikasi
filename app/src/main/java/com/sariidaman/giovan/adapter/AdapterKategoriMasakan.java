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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sariidaman.giovan.MainActivity;
import com.sariidaman.giovan.R;
import com.sariidaman.giovan.helper.AdapterItemClickListener;
import com.sariidaman.giovan.helper.ApiClient;
import com.sariidaman.giovan.helper.ApiService;
import com.sariidaman.giovan.model.KategoriMasakan;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterKategoriMasakan extends RecyclerView.Adapter<AdapterKategoriMasakan.ViewHolder>  {
    int selectedPosition=-1;
    boolean inFirst = true;
    Context ctx;
    ArrayList<KategoriMasakan> KategoriMasakans;


    ApiService mApiService;
  /*  AdapterItemClickListener itemClickListener;

    public void setItemListener(AdapterItemClickListener listener){
        itemClickListener = listener;
    }
*/
    public AdapterKategoriMasakan(@NonNull Context context, ArrayList<KategoriMasakan> KategoriMasakans_) {
        ctx = context;
        this.KategoriMasakans = KategoriMasakans_;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btn_kategori;


        public ViewHolder(View v) {
            super(v);
            btn_kategori = v.findViewById(R.id.btn_kategori);
            mApiService = ApiClient.getAPIService();
        }
    }



    @NonNull
    @Override
    public AdapterKategoriMasakan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kategori, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterKategoriMasakan.ViewHolder vh = new AdapterKategoriMasakan.ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterKategoriMasakan.ViewHolder holder, int position) {
        final KategoriMasakan kategoriMasakan = KategoriMasakans.get(position);
        holder.btn_kategori.setText(kategoriMasakan.getNama());

        if(selectedPosition==position){
            holder.btn_kategori.setTextColor(ctx.getResources().getColor(R.color.white));
            holder.btn_kategori.setBackground(ctx.getResources().getDrawable(R.drawable.roundedbutton_active));
        }
        else{
            holder.btn_kategori.setTextColor(ctx.getResources().getColor(R.color.secondary));
            holder.btn_kategori.setBackground(ctx.getResources().getDrawable(R.drawable.roundedbutton));
        }
        if(inFirst){
            if(kategoriMasakan.getNama().equals("All")){
                holder.btn_kategori.setTextColor(ctx.getResources().getColor(R.color.white));
                holder.btn_kategori.setBackground(ctx.getResources().getDrawable(R.drawable.roundedbutton_active));
                inFirst = false;

            }
        }

        holder.btn_kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kategoriMasakan.getNama().equals("All")){
                    MainActivity.MA.getDataMasakan();
                }else{
                    MainActivity.MA.getDataMasakan(kategoriMasakan.getId());
                }
                selectedPosition=position;
                notifyDataSetChanged();

            }
        });
    }



    public int getItemCount() {
        return KategoriMasakans.size();
    }

}
