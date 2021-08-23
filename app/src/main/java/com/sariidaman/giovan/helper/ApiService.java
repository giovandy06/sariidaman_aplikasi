package com.sariidaman.giovan.helper;

import com.sariidaman.giovan.model.BasicListResponse;
import com.sariidaman.giovan.model.BasicResponse;
import com.sariidaman.giovan.model.KategoriMasakan;
import com.sariidaman.giovan.model.Masakan;
import com.sariidaman.giovan.model.Meja;
import com.sariidaman.giovan.model.Order;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiService {

    @GET("getDataMeja")
    Call<BasicListResponse<Meja>> getDataMeja();

    @FormUrlEncoded
    @POST("setMeja")
    Call<BasicResponse> setMeja(@Field("id_meja") String id_meja,
                                @Field("nama") String nama,
                                @Field("token_fm") String token_fm);


    @FormUrlEncoded
    @POST("setProsesPesanan")
    Call<BasicResponse> setProsesPesanan(@Field("ordermeja_id") String ordermeja_id,
                                         @Field("id_masakan") String id_masakan,
                                         @Field("harga") String harga,
                                         @Field("qty") String qty,
                                         @Field("total_harga_bayar") String total_harga_bayar);


    @GET("getDataKategoriMasakan")
    Call<BasicListResponse<KategoriMasakan>> getDataKategoriMasakan();

    @FormUrlEncoded
    @POST("getDataMasakan")
    Call<BasicListResponse<Masakan>> getDataMasakan(@Field("id_kategori_masakan") String id_kategori_masakan);


    @FormUrlEncoded
    @POST("getDataOrder")
    Call<BasicListResponse<Order>> getDataOrder(@Field("ordermeja_id") String ordermeja_id);


    @FormUrlEncoded
    @POST("cekStatusTransaksi")
    Call<BasicResponse> cekStatusTransaksi(@Field("ordermeja_id") String ordermeja_id);

    @Multipart
    @POST("prosesPembayaran.php")
    Call<BasicResponse>  prosesPembayaran(
            @PartMap Map<String, RequestBody> post_body,
            @Part MultipartBody.Part Image
    );

}

