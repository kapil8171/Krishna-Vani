package com.example.shreebhagavatgita.repository

import androidx.lifecycle.LiveData
import com.example.shreebhagavatgita.datasource.api.ApiUtilities
import com.example.shreebhagavatgita.datasource.api.room.SavedChapters
import com.example.shreebhagavatgita.datasource.api.room.SavedChaptersDao
import com.example.shreebhagavatgita.datasource.api.room.SavedVerses
import com.example.shreebhagavatgita.datasource.api.room.SavedVersesDao
import com.example.shreebhagavatgita.datasource.sp.SharedPreferencesManager
import com.example.shreebhagavatgita.models.ChaptersItem
import com.example.shreebhagavatgita.models.VersesItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository(val savedChaptersDao: SavedChaptersDao, val savedVersesDao: SavedVersesDao, val sharedPreferencesManager: SharedPreferencesManager) {

    fun getAllChapter(): Flow<List<ChaptersItem>> = callbackFlow {
         val callback = object : Callback<List<ChaptersItem>>{
             override fun onResponse(
                 call: Call<List<ChaptersItem>>,
                 response: Response<List<ChaptersItem>>
             ) {
                   if (response.isSuccessful && response.body() != null) {
                       trySend(response.body()!!)
                       close()
                   }
             }

             override fun onFailure(call: Call<List<ChaptersItem>>, t: Throwable) {
                        close(t)
             }
         }
           ApiUtilities.api.getAllChapter().enqueue(callback)
            awaitClose {}
    }

    fun getVerses(chapterNumber: Int): Flow<List<VersesItem>> = callbackFlow {
        val callback = object : Callback<List<VersesItem>>{
            override fun onResponse(
                call: Call<List<VersesItem>>,
                response: Response<List<VersesItem>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<List<VersesItem>>, t: Throwable) {
                      close(t)
            }
        }
        ApiUtilities.api.getVerses(chapterNumber).enqueue(callback)
        awaitClose {}

    }

    fun getAParticularVerse(chapterNumber: Int, verseNumber: Int): Flow<VersesItem> = callbackFlow {
        val callback = object : Callback<VersesItem>{
            override fun onResponse(call: Call<VersesItem>, response: Response<VersesItem>) {

                if (response.isSuccessful && response.body()!=null){
                     trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<VersesItem>, t: Throwable) {
                close(t)
            }
        }

          ApiUtilities.api.getAParticularVerse(chapterNumber,verseNumber).enqueue(callback)
        awaitClose{}
    }



    //saved chapters
    suspend fun insertChapters(savedChapters: SavedChapters) = savedChaptersDao.insertChapters(savedChapters)

    fun getSavedChapters(): LiveData<List<SavedChapters>> = savedChaptersDao.getSavedChapters()

    fun getAParticularChapter(chapter_number :Int) : LiveData<SavedChapters> = savedChaptersDao.getAParticularChapter(chapter_number)

    suspend fun deleteChapter(id:Int) = savedChaptersDao.deleteChapter(id)

    // saved verses
    suspend fun deleteAParticularVerse(chapterNumber: Int,verseNumber: Int) = savedVersesDao.deleteAParticularVerse(chapterNumber,verseNumber)
    fun getParticularVerse(chapterNumber: Int, verseNumber: Int): LiveData<SavedVerses> = savedVersesDao.getParticularVerse(chapterNumber,verseNumber)
    fun getAllEnglishVerse() : LiveData<List<SavedVerses>> = savedVersesDao.getAllEnglishVerse()
    suspend fun insertEnglishVerse( versesInEnglish: SavedVerses)= savedVersesDao.insertEnglishVerse(versesInEnglish)



    //saved chapters in sp
    fun getAllSavedChaptersKeySP():Set<String> = sharedPreferencesManager.getAllSavedChaptersKeySP()
    fun putSavedChaptersSP(key: String,value: Int) = sharedPreferencesManager.putSavedChaptersSP(key,value)
    fun deleteSavedChapterFromSP(key: String) = sharedPreferencesManager.deleteSavedChapterFromSP(key)

}