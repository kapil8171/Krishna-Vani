package com.example.shreebhagavatgita.datasource.api

import com.example.shreebhagavatgita.models.ChaptersItem
import com.example.shreebhagavatgita.models.VersesItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


interface ApiInterface {



    @GET("/v2/chapters/")
    fun getAllChapter(): Call<List<ChaptersItem>>


    @GET("/v2/chapters/{chapterNumber}/verses/")
    fun getVerses(@Path("chapterNumber") chapterNumber:Int) :Call<List<VersesItem>>

    @GET("/v2/chapters/{chapterNum}/verses/{verseNum}/")
    fun getAParticularVerse(
        @Path("chapterNum") chapterNumber: Int,
        @Path("verseNum") verseNumber: Int,
    ) : Call<VersesItem>

}