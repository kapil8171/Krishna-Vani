package com.example.shreebhagavatgita.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shreebhagavatgita.datasource.api.room.AppDatabase
import com.example.shreebhagavatgita.datasource.api.room.SavedChapters
import com.example.shreebhagavatgita.datasource.api.room.SavedVerses
import com.example.shreebhagavatgita.datasource.sp.SharedPreferencesManager
import com.example.shreebhagavatgita.models.ChaptersItem
import com.example.shreebhagavatgita.models.VersesItem
import com.example.shreebhagavatgita.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class MainViewModel(application: Application): AndroidViewModel(application) {


    val savedChaptersDao = AppDatabase.getDatabaseInstance(application)?.savedChaptersDao()
    val savedVersesDao = AppDatabase.getDatabaseInstance(application)?.savedVersesDao()

    val sharedPreferencesManager = SharedPreferencesManager(application)

       val appRepository = AppRepository(savedChaptersDao!!, savedVersesDao!!, sharedPreferencesManager)

    fun getAllChapter(): Flow<List<ChaptersItem>> = appRepository.getAllChapter()
    fun getVerses(chapterNumber: Int): Flow<List<VersesItem>> = appRepository.getVerses(chapterNumber)
    fun getAParticularVerse(chapterNumber: Int, verseNumber: Int): Flow<VersesItem> = appRepository.getAParticularVerse(chapterNumber, verseNumber)


        //saved chapters
    suspend fun insertChapters(savedChapters: SavedChapters) = appRepository.insertChapters(savedChapters)

    fun getSavedChapters(): LiveData<List<SavedChapters>> = appRepository.getSavedChapters()

    fun getAParticularChapter(chapter_number :Int) : LiveData<SavedChapters> = appRepository.getAParticularChapter(chapter_number)
    suspend fun deleteChapter(id:Int) = appRepository.deleteChapter(id)



    //saved verses
    suspend fun deleteAParticularVerse(chapterNumber: Int,verseNumber: Int) = appRepository.deleteAParticularVerse(chapterNumber,verseNumber)

    fun getParticularVerse(chapterNumber: Int, verseNumber: Int): LiveData<SavedVerses> = appRepository.getParticularVerse(chapterNumber,verseNumber)

    fun getAllEnglishVerse() : LiveData<List<SavedVerses>> = appRepository.getAllEnglishVerse()

    suspend fun insertEnglishVerse( versesInEnglish: SavedVerses)= appRepository.insertEnglishVerse(versesInEnglish)


    // saved chapters in SharedPrefrence
    fun getAllSavedChaptersKeySP():Set<String> = appRepository.getAllSavedChaptersKeySP()
    fun putSavedChaptersSP(key: String,value: Int) = appRepository.putSavedChaptersSP(key,value)
    fun deleteSavedChapterFromSP(key: String) = appRepository.deleteSavedChapterFromSP(key)

}