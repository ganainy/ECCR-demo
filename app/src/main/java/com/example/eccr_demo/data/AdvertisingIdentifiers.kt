package com.example.eccr_demo.data
// the device identifiers that will be sent to the server in a post request
data class AdvertisingIdentifiers(
    val advertisingId: String,
    val androidId: String,
    val locale: String,
    val screenWidth: Int,
    val screenHeight: Int,
    val deviceModel: String,
    val osVersion: String,
    val networkInfo: NetworkInfo
)

data class NetworkInfo(
    val carrier: String,
    val wifiSsid: String
)