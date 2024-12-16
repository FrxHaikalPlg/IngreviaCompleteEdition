package com.frxhaikal_plg.ingrevia.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.remote.model.search.DataItem
import com.frxhaikal_plg.ingrevia.databinding.ItemListBinding

class SearchResultAdapter(
    private val onItemClick: (DataItem) -> Unit
) : ListAdapter<DataItem, SearchResultAdapter.ViewHolder>(SearchDiffCallback()) {

    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = getItem(position)
        
        with(holder.binding) {
            recipesName.text = recipe.title
            caloriesNumber.text = "${recipe.calories} cal"
            stepNumber.text = "${recipe.totalSteps} steps"
            protein.text = "${recipe.protein}g protein"
            stepIcon.setImageResource(R.drawable.ic_step)
            
            Glide.with(holder.itemView.context)
                .load(recipe.imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(ivItemPhoto)
        }

        holder.itemView.setOnClickListener {
            onItemClick(recipe)
        }
    }

    class SearchDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }
}