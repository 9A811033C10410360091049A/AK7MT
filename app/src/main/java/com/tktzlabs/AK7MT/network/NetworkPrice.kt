package com.tktzlabs.AK7MT.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkPrice(val symbol: String, val price: String)
data class NetworkPing(val nothing: String) // this method returns always {}

