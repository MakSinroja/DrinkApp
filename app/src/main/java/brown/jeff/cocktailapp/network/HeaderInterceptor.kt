package brown.jeff.cocktailapp.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor constructor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}