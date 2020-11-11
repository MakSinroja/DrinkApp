package brown.jeff.cocktailapp.network

import brown.jeff.cocktailapp.BuildConfig
import brown.jeff.cocktailapp.ui.adapter.DrinkAdapter
import brown.jeff.cocktailapp.util.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { GsonBuilder().create() }

    single { HeaderInterceptor(get()) }

    single { GsonConverterFactory.create() }

    single { ScalarsConverterFactory.create() }

    single { RxJava2CallAdapterFactory.createAsync() }

    single {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            interceptor.level = HttpLoggingInterceptor.Level.NONE

        OkHttpClient.Builder().apply {
            readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(get<HeaderInterceptor>())
            addInterceptor(interceptor)
        }.build()
    }

    single {
        Retrofit.Builder().apply {
            addConverterFactory(get<GsonConverterFactory>())
            addConverterFactory(get<ScalarsConverterFactory>())
            addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            client(get<OkHttpClient>())
            baseUrl(Constants.BASE_URL)
        }.build()
    }

    factory { get<Retrofit>().create(DrinkApi::class.java) }

    factory { NetworkConnection(get()) }
    factory { DrinkAdapter(get(), get()) }
}