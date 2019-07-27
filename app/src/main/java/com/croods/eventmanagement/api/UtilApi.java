package com.croods.eventmanagement.api;

public class UtilApi {

   // private static final String BASE_URL_API = "http://192.168.2.100:8080/api/";
   private static final String BASE_URL_API = "http://prageuevents.croodstech.com/api/";

    // Interface BaseApiService
    public static APIInterface getAPIService(){
        return APIClient.getClient(BASE_URL_API).create(APIInterface.class);
    }
}
