package com.example.shreebhagavatgita.datasource.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {



    val headers = mapOf<String,String>(
        "Accept" to "application/json",
        "x-rapidapi-key" to "cb18f5ffd6mshcdae6b22d89b50fp19d11ajsnea12d3d1d78c",
        "x-rapidapi-host" to  "bhagavad-gita3.p.rapidapi.com"
    )

    //using Interceptor
    val client = OkHttpClient.Builder().apply {
        addInterceptor{chain->
            val newRequest = chain.request().newBuilder().apply {
                headers.forEach{key,value -> addHeader(key,value) }
            }.build()
            chain.proceed(newRequest)
        }
    }.build()


    val api : ApiInterface by lazy {
            Retrofit.Builder()
                .baseUrl("https://bhagavad-gita3.p.rapidapi.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
    }
}