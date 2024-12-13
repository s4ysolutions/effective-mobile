package solutions.s4y.effectm.provider

import android.content.Context
import android.util.Log
import io.reactivex.rxjava3.core.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import solutions.s4y.effectm.provider.json.JsonOffers
import solutions.s4y.effectm.provider.json.JsonTickets
import solutions.s4y.effectm.provider.json.JsonTicketsOffers

interface RetrofitClient {
    @GET("/offers.json")
    fun getOffers(): Single<JsonOffers>

    @GET("/tickets.json")
    fun getTickets(): Single<JsonTickets>

    @GET("/offers_tickets.json")
    fun getTicketsOffers(): Single<JsonTicketsOffers>

    companion object {
        private const val ASSET_URL_PREFIX = "http://android_asset/"
        private val TAG = RetrofitClient::class.java.simpleName

        fun getInstance(context: Context): RetrofitClient {
            return (OkHttpClient.Builder()
                .addInterceptor(
                    object : Interceptor {
                        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                            val request = chain.request()
                            val url = request.url().toString()
                            if (!url.startsWith(ASSET_URL_PREFIX)) {
                                return chain.proceed(request)
                            }
                            val assetPath = url.substring(ASSET_URL_PREFIX.length)
                            try {
                                val assetData = context.assets.open(assetPath).use { assetStream ->
                                    val assetSize = assetStream.available()
                                    ByteArray(assetSize).apply {
                                        assetStream.read(this)
                                    }
                                }
                                Log.d(TAG, "Loaded asset: $assetPath\n${String(assetData)}")
                                return okhttp3.Response.Builder()
                                    .request(request)
                                    .protocol(okhttp3.Protocol.HTTP_1_1)
                                    .code(200)
                                    .message("OK")
                                    .body(okhttp3.ResponseBody.create(null, assetData))
                                    .build()
                            } catch (e: Exception) {
                                Log.e(TAG, "Failed to load asset: $assetPath", e)
                                return okhttp3.Response.Builder()
                                    .request(request)
                                    .protocol(okhttp3.Protocol.HTTP_1_1)
                                    .code(400)
                                    .message(e.message ?: "Unknown error")
                                    .build()
                            }
                        }
                    }
                )).let { httpClientBuilder ->
                    Retrofit.Builder()
                        .baseUrl(ASSET_URL_PREFIX) // setting the base url
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                        .client(httpClientBuilder.build())
                        .build()
                }.create(RetrofitClient::class.java)
        }
    }
}