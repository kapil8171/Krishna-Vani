package com.example.shreebhagavatgita.datasource.sp

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {





         val sharedPreferences : SharedPreferences by lazy {
                 context.getSharedPreferences("saved_chapters", Context.MODE_PRIVATE)
         }


    fun getAllSavedChaptersKeySP():Set<String> {
        return sharedPreferences.all.keys
    }

    fun putSavedChaptersSP(key: String,value: Int){
        sharedPreferences.edit().putInt(key,value).apply()
    }

    fun deleteSavedChapterFromSP(key: String){
        sharedPreferences.edit().remove(key).apply()
    }
}