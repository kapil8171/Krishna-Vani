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
import com.example.shreebhagavatgita.databinding.FragmentVersesBinding
import com.example.shreebhagavatgita.view.adapter.AdapterVerses
import com.example.shreebhagavatgita.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VersesFragment : Fragment() {
      private lateinit var binding: FragmentVersesBinding
      private val viewModel : MainViewModel by viewModels()
    private lateinit var adapterVerses: AdapterVerses
    private var chapterNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVersesBinding.inflate(layoutInflater)

        changeStatusBarColor()
        onReadMoreClicked()
        getAndSetChapterDetail()

        getData()


        return binding.root
    }

    private fun getData() {
        val bundle = arguments
        val showDataFromRoom = bundle?.getBoolean("showRoomData",false)

        if(showDataFromRoom == true){
              getDataFromRoom()
        }
        else{
            checkInternet()
        }

    }

    private fun getDataFromRoom() {

        viewModel.getAParticularChapter(chapterNumber).observe(viewLifecycleOwner){

            binding.tvChapterNumber.text = "Chapter ${it.chapter_number}"
            binding.tvChapterTitle.text =  it.name_translated
            binding.tvChapterDescription.text = it.chapter_summary
            binding.tvNumberofVerses.text = it.verses_count.toString()

            showListInAdapter(it.verses,false)

        }
    }

    private fun checkInternet() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner){
            if (it == true){
                binding.shimmer.visibility = View.VISIBLE
                binding.rvVerses.visibility = View.VISIBLE
                binding.tvShowingMessage.visibility = View.GONE
                getAllVerses()
            }
            else{
                Log.e("TAG", "No internet connection detected")
                showNetworkError()
            }
        }
    }

    private fun onVersesItemVClicked(verse:String, verseNumber: Int){

          val bundle = Bundle()
           bundle.putInt("chapterNum",chapterNumber)
           bundle.putInt("verseNum",verseNumber)
          findNavController().navigate(R.id.action_versesFragment_to_verseDetailFragment,bundle)
    }

    private fun onReadMoreClicked() {
        var isExpended = false
        binding.tvSeeMore.setOnClickListener{
            if (!isExpended){
                binding.tvChapterDescription.maxLines =50
                isExpended=true
                binding.tvSeeMore.text = "ReadLess"
            }
            else{
                binding.tvChapterDescription.maxLines=4
                isExpended=false
                binding.tvSeeMore.text = "ReadMore"

            }
        }
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

    private fun getAndSetChapterDetail() {
        val bundle = arguments
        chapterNumber = bundle?.getInt("chapterNumber")?:1
        binding.tvChapterNumber.text = "Chapter ${bundle?.getInt("chapterNumber")}"
        binding.tvChapterTitle.text = bundle?.getString("chapterTitle")
        binding.tvChapterDescription.text = bundle?.getString("chapterDesc")
        binding.tvNumberofVerses.text = bundle?.getInt("verseCount").toString()

    }

    private  fun getAllVerses() {
        lifecycleScope.launch {

            try {
                viewModel.getVerses(chapterNumber).collect{

                    val verseList = arrayListOf<String>()

                    for (currentVerse in it){
                        for (verses in currentVerse.translations){
                            if (verses.language == "english"){
                                verseList.add(verses.description)
                                break
                            }
                        }
                    }

                    withContext(Dispatchers.Main) {
                        showListInAdapter(verseList, true)
                    }
                }
            }
            catch (_: java.net.UnknownHostException){
                // No internet connection
                Log.e("TAG", "Network error: No Internet Connection")
                showNetworkError()
            }
            catch (e: java.net.SocketTimeoutException) {
                // Server took too long to respond
                Log.e("TAG", "Network error: Request timed out")
                showNetworkError("Server took too long to respond")
            }
            catch (e: Exception) {
                // Handle other unexpected errors
                Log.e("TAG", "Network error: ${e.localizedMessage}")
                showNetworkError("An unexpected error occurred")
            }

        }
    }

    private fun showNetworkError(message: String = "No Internet Connection") {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.shimmer.visibility = View.GONE
            binding.rvVerses.visibility = View.GONE
            binding.tvShowingMessage.visibility = View.VISIBLE
            binding.tvShowingMessage.text = message
        }
    }


    private fun showListInAdapter(verseList: List<String>, onClick: Boolean) {
        adapterVerses = AdapterVerses(::onVersesItemVClicked, onClick)
        binding.rvVerses.adapter = adapterVerses


        adapterVerses.differ.submitList(verseList)
        binding.shimmer.visibility = View.GONE
    }


}