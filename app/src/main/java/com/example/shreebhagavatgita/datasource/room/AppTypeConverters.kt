package com.example.shreebhagavatgita.datasource.api.room

import androidx.room.TypeConverter
import com.example.shreebhagavatgita.models.Commentary
import com.example.shreebhagavatgita.models.Translation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppTypeConverters {

    @TypeConverter
    fun fromListToString(list: List<String>) :String {
       return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToList(string: String) : List<String>{
        return Gson().fromJson(string,object :TypeToken<List<String>>(){}.type)
    }

    @TypeConverter
    fun fromTransToString(list: List<Translation>) :String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToTrans(string: String) : List<Translation>{
        return Gson().fromJson(string,object :TypeToken<List<Translation>>(){}.type)
    }

    @TypeConverter
    fun fromCommenToString(list: List<Commentary>) :String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToCommen(string: String) : List<Commentary>{
        return Gson().fromJson(string,object :TypeToken<List<Commentary>>(){}.type)
    }


}