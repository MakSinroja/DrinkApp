package brown.jeff.cocktailapp.ui.drinkclicked

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import brown.jeff.cocktailapp.R
import brown.jeff.cocktailapp.model.Drink
import brown.jeff.cocktailapp.ui.adapter.DrinkAdapter
import brown.jeff.cocktailapp.util.backPressedToolbar
import brown.jeff.cocktailapp.util.loadImage
import brown.jeff.cocktailapp.util.shareDrink
import brown.jeff.cocktailapp.util.showSnackBar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.drink_clicked_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class DrinkClickedFragment : Fragment(R.layout.drink_clicked_fragment) {


    private val drinkClickedViewModel: DrinkClickedViewModel by sharedViewModel()
    private lateinit var drinkClickedAdapter: DrinkAdapter
    private val safeArgs: DrinkClickedFragmentArgs by navArgs()
    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("drinkName", safeArgs.passDrink.drink)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.drink_clicked_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.drinkclicked_favorite_menuitem -> {
                addTofavoriteSnackBar(this.requireView(), safeArgs.passDrink)
                true
            }
            R.id.drinkclicked_settings_menuitem -> {
                changeToSettingsScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        toolbar = view?.findViewById(R.id.drink_toolbar)!!
        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolBarLayout)
        collapsingToolbarLayout.setCollapsedTitleTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.actionBarTextColor
            )
        )
        collapsingToolbarLayout.setExpandedTitleColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.actionBarTextColor
            )
        )
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.backPressedToolbar(activity)
        retrieveSafeArgs()

        drinkClickedViewModel.getDrinkById(safeArgs.passDrink.idDrink)

        return view
    }

    override fun onResume() {
        super.onResume()
        collapsingToolbarLayout.title = safeArgs.passDrink.drink

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        appbarFadeIn()
        loadDrinkIntoView()

        drinkClickedAdapter = DrinkAdapter(
            emptyList()
        ) { drink: Drink ->
            handleScreenChange(drink)
        }


        drinkclicked_recyclerview.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = drinkClickedAdapter
            isNestedScrollingEnabled = true

        }

        shareDrinkUrl(requireActivity())




        drinkClickedViewModel.clickedDrink.observe(viewLifecycleOwner, Observer {
            drinkClickedViewModel.getDrinksByIngredient(it.strIngredient1)
            favoriteFloatingActionButton(requireView(), it)

        })
        drinkClickedViewModel.relatedDrinks.observe(viewLifecycleOwner, Observer {
            Timber.e("List updated")
            drinkClickedAdapter.updateDrinkList(it)
        })


    }


    //shares drink url on button press
    private fun shareDrinkUrl(activity: Activity) {
        sharedrink_button.setOnClickListener {
            drinkClickedViewModel.clickedDrink.observe(viewLifecycleOwner, Observer {
                shareDrink(activity, getString(R.string.drink_url, it.idDrink, it.drink))
            })
        }
    }

    //observes drink from viewmodel
    private fun loadDrinkIntoView() {
        drinkClickedViewModel.clickedDrink.observe(viewLifecycleOwner, Observer {
            drink_imageview.loadImage(it)
            setIngredients(it.strMeasure1, it.strIngredient1, ingredient1_tv, view_divider1)
            setIngredients(it.strMeasure2, it.strIngredient2, ingredient2_tv, view_divider2)
            setIngredients(it.strMeasure3, it.strIngredient3, ingredient3_tv, view_divider3)
            setIngredients(it.strMeasure4, it.strIngredient4, ingredient4_tv, view_divider4)
            setIngredients(it.strMeasure5, it.strIngredient5, ingredient5_tv, view_divider5)
            setIngredients(it.strMeasure6, it.strIngredient6, ingredient6_tv, view_divider6)
            setIngredients(it.strMeasure7, it.strIngredient7, ingredient7_tv, view_divider7)
            setIngredients(it.strMeasure8, it.strIngredient8, ingredient8_tv, view_divider8)
            setIngredients(it.strMeasure9, it.strIngredient9, ingredient9_tv, view_divider9)
            setIngredients(it.strMeasure10, it.strIngredient10, ingredient10_tv, view_divider10)
            setIngredients(it.strMeasure11, it.strIngredient11, ingredient11_tv, view_divider11)
            setIngredients(it.strMeasure12, it.strIngredient12, ingredient12_tv, view_divider12)
            setIngredients(it.strMeasure13, it.strIngredient13, ingredient13_tv, view_divider13)
            setIngredients(it.strMeasure14, it.strIngredient14, ingredient14_tv, view_divider14)
            setIngredients(it.strMeasure15, it.strIngredient15, ingredient15_tv, view_divider15)
            instructions_tv.text = it.strInstructions
        })
    }


    private fun favoriteFloatingActionButton(view: View, drink: Drink) {
        favoritedrink_fab.setOnClickListener {
            addTofavoriteSnackBar(view, drink)
        }

    }

    //retrieves data from previous fragment and sets viewmodel data
    private fun retrieveSafeArgs() {
        val drinkPassed = safeArgs.passDrink
        drinkClickedViewModel.setClickedDrink(drinkPassed)


    }

    private fun handleScreenChange(drink: Drink) {
        val action =
            DrinkClickedFragmentDirections.drinkclickedToDrinkclicked(drink)
        findNavController().navigate(action)

    }

    private fun changeToSettingsScreen() {
        val action = DrinkClickedFragmentDirections.drinkclickedToSettings()
        findNavController().navigate(action)
    }


    private fun addTofavoriteSnackBar(view: View, drink: Drink) {
        drinkClickedViewModel.addDrinkToFavorites(drink)
        showSnackBar(
            view,
            getString(R.string.drink_favorited),
            Snackbar.LENGTH_LONG,
            getString(R.string.undo)
        ) { drinkClickedViewModel.removeDrinkFromFavorites(drink.idDrink) }

    }


    private fun setIngredients(
        measure: String?,
        ingredientName: String?,
        textView: TextView,
        view: View
    ) {
        if (ingredientName != null && measure != null) {
            textView.visibility = View.VISIBLE
            view.visibility = View.VISIBLE
            textView.text =
                getString(R.string.set_ingredients_and_measurements, measure, ingredientName)
        } else {
            textView.visibility = View.GONE
            view.visibility = View.GONE
        }
    }


    //fades imageview,title,and floating actionbar into toolbar
    private fun appbarFadeIn() {
        var scrollRage = -1
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRage == -1) {
                scrollRage = appBarLayout?.totalScrollRange!!
            }
            drink_toolbar.menu.findItem(R.id.drinkclicked_favorite_menuitem).isVisible =
                scrollRage + verticalOffset == 0
        })
    }

}


