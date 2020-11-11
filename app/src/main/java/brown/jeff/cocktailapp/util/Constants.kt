package brown.jeff.cocktailapp.util


class Constants {
//constant values

    companion object {
        const val IS_ALERT_KEY: String = "isAlertDisplayed"
        const val IS_DRINK: String = "isDrink"
        const val SEARCH_STRING = "searchString"
        const val BASE_URL: String =
            "https://www.thecocktaildb.com/api/json/v2/${brown.jeff.cocktailapp.BuildConfig.apiKey}/"

        const val TIMEOUT: Long = 60

    }


}