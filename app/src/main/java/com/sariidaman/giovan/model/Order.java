package com.sariidaman.giovan.model;

public class Order {
    int id;
    String id_masakan;
    String nama_masakan;
    int harga;
    int qty;
    String gambar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_masakan() {
        return id_masakan;
    }

    public void setId_masakan(String id_masakan) {
        this.id_masakan = id_masakan;
    }

    public String getNama_masakan() {
        return nama_masakan;
    }

    public void setNama_masakan(String nama_masakan) {
        this.nama_masakan = nama_masakan;
    }


    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
