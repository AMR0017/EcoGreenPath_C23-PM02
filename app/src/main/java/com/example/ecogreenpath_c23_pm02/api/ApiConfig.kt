package com.example.ecogreenpath_c23_pm02.api

import android.content.Context
import com.example.ecogreenpath_c23_pm02.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



object ApiConfig {
    private lateinit var apiService: ApiService

    fun getApiService(context: Context): ApiService {
        if (!ApiConfig::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.APP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }
        return apiService
    }

    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level =
                        HttpLoggingInterceptor.Level.BODY
                }
            })
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(AuthenticationToken(context))
            .build()
    }
}