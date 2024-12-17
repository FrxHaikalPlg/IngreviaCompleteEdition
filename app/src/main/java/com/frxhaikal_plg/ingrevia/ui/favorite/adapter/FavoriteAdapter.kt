package com.frxhaikal_plg.ingrevia.ui.favorite.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.remote.model.favorite.DataItem
import com.frxhaikal_plg.ingrevia.data.remote.model.favorite.Recipe
import com.frxhaikal_plg.ingrevia.data.remote.model.home.RecipesItem
import com.frxhaikal_plg.ingrevia.databinding.ItemListBinding
import com.frxhaikal_plg.ingrevia.ui.detailrecipes.RecipesDetailActivity
import com.frxhaikal_plg.ingrevia.ui.home.HomeFragment

class FavoriteAdapter(
    private var recipes: List<DataItem> = emptyList(),
    private val onItemClick: (Recipe) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = recipes[position]
        val recipe = dataItem.recipe
        
        with(holder.binding) {
            recipesName.text = recipe?.title
            caloriesNumber.text = "${recipe?.calories} cal"
            stepNumber.text = "${recipe?.totalSteps} steps"
            protein.text = "Protein ${recipe?.protein}g"
            stepIcon.setImageResource(R.drawable.ic_step)
            
            recipe?.imageUrl?.let { url ->
                Glide.with(holder.itemView.context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(ivItemPhoto)
            }
        }

        holder.itemView.setOnClickListener {
            recipe?.let { 
                val intent = Intent(holder.itemView.context, RecipesDetailActivity::class.java)
                // Konversi Recipe dari favorite ke RecipesItem
                val recipesItem = RecipesItem(
                    id = dataItem.recipeId ?: "",
                    title = recipe.title ?: "",
                    calories = recipe.calories ?: 0,
                    totalSteps = recipe.totalSteps ?: 0,
                    imageUrl = recipe.imageUrl ?: "",
                    protein = recipe.protein ?: 0,
                    fat = recipe.fat ?: 0,
                    sodium = recipe.sodium ?: 0,
                    directions = recipe.directions?.filterNotNull() ?: emptyList(),
                    ingredients = recipe.ingredients?.filterNotNull() ?: emptyList(),
                    desc = recipe.desc ?: ""
                )
                intent.putExtra(HomeFragment.DISCOVER_RECIPE_EXTRA, recipesItem)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = recipes.size

    fun updateData(newRecipes: List<DataItem>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}