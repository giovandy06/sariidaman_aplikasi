package com.sariidaman.giovan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sariidaman.giovan.MainActivity;
import com.sariidaman.giovan.R;
import com.sariidaman.giovan.helper.DatabaseHandler;
import com.sariidaman.giovan.helper.MyFunction;
import com.sariidaman.giovan.model.Order;

import java.util.List;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder>  {
    Context ctx;
    List<Order> orders;

    public AdapterOrder(@NonNull Context context, List<Order> orderSqlLites_) {
        ctx = context;
        this.orders = orderSqlLites_;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_close;
        public ImageView img_plus;
        public ImageView img_minus;
        public TextView txt_nama;
        public TextView txt_harga;
        public TextView txt_qty;
        public TextView txt_total;


        public ViewHolder(View v) {
            super(v);
            img_close = v.findViewById(R.id.img_close);
            img_plus = v.findViewById(R.id.img_plus);
            img_minus = v.findViewById(R.id.img_minus);
            txt_nama = v.findViewById(R.id.txt_nama);
            txt_harga = v.findViewById(R.id.txt_harga);
            txt_qty = v.findViewById(R.id.txt_qty);
            txt_total = v.findViewById(R.id.txt_total);
        }
    }



    @NonNull
    @Override
    public AdapterOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterOrder.ViewHolder vh = new AdapterOrder.ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrder.ViewHolder holder, int position) {
        final Order order = orders.get(position);
        int total = order.getHarga() * order.getQty();
        holder.txt_nama.setText(order.getNama_masakan());
        holder.txt_harga.setText(MyFunction.formatRupiah(order.getHarga()));
        holder.txt_qty.setText(String.valueOf(order.getQty()));
        holder.txt_total.setText(MyFunction.formatRupiah(total));

        holder.img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(holder.txt_qty.getText().toString());
                holder.txt_qty.setText(String.valueOf(count + 1));
                order.setQty(count + 1);
                DatabaseHandler databaseHandler = new DatabaseHandler(ctx);
                databaseHandler.updateQty(order);
                com.sariidaman.giovan.Order.order_activity.getOrder();
            }
        });
        holder.img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(holder.txt_qty.getText().toString());
                if(count > 1){
                    holder.txt_qty.setText(String.valueOf(count - 1));
                    order.setQty(count - 1);
                    DatabaseHandler databaseHandler = new DatabaseHandler(ctx);
                    databaseHandler.updateQty(order);
                    com.sariidaman.giovan.Order.order_activity.getOrder();
                }
            }
        });
        //Glide.with(ctx).load(ApiClient.BASE_URL + "uploads/" +  orderSqlLite.getGambar()).into(holder.img_close);
        holder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler databaseHandler = new DatabaseHandler(ctx);
                databaseHandler.deleteModel(order);
                com.sariidaman.giovan.Order.order_activity.getOrderAPI();
                com.sariidaman.giovan.Order.order_activity.getOrder();
            }
        });

    }



    public int getItemCount() {
        return orders.size();
    }

}

