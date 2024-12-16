package com.frxhaikal_plg.ingrevia.ui.recipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendedRecipesItem
import com.frxhaikal_plg.ingrevia.databinding.ItemSquareBinding

class SeeMoreRecipesAdapter(
    private val recipes: List<RecommendedRecipesItem>
) : RecyclerView.Adapter<SeeMoreRecipesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSquareBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSquareBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        
        with(holder.binding) {
            recipesName.text = recipe.title
            caloriesNumber.text = "${recipe.calories} cal"
            stepNumber.text = "${recipe.totalSteps} steps"
            stepIcon.setImageResource(R.drawable.ic_step)
            
            recipe.imageUrl?.let { url ->
                Glide.with(holder.itemView.context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .timeout(10000)
                    .into(ivItemPhoto)
            } ?: run {
                ivItemPhoto.setImageResource(R.drawable.ic_place_holder)
            }
        }
    }

    override fun getItemCount() = recipes.size
}