package com.croods.eventmanagement.api;

public class APIClient
{
    private static retrofit2.Retrofit retrofit = null;

    public static retrofit2.Retrofit getClient(String baseUrl) {
        okhttp3.logging.HttpLoggingInterceptor interceptor = new okhttp3.logging.HttpLoggingInterceptor();


        interceptor.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY);
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
