package pl.kitek.beers.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ThrottleInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            for (i in 0..3) Thread.sleep(200)
        } catch (ignored: Exception) {
        }

        return chain.proceed(chain.request())
    }
}
