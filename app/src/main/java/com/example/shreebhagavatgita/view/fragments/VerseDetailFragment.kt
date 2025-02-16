package com.example.shreebhagavatgita.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.shreebhagavatgita.NetworkManager
import com.example.shreebhagavatgita.R
import com.example.shreebhagavatgita.databinding.FragmentVerseDetailBinding
import com.example.shreebhagavatgita.datasource.api.room.SavedVerses
import com.example.shreebhagavatgita.models.Commentary
import com.example.shreebhagavatgita.models.Translation
import com.example.shreebhagavatgita.models.VersesItem
import com.example.shreebhagavatgita.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VerseDetailFragment : Fragment() {

    private lateinit var binding: FragmentVerseDetailBinding
    private val viewModel : MainViewModel by viewModels()
    private var chapterNum = 0
    private var verseNum = 0
    private var verseDetail = MutableLiveData<VersesItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

         binding = FragmentVerseDetailBinding.inflate(layoutInflater)

        changeStatusBarColor()
        getAndSetChapAndVerse()
        onReadMoreClicked()
        getData()
        onSavedVerse()
        return binding.root
    }

    private fun getData() {
        val bundle = arguments
        val showRoomData = bundle?.getBoolean("showRoomData",false)

        if (showRoomData==true){
            binding.ivFavoriteVerseFilled.visibility= View.GONE
            binding.ivFavoriteVerse.visibility= View.GONE

            viewModel.getParticularVerse(chapterNum,verseNum).observe(viewLifecycleOwner){ verse->

                binding.tvVerseText.text = verse.text
                binding.tvTransliterationIfEnglish.text = verse.transliteration
                binding.tvWordIfEnglish.text = verse.word_meanings

                val englishTranslationList = arrayListOf<Translation>()
                for (i in verse.translations){
                    if (i.language == "hindi"){
                        englishTranslationList.add(i)
                    }
                }

                val englishTranslationListSize = englishTranslationList.size
                if (englishTranslationList.isNotEmpty()){
                    binding.tvAuthorName.text = englishTranslationList[0].author_name
                    binding.tvText.text = englishTranslationList[0].description
                    if (englishTranslationListSize==1){
                        binding.fabTranslationRight.visibility = View.GONE
                    }

                    var  i=0
                    binding.fabTranslationRight.setOnClickListener {
                        if (i <englishTranslationListSize-1){
                            i++
                            binding.tvAuthorName.text = englishTranslationList[i].author_name
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationLeft.visibility = View.VISIBLE

                            if (i == englishTranslationListSize-1) binding.fabTranslationRight.visibility =
                                View.GONE
                        }
                    }

                    binding.fabTranslationLeft.setOnClickListener {
                        if (i>0){
                            i--
                            binding.tvAuthorName.text = englishTranslationList[i].author_name
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationRight.visibility = View.VISIBLE
                            if (i==0) binding.fabTranslationLeft.visibility = View.GONE
                        }
                    }

                }

                val englishCommentaryList = arrayListOf<Commentary>()
                for (i in verse.commentaries){
                    if (i.language == "hindi"){
                        englishCommentaryList.add(i)
                    }
                }

                val englishCommentaryListSize = englishCommentaryList.size

                if (englishCommentaryList.isNotEmpty()){
                    binding.tvAuthorNameCommentary.text = englishCommentaryList[0].author_name
                    binding.tvTextCommentary.text = englishCommentaryList[0].description
                    if (englishCommentaryListSize==1) binding.fabCommentaryRight.visibility =
                        View.GONE

                    var  i=0
                    binding.fabCommentaryRight.setOnClickListener {
                        if (i <englishCommentaryListSize-1){
                            i++
                            binding.tvAuthorCommentary.text = englishCommentaryList[i].author_name
                            binding.tvTextCommentary.text = englishCommentaryList[i].description
                            binding.fabCommentaryLeft.visibility = View.VISIBLE

                            if (i == englishCommentaryListSize-1) binding.fabCommentaryRight.visibility =
                                View.GONE
                        }
                    }

                    binding.fabCommentaryLeft.setOnClickListener {
                        if (i>0){
                            i--
                            binding.tvAuthorNameCommentary.text = englishCommentaryList[i].author_name
                            binding.tvTextCommentary.text = englishCommentaryList[i].description
                            binding.fabCommentaryRight.visibility = View.VISIBLE
                            if (i==0) binding.fabCommentaryLeft.visibility = View.GONE
                        }
                    }

                }

                binding.progressbar.visibility = View.GONE

                binding.tvVerseNumber.visibility = View.VISIBLE
                binding.tvVerseText.visibility = View.VISIBLE
                binding.tvTransliterationIfEnglish.visibility = View.VISIBLE
                binding.tvWordIfEnglish.visibility = View.VISIBLE
                binding.view.visibility = View.VISIBLE
                binding.tvTranslation.visibility = View.VISIBLE
                binding.clTranslation.visibility = View.VISIBLE
                binding.tvCommentries.visibility = View.VISIBLE
                binding.clCommentries.visibility = View.VISIBLE
                binding.backgroundImage.visibility = View.VISIBLE
                binding.ivFavoriteVerse.visibility = View.GONE


            }
        }
        else{
            checkInternet()
        }

    }

    private fun onSavedVerse(){

          binding.ivFavoriteVerseFilled.setOnClickListener {
              binding.ivFavoriteVerse.visibility = View.VISIBLE
              binding.ivFavoriteVerseFilled.visibility = View.GONE
              deleteVerse()
          }

          binding.ivFavoriteVerse.setOnClickListener{
                  binding.ivFavoriteVerse.visibility = View.GONE
                  binding.ivFavoriteVerseFilled.visibility = View.VISIBLE
              savingVerseDetails()
          }

      }

    private fun deleteVerse() {
        lifecycleScope.launch {
            viewModel.deleteAParticularVerse(chapterNum,verseNum)
        }

    }

    private fun savingVerseDetails() {
        verseDetail.observe(viewLifecycleOwner){

            val englishCommentaryList = arrayListOf<Commentary>()
            for (i in it.commentaries){
                if (i.language == "hindi"){
                    englishCommentaryList.add(i)
                }
            }
            val englishTranslationList = arrayListOf<Translation>()
            for (i in it.translations){
                if (i.language == "hindi"){
                    englishTranslationList.add(i)
                }
            }

               val savedVerses = SavedVerses(
                   it.chapter_number,
                   englishCommentaryList,
                   it.id,
                   it.slug,
                   it.text,
                   englishTranslationList,
                   it.transliteration,
                   it.verse_number,
                   it.word_meanings
               )

            lifecycleScope.launch {
                viewModel.insertEnglishVerse(savedVerses)
            }
        }

    }

    private fun checkInternet() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner){
            if (it == true){
                binding.tvShowingMessage.visibility = View.GONE
                binding.progressbar.visibility = View.VISIBLE
                getVerseDetail()
            }
            else{

                binding.tvShowingMessage.visibility = View.VISIBLE
                binding.progressbar.visibility = View.GONE

                binding.tvVerseNumber.visibility = View.GONE
                binding.tvVerseText.visibility = View.GONE
                binding.tvTransliterationIfEnglish.visibility = View.GONE
                binding.tvWordIfEnglish.visibility = View.GONE
                binding.view.visibility = View.GONE
                binding.tvTranslation.visibility = View.GONE
                binding.clTranslation.visibility = View.GONE
                binding.tvCommentries.visibility = View.GONE
                binding.clCommentries.visibility = View.GONE
                binding.backgroundImage.visibility = View.GONE
                binding.ivFavoriteVerse.visibility = View.GONE
            }
        }
    }



    private fun onReadMoreClicked() {
        var isExpended = false
        binding.tvSeeMore.setOnClickListener{
            if (!isExpended){
                binding.tvTextCommentary.maxLines =100
                isExpended=true
                binding.tvSeeMore.text = "ReadLess"
            }
            else{
                binding.tvTextCommentary.maxLines=4
                isExpended=false
                binding.tvSeeMore.text = "ReadMore"

            }
        }
    }

    private fun getVerseDetail() {
        lifecycleScope.launch {

            try {
                viewModel.getAParticularVerse(chapterNum,verseNum).collect{verse->
                    verseDetail.postValue(verse)

                    binding.tvVerseText.text = verse.text
                    binding.tvTransliterationIfEnglish.text = verse.transliteration
                    binding.tvWordIfEnglish.text = verse.word_meanings

                    val englishTranslationList = arrayListOf<Translation>()
                    for (i in verse.translations){
                        if (i.language == "hindi"){
                            englishTranslationList.add(i)
                        }
                    }

                    val englishTranslationListSize = englishTranslationList.size
                    if (englishTranslationList.isNotEmpty()){
                        binding.tvAuthorName.text = englishTranslationList[0].author_name
                        binding.tvText.text = englishTranslationList[0].description
                        if (englishTranslationListSize==1){
                            binding.fabTranslationRight.visibility = View.GONE
                        }

                        var  i=0
                        binding.fabTranslationRight.setOnClickListener {
                            if (i <englishTranslationListSize-1){
                                i++
                                binding.tvAuthorName.text = englishTranslationList[i].author_name
                                binding.tvText.text = englishTranslationList[i].description
                                binding.fabTranslationLeft.visibility = View.VISIBLE

                                if (i == englishTranslationListSize-1) binding.fabTranslationRight.visibility =
                                    View.GONE
                            }
                        }

                        binding.fabTranslationLeft.setOnClickListener {
                            if (i>0){
                                i--
                                binding.tvAuthorName.text = englishTranslationList[i].author_name
                                binding.tvText.text = englishTranslationList[i].description
                                binding.fabTranslationRight.visibility = View.VISIBLE
                                if (i==0) binding.fabTranslationLeft.visibility = View.GONE
                            }
                        }

                    }

                    val englishCommentaryList = arrayListOf<Commentary>()
                    for (i in verse.commentaries){
                        if (i.language == "hindi"){
                            englishCommentaryList.add(i)
                        }
                    }

                    val englishCommentaryListSize = englishCommentaryList.size

                    if (englishCommentaryList.isNotEmpty()){
                        binding.tvAuthorNameCommentary.text = englishCommentaryList[0].author_name
                        binding.tvTextCommentary.text = englishCommentaryList[0].description
                        if (englishCommentaryListSize==1) binding.fabCommentaryRight.visibility =
                            View.GONE

                        var  i=0
                        binding.fabCommentaryRight.setOnClickListener {
                            if (i <englishCommentaryListSize-1){
                                i++
                                binding.tvAuthorCommentary.text = englishCommentaryList[i].author_name
                                binding.tvTextCommentary.text = englishCommentaryList[i].description
                                binding.fabCommentaryLeft.visibility = View.VISIBLE

                                if (i == englishCommentaryListSize-1) binding.fabCommentaryRight.visibility =
                                    View.GONE
                            }
                        }

                        binding.fabCommentaryLeft.setOnClickListener {
                            if (i>0){
                                i--
                                binding.tvAuthorNameCommentary.text = englishCommentaryList[i].author_name
                                binding.tvTextCommentary.text = englishCommentaryList[i].description
                                binding.fabCommentaryRight.visibility = View.VISIBLE
                                if (i==0) binding.fabCommentaryLeft.visibility = View.GONE
                            }
                        }

                    }

                }
            }
            catch (e: java.net.UnknownHostException) {
                // No internet connection
                Log.e("TAG", "Network error: No Internet Connection")
                showNetworkError()
            } catch (e: java.net.SocketTimeoutException) {
                // Server took too long to respond
                Log.e("TAG", "Network error: Request timed out")
                showNetworkError("Server took too long to respond")
            } catch (e: Exception) {
                // Handle other unexpected errors
                Log.e("TAG", "Network error: ${e.localizedMessage}")
                showNetworkError("An unexpected error occurred")
            }

            binding.progressbar.visibility = View.GONE

            binding.tvVerseNumber.visibility = View.VISIBLE
            binding.tvVerseText.visibility = View.VISIBLE
            binding.tvTransliterationIfEnglish.visibility = View.VISIBLE
            binding.tvWordIfEnglish.visibility = View.VISIBLE
            binding.view.visibility = View.VISIBLE
            binding.tvTranslation.visibility = View.VISIBLE
            binding.clTranslation.visibility = View.VISIBLE
            binding.tvCommentries.visibility = View.VISIBLE
            binding.clCommentries.visibility = View.VISIBLE
            binding.backgroundImage.visibility = View.VISIBLE
            binding.ivFavoriteVerse.visibility = View.VISIBLE
        }
    }

    private fun showNetworkError(message: String = "No Internet Connection") {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.tvShowingMessage.visibility = View.VISIBLE
            binding.tvShowingMessage.text = message
        }
    }


    private fun getAndSetChapAndVerse() {
        val bundle = arguments
         chapterNum = bundle?.getInt("chapterNum")!!
         verseNum = bundle.getInt("verseNum")

        binding.tvVerseNumber.text = "||$chapterNum.$verseNum||"

    }


    private fun changeStatusBarColor() {
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        if (window != null) {
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = true
            }
        }
    }


}