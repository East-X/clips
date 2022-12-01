package com.eastx7.clips.api

import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import com.eastx7.clips.data.Constants.SERVER_URL
import com.eastx7.clips.data.Clips
import java.net.CookieManager
import java.net.CookiePolicy

interface ApiClips {

    @GET(".")
    suspend fun getClips(
        @Query("group") group: String,
        @Query("category_id") categoryId: String
    ): List<Clips>?


    companion object {
        fun getRetrofitBuilder(cookieManager: CookieManager): Retrofit.Builder {
            //TODO: remove logger in production
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .cookieJar(JavaNetCookieJar(cookieManager))
                .build()

            return Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(client)
        }
    }
}

interface GsonService : ApiClips {
    companion object {
        fun create(cookieManager: CookieManager): GsonService {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            return ApiClips.getRetrofitBuilder(cookieManager)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(GsonService::class.java)
        }
    }
}