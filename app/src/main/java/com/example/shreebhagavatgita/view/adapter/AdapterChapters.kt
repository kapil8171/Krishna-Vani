package com.example.shreebhagavatgita.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shreebhagavatgita.databinding.ItemViewChaptersBinding
import com.example.shreebhagavatgita.models.ChaptersItem
import com.example.shreebhagavatgita.viewmodel.MainViewModel
import kotlin.reflect.KFunction1


class AdapterChapters(
    val onChapterIvClicked: (ChaptersItem) -> Unit,
    val onFavouriteClicked: (ChaptersItem) -> Unit,
    val showSaveButton: Boolean,
    val onFavouriteFilled: KFunction1<ChaptersItem, Unit>,
    val viewModel: MainViewModel
) :RecyclerView.Adapter<AdapterChapters.ChaptersViewHolder>() {

    class ChaptersViewHolder(val binding: ItemViewChaptersBinding):ViewHolder(binding.root)


      val diffUtil = object : DiffUtil.ItemCallback<ChaptersItem>(){
          override fun areItemsTheSame(oldItem: ChaptersItem, newItem: ChaptersItem): Boolean {
                 return oldItem.id == newItem.id
          }

          override fun areContentsTheSame(oldItem: ChaptersItem, newItem: ChaptersItem): Boolean {
               return oldItem == newItem
          }
      }

      val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChaptersViewHolder {

         return ChaptersViewHolder(ItemViewChaptersBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
         return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ChaptersViewHolder, position: Int) {
           val chapter = differ.currentList[position]



            val keys = viewModel.getAllSavedChaptersKeySP()

        if (!showSaveButton){
            holder.binding.apply {
                ivFavorite.visibility = View.GONE
                ivFavoriteFilled.visibility = View.GONE
            }
        }
        else{
            if(keys.contains(chapter.chapter_number.toString())){
                holder.binding.ivFavorite.visibility = View.GONE
                holder.binding.ivFavoriteFilled.visibility=View.VISIBLE
            }
            else{
                holder.binding.apply {
                    ivFavorite.visibility = View.VISIBLE
                    ivFavoriteFilled.visibility = View.GONE
                }
            }
        }

            holder.binding.apply {
              tvChapterNumber.text = "Chapter ${chapter.chapter_number}"
              tvChapterTitle.text = chapter.name_translated
              tvChapterDescription.text  = chapter.chapter_summary
              tvVersesCount.text = chapter.verses_count.toString()

            }



        holder.binding.ll.setOnClickListener {
                 onChapterIvClicked(chapter)
        }

        holder.binding.apply {
            ivFavorite.setOnClickListener {
                ivFavoriteFilled.visibility =View.VISIBLE
                ivFavorite.visibility =View.GONE

                onFavouriteClicked(chapter)
            }

            ivFavoriteFilled.setOnClickListener {
                ivFavorite.visibility =View.VISIBLE
                ivFavoriteFilled.visibility =View.GONE

                onFavouriteFilled(chapter)
            }


        }
    }
}