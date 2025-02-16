package com.example.shreebhagavatgita.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shreebhagavatgita.databinding.ItemViewVersesBinding


class AdapterVerses(val onVersesItemVClicked: (String, Int) -> Unit, val  onClick: Boolean) : RecyclerView.Adapter<AdapterVerses.VerseViewHolder>() {

    class VerseViewHolder( val binding: ItemViewVersesBinding):ViewHolder(binding.root)


     val diffUtil = object :DiffUtil.ItemCallback<String>(){
         override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
             return oldItem == newItem
         }

         override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
              return oldItem == newItem
         }

     }
    val differ = AsyncListDiffer(this,diffUtil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseViewHolder {

        return VerseViewHolder(ItemViewVersesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VerseViewHolder, position: Int) {
              val verse = differ.currentList[position]

          holder.binding.apply {
                tvVerseNumber.text = "Verse ${position+1}"
                tvVerse.text =  verse
          }


        if (onClick){
            holder.binding.ivNext.visibility = View.VISIBLE
        }
        else{
            holder.binding.ivNext.visibility = View.GONE
        }



        holder.binding.ll.setOnClickListener {
            if (onClick){
                holder.binding.ivNext.visibility = View.VISIBLE
                onVersesItemVClicked(verse, position+1)
            }
            else{
                holder.binding.ivNext.visibility = View.GONE
            }
        }
    }
}