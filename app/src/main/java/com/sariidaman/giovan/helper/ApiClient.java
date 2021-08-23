package com.sariidaman.giovan.helper;

/**
 * Created by root on 2/3/17.
 */

public class ApiClient {
    public static final String SITE_URL = "http://202.157.186.36/restoran/API/ApiController/";
    public static final String BASE_URL = "http://202.157.186.36/restoran/";

    // Mendeklarasikan Interface BaseApiService
    public static ApiService getAPIService(){
        return RetrofitClient.getClient(SITE_URL).create(ApiService.class);
    }

}
