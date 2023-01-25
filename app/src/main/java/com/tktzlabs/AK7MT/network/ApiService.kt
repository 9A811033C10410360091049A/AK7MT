package com.tktzlabs.AK7MT

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.tktzlabs.AK7MT.PreferenceHelper.apiUrl
import com.tktzlabs.AK7MT.network.NetworkPing
import com.tktzlabs.AK7MT.network.NetworkPrice
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.binance.com/api/v3/"
private val customPreference = "User_data"

object RetrofitHelper {
    fun getInstance(): Retrofit {
        val context = MainActivity.appContext // getting context from MainActivity
        val settings = PreferenceHelper.customPreference(context, customPreference)
        val apiUrl: String = settings.apiUrl.toString()
        Log.d("REST API TEST: ", "We got follwing API from setting: $apiUrl")
        return Retrofit.Builder().baseUrl(apiUrl).addConverterFactory(GsonConverterFactory.create()).build()
    }

    suspend fun getPing(): String {
        val communicationApiService = getInstance().create(CommunicationApiService::class.java)
        return try {
            communicationApiService.ping().body().toString()
        } catch (e: Exception) {
            ""
        }
    }


}

interface CommunicationApiService {
    @GET("ticker/price?symbol=BTCUSDT")
    suspend fun getBtcPrice(): Response<NetworkPrice>

    @GET("ping")
    suspend fun ping(): Response<NetworkPing>
}

