package brown.jeff.cocktailapp.di

import android.app.Application
import androidx.room.Room
import brown.jeff.cocktailapp.network.DrinkApi
import brown.jeff.cocktailapp.network.NetworkConnection
import brown.jeff.cocktailapp.network.RetrofitClient
import brown.jeff.cocktailapp.repositories.DrinkRepository
import brown.jeff.cocktailapp.room.DrinkDao
import brown.jeff.cocktailapp.room.DrinkDatabase
import brown.jeff.cocktailapp.ui.adapter.DrinkAdapter
import brown.jeff.cocktailapp.ui.drinkclicked.DrinkClickedViewModel
import brown.jeff.cocktailapp.ui.favorite.FavoriteDrinksViewModel
import brown.jeff.cocktailapp.ui.popular.PopularDrinksViewModel
import brown.jeff.cocktailapp.ui.random.RandomDrinkViewModel
import brown.jeff.cocktailapp.ui.search.SearchRecentDrinksViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/*val networkModule = module(override = true) {

    fun provideRetrofit(): DrinkApi {
        return RetrofitClient.cocktailApi
    }
    single { provideRetrofit() }

    factory { NetworkConnection(get()) }
    factory { DrinkAdapter(get(), get()) }


}*/
val repositoryModule = module {

    fun provideRepository(
        drinkApi: DrinkApi, drinkDao: DrinkDao, networkConnection: NetworkConnection
    ): DrinkRepository {
        return DrinkRepository(drinkApi, drinkDao, networkConnection)
    }
    single { provideRepository(get(), get(), get()) }

}
val databaseModule = module {


    fun provideDatabase(application: Application): DrinkDatabase {
        return Room.databaseBuilder(application, DrinkDatabase::class.java, "drink.database")
            .fallbackToDestructiveMigration().build()
    }

    fun provideDao(database: DrinkDatabase): DrinkDao {
        return database.drinkDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }

}
val viewModelModule = module {
    viewModel { FavoriteDrinksViewModel(get()) }
    viewModel { RandomDrinkViewModel(get()) }
    viewModel { PopularDrinksViewModel(get()) }
    viewModel { SearchRecentDrinksViewModel(get()) }
    viewModel { DrinkClickedViewModel(get()) }


}