package com.sariidaman.giovan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sariidaman.giovan.R;
import com.sariidaman.giovan.helper.DatabaseHandler;
import com.sariidaman.giovan.helper.MyFunction;
import com.sariidaman.giovan.model.Order;

import java.util.List;

public class AdapterOrderApi extends RecyclerView.Adapter<AdapterOrderApi.ViewHolder>  {
    Context ctx;
    List<Order> orders;

    public AdapterOrderApi(@NonNull Context context, List<Order> orderSqlLites_) {
        ctx = context;
        this.orders = orderSqlLites_;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_nama;
        public TextView txt_harga;
        public TextView txt_qty;
        public TextView txt_total;


        public ViewHolder(View v) {
            super(v);
            txt_nama = v.findViewById(R.id.txt_nama);
            txt_harga = v.findViewById(R.id.txt_harga);
            txt_qty = v.findViewById(R.id.txt_qty);
            txt_total = v.findViewById(R.id.txt_total);
        }
    }



    @NonNull
    @Override
    public AdapterOrderApi.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_api, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterOrderApi.ViewHolder vh = new AdapterOrderApi.ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderApi.ViewHolder holder, int position) {
        final Order order = orders.get(position);
        int total = order.getHarga() * order.getQty();
        holder.txt_nama.setText(order.getNama_masakan());
        holder.txt_harga.setText(MyFunction.formatRupiah(order.getHarga()));
        holder.txt_qty.setText(" X " + String.valueOf(order.getQty()));
        holder.txt_total.setText(MyFunction.formatRupiah(total));


    }



    public int getItemCount() {
        return orders.size();
    }

}

