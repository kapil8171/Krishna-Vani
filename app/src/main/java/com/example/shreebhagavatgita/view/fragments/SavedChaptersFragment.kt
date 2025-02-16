package com.example.shreebhagavatgita.view.fragments

import android.os.Bundle
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
import com.example.shreebhagavatgita.R
import com.example.shreebhagavatgita.databinding.FragmentSavedChaptersBinding
import com.example.shreebhagavatgita.models.ChaptersItem
import com.example.shreebhagavatgita.view.adapter.AdapterChapters
import com.example.shreebhagavatgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class SavedChaptersFragment : Fragment() {

      private lateinit var binding: FragmentSavedChaptersBinding
      private lateinit var adapterChapters: AdapterChapters
      private val viewModel : MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedChaptersBinding.inflate(layoutInflater)

        changeStatusBarColor()
         getSavedChapters()

        return(binding.root)
    }


    private fun getSavedChapters() {

        viewModel.getSavedChapters().observe(viewLifecycleOwner){
            val chapterList = arrayListOf<ChaptersItem>()

            for(i in it){
                val chaptersItem = ChaptersItem(
                    i.chapter_number,
                    i.chapter_summary,
                    i.chapter_summary_hindi,
                    i.id,
                    i.name,
                    i.name_meaning,
                    i.name_translated,
                    i.name_transliterated,
                    i.slug,
                    i.verses_count
                )
                chapterList.add(chaptersItem)
            }


            if (chapterList.isEmpty()){
                binding.shimmer.visibility = View.GONE
                binding.rvChapters.visibility = View.GONE
                binding.tvShowingMessage.visibility = View.VISIBLE

            }
            adapterChapters = AdapterChapters(
                ::onChapterItemVClicked,
                ::onFavClicked,
                false,
                ::onFavouriteFilled,
                viewModel
            )
            binding.rvChapters.adapter = adapterChapters
            adapterChapters.differ.submitList(chapterList)


            binding.shimmer.visibility = View.GONE
            binding.rvChapters.visibility = View.VISIBLE
        }

    }

    private  fun onFavouriteFilled(chaptersItem: ChaptersItem){
        lifecycleScope.launch {
            viewModel.deleteChapter(chaptersItem.id)
        }

    }



    fun onChapterItemVClicked(chaptersItem: ChaptersItem){


        val bundle = Bundle()
        bundle.putInt("chapterNumber", chaptersItem.chapter_number)
        bundle.putBoolean("showRoomData",true)
        findNavController().navigate(R.id.action_savedChaptersFragment_to_versesFragment,bundle)

    }

    fun onFavClicked(chaptersItem: ChaptersItem){

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