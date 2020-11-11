package brown.jeff.cocktailapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import brown.jeff.cocktailapp.R
import brown.jeff.cocktailapp.model.Drink
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar


//used to load image into imageview
fun ImageView.loadImage(drink: Drink) {

    Glide.with(this)
        .load(drink.drinkImg)
        .error(R.drawable.ic_broken_image_black_24dp)
        .apply(RequestOptions.centerCropTransform())
        .into(this)
}

//hides keyboard from view
fun Context.hideKeyBoard(view: View) {
    val inputManager =
        getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(view.windowToken, 0)

}

fun Fragment.hideKeyboard() {
    view?.let {
        activity?.hideKeyBoard(it)
    }
}


fun showSnackBar(
    view: View,
    displayedMessage: String,
    duration: Int,
    action: String,
    actionFunction: () -> Unit
) {
    val snackBar = Snackbar.make(view, displayedMessage, duration)
    snackBar.setAction(action) {
        actionFunction()
    }
    snackBar.setTextColor(Color.WHITE)
    snackBar.show()

}


//back press
fun Toolbar.backPressedToolbar(activity: Activity?) {
    this.setNavigationOnClickListener {
        activity?.onBackPressed()
    }
}

//change recyclerview on orientation change
fun changeRecyclerViewLayout(
    context: Context,
    activity: Activity,
    recyclerView: RecyclerView
) {
    if (activity.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
        recyclerView.layoutManager = GridLayoutManager(context, 2)
    } else {
        recyclerView.layoutManager = GridLayoutManager(context, 4)


    }
}


//intent to share drink
fun shareDrink(
    activity: Activity,
    drinkName: String
) {
    val drink = drinkName + " Cocktail"
    val formattedDrinkName = drink.replace(' ', '-')
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, drinkName)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share $formattedDrinkName")
    activity.startActivity(shareIntent)
}

//intent to open url
fun openToWebsite(activity: Activity, url: String, title: String) {

    val intent: Intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(url)
    }
    val shareIntent = Intent.createChooser(intent, title)
    activity.startActivity(shareIntent)
}

//intent to email feedback to dev
fun emailFeedback(activity: Activity) {
    val intent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_EMAIL, "jbicondev@gmail.com")
        putExtra(Intent.EXTRA_SUBJECT, "DrinkApp: Feedback")
    }

    val sendIntent = Intent.createChooser(intent, "Email feedback")
    activity.startActivity(sendIntent)
}








