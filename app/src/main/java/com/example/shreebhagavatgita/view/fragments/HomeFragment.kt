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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shreebhagavatgita.NetworkManager
import com.example.shreebhagavatgita.R
import com.example.shreebhagavatgita.databinding.FragmentHomeBinding
import com.example.shreebhagavatgita.datasource.api.room.SavedChapters
import com.example.shreebhagavatgita.models.ChaptersItem
import com.example.shreebhagavatgita.view.adapter.AdapterChapters
import com.example.shreebhagavatgita.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel : MainViewModel by viewModels()
    private lateinit var adapterChapters: AdapterChapters
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        changeStatusBarColor()
        showVerseOfTheDay()
        checkInternetConnectivity()
         binding.ivSettings.setOnClickListener {
             findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
         }
        return binding.root
    }

    private fun showVerseOfTheDay() {
        val chapterNumber = (1..18).random()
        val verseNumber = (1..20).random()

        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                lifecycleScope.launch {
                    try {
                        viewModel.getAParticularVerse(chapterNumber, verseNumber).collect {
                            for (i in it.translations) {
                                if (i.language == "english") {
                                    binding.tvVerseOfTheDay.text = i.description
                                    break
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("HomeFragment", "Error fetching verse of the day: ${e.localizedMessage}")
                    }
                }
            } else {
                binding.tvVerseOfTheDay.text = "No Internet Connection"
            }
        }
    }


    private fun onFavouriteClicked(chaptersItem: ChaptersItem){

        lifecycleScope.launch {

            viewModel.putSavedChaptersSP(chaptersItem.chapter_number.toString(),chaptersItem.id)
            viewModel.getVerses(chaptersItem.chapter_number).collect{
                val verseList = arrayListOf<String>()
                for (currentVerse in it){
                    for (verses in currentVerse.translations){
                        if (verses.language == "english"){
                            verseList.add(verses.description)
                            break
                        }
                    }
                }

                val savedChapters = SavedChapters(
                    chapter_number = chaptersItem.chapter_number,
                    chapter_summary = chaptersItem.chapter_summary,
                    chapter_summary_hindi = chaptersItem.chapter_summary_hindi,
                    id = chaptersItem.id,
                    name = chaptersItem.name,
                    name_meaning = chaptersItem.name_meaning,
                    name_translated = chaptersItem.name_translated,
                    name_transliterated = chaptersItem.name_transliterated,
                    slug = chaptersItem.slug,
                    verses_count = chaptersItem.verses_count,
                    verses = verseList

                )

                viewModel.insertChapters(savedChapters)
            }
        }
    }

    private fun onFavouriteFilled(chaptersItem: ChaptersItem){
        lifecycleScope.launch {
            viewModel.deleteSavedChapterFromSP(chaptersItem.chapter_number.toString())
            viewModel.deleteChapter(chaptersItem.id)
        }
    }

    private fun checkInternetConnectivity() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner){
            if (it == true){

                binding.shimmer.visibility = View.VISIBLE
                binding.rvChapters.visibility = View.VISIBLE
                binding.tvShowingMessage.visibility = View.GONE
                getAllChapter()
            }
            else{
                binding.shimmer.visibility = View.GONE
                binding.rvChapters.visibility = View.GONE
                binding.tvShowingMessage.visibility = View.VISIBLE
            }
        }
    }


    private fun getAllChapter() {

         lifecycleScope.launch {
             try {
                 viewModel.getAllChapter().collect{chapterList ->
                     adapterChapters =
                         AdapterChapters(::onChapterIvClicked, ::onFavouriteClicked, true, ::onFavouriteFilled,viewModel)
                     binding.rvChapters.adapter = adapterChapters
                     adapterChapters.differ.submitList(chapterList)
                     binding.shimmer.visibility = View.GONE
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

         }

    }

    private fun showNetworkError(message: String = "No Internet Connection") {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.shimmer.visibility = View.GONE
            binding.rvChapters.visibility = View.GONE
            binding.tvShowingMessage.visibility = View.VISIBLE
            binding.tvShowingMessage.text = message
        }
    }


    private fun onChapterIvClicked(chaptersItem: ChaptersItem){

         val bundle = Bundle()
        bundle.putInt("chapterNumber", chaptersItem.chapter_number)
        bundle.putString("chapterTitle", chaptersItem.name_translated)
        bundle.putString("chapterDesc", chaptersItem.chapter_summary)
        bundle.putInt("verseCount", chaptersItem.verses_count)

        findNavController().navigate(R.id.action_homeFragment_to_versesFragment,bundle)
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