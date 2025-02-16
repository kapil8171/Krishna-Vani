package com.example.shreebhagavatgita.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shreebhagavatgita.databinding.ItemViewVersesBinding
import com.example.shreebhagavatgita.datasource.api.room.SavedVerses


class AdapterSavedVerses(val onVersesItemVClicked: (SavedVerses) -> Unit) : RecyclerView.Adapter<AdapterSavedVerses.SavedVerseViewHolder>() {

    class SavedVerseViewHolder( val binding: ItemViewVersesBinding):ViewHolder(binding.root)


    val diffUtil = object :DiffUtil.ItemCallback<SavedVerses>(){
        override fun areItemsTheSame(oldItem: SavedVerses, newItem: SavedVerses): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SavedVerses, newItem: SavedVerses): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedVerseViewHolder {

        return SavedVerseViewHolder(ItemViewVersesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SavedVerseViewHolder, position: Int) {
        val verse = differ.currentList[position]


           holder.binding.tvVerseNumber.text = "Verse ${verse.chapter_number}.${verse.verse_number}"
           holder.binding.tvVerse.text = verse.translations[0].description

        holder.binding.ll.setOnClickListener {
                    onVersesItemVClicked(verse)
            }
        }
    }
