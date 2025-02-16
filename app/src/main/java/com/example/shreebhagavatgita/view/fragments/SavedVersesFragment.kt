package com.example.shreebhagavatgita.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shreebhagavatgita.R
import com.example.shreebhagavatgita.databinding.FragmentSavedVersesBinding
import com.example.shreebhagavatgita.datasource.api.room.SavedVerses
import com.example.shreebhagavatgita.view.adapter.AdapterSavedVerses
import com.example.shreebhagavatgita.viewmodel.MainViewModel
import kotlin.math.log


class SavedVersesFragment : Fragment() {

    private lateinit var binding: FragmentSavedVersesBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapterSavedVerses: AdapterSavedVerses

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentSavedVersesBinding.inflate(layoutInflater)



            getVerseFromRoom()


        return binding.root
    }

    private fun getVerseFromRoom() {
           viewModel.getAllEnglishVerse().observe(viewLifecycleOwner){

               adapterSavedVerses = AdapterSavedVerses(::onVersesItemVClicked)
               binding.rvVerses.adapter = adapterSavedVerses
               adapterSavedVerses.differ.submitList(it)
               binding.shimmer.visibility = View.GONE


           }
    }


    private fun onVersesItemVClicked(verse : SavedVerses){

        val bundle = Bundle()
        bundle.putBoolean("showRoomData" , true)
        bundle.putInt("chapterNum", verse.chapter_number)
        bundle.putInt("verseNum", verse.verse_number)
        findNavController().navigate(R.id.action_savedVersesFragment_to_verseDetailFragment,bundle)
    }


}