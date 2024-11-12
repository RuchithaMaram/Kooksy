package com.teamfour.kooksy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import com.teamfour.kooksy.R
import com.teamfour.kooksy.databinding.FragmentRecipeBinding
import com.teamfour.kooksy.ui.favorite.FavoritesViewModel
import com.teamfour.kooksy.ui.profile.Recipe

class FragmentRecipeDetails : Fragment() {
    private lateinit var recipeItem: Recipe
    private var isFav: Boolean = false
    private var isIconToggled: Boolean = false
    val viewmodel: FavoritesViewModel by viewModels()
    private lateinit var binding: FragmentRecipeBinding
    val args: FragmentRecipeDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeItem = args.recipeItem

        (requireActivity() as AppCompatActivity).supportActionBar?.title = recipeItem.recipe_name
        setEmojiDrawable()
        isFav = recipeItem.is_favourite
        setRecyclerViewData()
        setupTabListener()
        setOptionsMenu(isFav)
        observeViewModel()
    }

    private fun setupTabListener() {
        binding.recipeTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position){
                    0 -> binding.recipeDetailsRecyclerView.adapter = RecipeDetailsAdapter(recipeItem.recipe_ingredients)
                    1 -> binding.recipeDetailsRecyclerView.adapter = ProcedureAdapter(recipeItem.recipe_instructions)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                println("Unselected")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                println("Reselected")
            }
        })
    }

    private fun setRecyclerViewData() {
        val adapter = RecipeDetailsAdapter(recipeItem.recipe_ingredients)

        val spaceDecoration = SpaceItemDecoration(16) // Adds 16dp space between items
        binding.recipeDetailsRecyclerView.addItemDecoration(spaceDecoration)

        binding.recipeDetailsRecyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewmodel.isFavouritevalueUpdated.observe(viewLifecycleOwner) { added ->
            if (added)
                Toast.makeText(
                    requireActivity(),
                    "Added to favourites successfully",
                    Toast.LENGTH_SHORT
                ).show()
            else
                Toast.makeText(
                    requireActivity(),
                    "Removed from favourites successfully",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun setEmojiDrawable() {
        val difficultyLevel = recipeItem.recipe_difficultyLevel
        val drawable = DifficultyLevel.valueOf(difficultyLevel).drawableRes
        val emojiDrawable = ResourcesCompat.getDrawable(resources, drawable, null)

        Picasso.get().load(recipeItem.recipe_imageURL).placeholder(R.drawable.ic_recipe_book)
            .into(binding.recipePageImage)
        binding.cookingTime.text = recipeItem.recipe_cookTime.toString().plus(" mins")
        binding.recipeDifficultyLevel.setCompoundDrawablesWithIntrinsicBounds(
            null,
            emojiDrawable,
            null,
            null
        )
        binding.recipeCalories.text = recipeItem.recipe_calories.toString().plus(" Cals")
        binding.recipeRatingBar.rating = recipeItem.recipe_rating.toFloat()
    }

    private fun setOptionsMenu(isFav: Boolean) {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fav_menu, menu)
                val item = menu.findItem(R.id.fav_icon)
                isIconToggled = isFav
                if (isFav) {
                    item.icon = ResourcesCompat.getDrawable(
                        resources, R.drawable.ic_fav_selected, null
                    )
                } else {
                    item.icon = ResourcesCompat.getDrawable(
                        resources, R.drawable.ic_fav_unselected, null
                    )
                }
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.fav_icon -> {
                        toggleMenuItemIcon(item)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun toggleMenuItemIcon(item: MenuItem) {
        if (isIconToggled) {
            item.icon = ResourcesCompat.getDrawable(
                resources, R.drawable.ic_fav_unselected, null
            )
        } else {
            item.icon = ResourcesCompat.getDrawable(
                resources, R.drawable.ic_fav_selected, null
            )
        }
        isIconToggled = !isIconToggled // Toggle the state
        viewmodel.updateFavoriteStatus(isIconToggled, args.recipeItem)
    }
}

enum class DifficultyLevel(val drawableRes: Int) {
    Easy(R.drawable.hearteyes_emoji), Medium(R.drawable.happy_emoji), Hard(R.drawable.grinningface_emoji)
}