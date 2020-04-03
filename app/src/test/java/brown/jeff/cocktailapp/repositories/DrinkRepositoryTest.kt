package brown.jeff.cocktailapp.repositories

import brown.jeff.cocktailapp.model.Drink
import brown.jeff.cocktailapp.network.DrinkApi
import brown.jeff.cocktailapp.network.Errors
import brown.jeff.cocktailapp.network.NetworkConnection
import brown.jeff.cocktailapp.network.Result
import brown.jeff.cocktailapp.room.DrinkDao
import brown.jeff.cocktailapp.util.DRINK
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class DrinkRepositoryTest {

    private lateinit var mockDrinkDao: DrinkDao
    private lateinit var mockDrinkApi: DrinkApi
    private lateinit var mockNetworkConnection: NetworkConnection
    private lateinit var mockDrinkCall: Call<Drink>
    private lateinit var mockDrinkResponse: Response<Drink>
    private lateinit var mockDrinksCall: Call<Drink>
    private lateinit var mockDrinksResponse: Response<Drink>
    private lateinit var drinkRepository: DrinkRepository

    private val drink = DRINK
    private val drinkID = DRINK.idDrink
    @Before
    fun setup() {
        mockDrinkApi = mock()
        mockDrinkDao = mock()
        mockNetworkConnection = mock()
        //for single Drink
        mockDrinkCall = mock()
        mockDrinkResponse = mock()
        //for call to Drinks
        mockDrinksCall = mock()
        mockDrinksResponse = mock()
        drinkRepository = DrinkRepository(mockDrinkApi, mockDrinkDao, mockNetworkConnection)

    }

    @Test
    fun `should return drink when drink id is given`() = runBlocking {
        //given
        val drink = DRINK
        given { mockNetworkConnection.isInternetAvailable() }.willReturn(true)
        given { mockDrinkResponse.isSuccessful }.willReturn(true)
        given { mockDrinkResponse.body() }.willReturn(drink)
        given { mockDrinkCall.execute() }.willReturn(mockDrinkResponse)
        given { mockDrinkApi.getDrinkById(drinkID) }.willReturn(mockDrinkCall)
        //when
        val response = drinkRepository.getDrinkById(drinkID)
        //then
        verify(mockDrinkApi).getDrinkById(drinkID)
        Assert.assertEquals(response, Result.Success(drink))

    }

    @Test
    fun `should return network connection error when internet is not available`() = runBlocking {
        //given
        given { mockNetworkConnection.isInternetAvailable() }.willReturn(false)
        //when
        val response = drinkRepository.getDrinkById(drinkID)
        //then
        Assert.assertEquals(response, Result.Failure(Errors.NetworkError()))
        println(response)
        println(Result.Failure(Errors.NetworkError()))
    }

    @Test
    fun `should return exception error when input is incorrect`() = runBlocking {
        //given
        given { mockNetworkConnection.isInternetAvailable() }.willReturn(true)
        //when
        val response = drinkRepository.getDrinkById("test")
        //then
        val result = Result.Failure(Errors.ExceptionError(NullPointerException()))
        Assert.assertEquals(response, result)
        println(response)
        println(result)


    }

    @Test
    fun `should return response error with api call is unsuccessful`() = runBlocking {
        //given
        given { mockNetworkConnection.isInternetAvailable() }.willReturn(true)
        given { mockDrinkResponse.isSuccessful }.willReturn(false)
        given { mockDrinkCall.execute() }.willReturn(mockDrinkResponse)
        given { mockDrinkApi.getDrinkById(drinkID) }.willReturn(mockDrinkCall)
        //then
        val response = drinkRepository.getDrinkById(drinkID)
        //when
        val result = Result.Failure(Errors.ResponseError("null"))
        Assert.assertEquals(response, result)
        println(response)
        println(result)


    }
}