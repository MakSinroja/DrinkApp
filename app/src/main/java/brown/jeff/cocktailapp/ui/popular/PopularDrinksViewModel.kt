package brown.jeff.cocktailapp.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brown.jeff.cocktailapp.model.Drink
import brown.jeff.cocktailapp.repositories.DrinkRepository
import brown.jeff.cocktailapp.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class PopularDrinksViewModel(private val drinkRepository: DrinkRepository) : ViewModel() {

    private val _popularDrinks = MutableLiveData<List<Drink>>()
    val popularDrinks: LiveData<List<Drink>>
        get() = _popularDrinks

    private val _loadingDrinks = MutableLiveData<Boolean>()
    val loadingDrinks: LiveData<Boolean>
        get() = _loadingDrinks

    private val _displayError = MutableLiveData<String>()
    val displayError: LiveData<String>
        get() = _displayError


    fun getAllPopularDrinks() {
        _loadingDrinks.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (val result = drinkRepository.getPopularDrinks()) {
                    is Result.Success -> {
                        Timber.i(result.data.drinks.toString())
                        _popularDrinks.postValue(result.data.drinks)
                        _loadingDrinks.postValue(false)
                    }
                    is Result.Failure -> {
                        _displayError.postValue(result.errors.toString())
                        _loadingDrinks.postValue(false)
                    }
                }
            }
        }

    }

//    fun getPopularDrinks() {
//        viewModelScope.launch {
//
//                val result = drinkRepository.getPopularDrinks().execute()
//                try {
//                    if (result.isSuccessful && result.body() != null) {
//                        _popularDrinks.postValue(result.body()!!.drinks)
//                        Timber.e("Success")
//                    } else {
//                        Timber.e(result.message())
//
//                    }
//                } catch (e: Exception) {
//                    Timber.e(e)
//
//
//            }
//        }
//    }
}