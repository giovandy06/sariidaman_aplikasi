package com.sariidaman.giovan.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sariidaman.giovan.model.Order;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // static variable
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "db_order";

    // table name
    private static final String TABLE_NAME = "orders";

    // column tables
    private static final String KEY_ID = "id";
    private static final String KEY_ID_MASAKAN = "id_masakan";
    private static final String KEY_NAME = "nama_masakan";
    private static final String KEY_HARGA = "harga";
    private static final String KEY_QTY = "qty";
    private static final String KEY_GAMBAR = "gambar";


    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID_MASAKAN + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_HARGA + " INTEGER,"
                + KEY_QTY + " INTEGER,"
                + KEY_GAMBAR + " TEXT" + ")";

        db.execSQL(create_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addData(Order order){
        SQLiteDatabase db  = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_MASAKAN, order.getId_masakan());
        values.put(KEY_NAME, order.getNama_masakan());
        values.put(KEY_HARGA, order.getHarga());
        values.put(KEY_QTY, order.getQty());
        values.put(KEY_GAMBAR, order.getGambar());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public boolean checkIfDataExist(String id_masakan) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID_MASAKAN}, KEY_ID_MASAKAN + "=?",
                new String[] { String.valueOf(id_masakan) }, null, null, null, null);
        if (cursor != null){
            if(cursor.moveToFirst()){
                return true;
            }else{
                return false;
            }
        }
        else{
            return false;
        }
    }
    public int getQty(String id_masakan) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_QTY }, KEY_ID_MASAKAN + "=?",
                new String[] { String.valueOf(id_masakan) }, null, null, null, null);
        if (cursor != null){
            if(cursor.moveToFirst()){
              return cursor.getInt(0);
            }
        }

        return  0;

    }

    public int updateQty(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        int qty = order.getQty();

        values.put(KEY_QTY, qty);
        values.put(KEY_GAMBAR, order.getGambar());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID_MASAKAN + " = ?",
                new String[] { String.valueOf(order.getId_masakan()) });
    }
    public int updateData(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        int qty = getQty(order.getId_masakan()) + order.getQty();
        Log.d("QTY", String.valueOf(qty));
        values.put(KEY_ID_MASAKAN, order.getId_masakan());
        values.put(KEY_NAME, order.getNama_masakan());
        values.put(KEY_HARGA, order.getHarga());
        values.put(KEY_QTY, qty);
        values.put(KEY_GAMBAR, order.getGambar());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID_MASAKAN + " = ?",
                new String[] { String.valueOf(order.getId_masakan()) });
    }
    // get All Record
    public List<Order> getData() {
        List<Order> orders = new ArrayList<Order>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(Integer.parseInt(cursor.getString(0)));
                order.setId_masakan(cursor.getString(1));
                order.setNama_masakan(cursor.getString(2));
                order.setHarga(cursor.getInt(3));
                order.setQty(cursor.getInt(4));
                order.setGambar(cursor.getString(5));

                orders.add(order);
            } while (cursor.moveToNext());
        }

        // return contact list
        return orders;
    }
    public void deleteModel(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(order.getId()) });
        db.close();
    }
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " >= ?",
                new String[] { String.valueOf(0) });
        db.close();
    }
}
