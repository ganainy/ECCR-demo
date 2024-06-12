package com.example.eccr_demo.data
// the device identifiers that will be sent to the server in a post request
data class DeviceIdentifiers(
    val imei: String,
    val imsi: String,
    val simNumber: String,
    val serialNumber: String,
    val androidId: String,
    val phoneNumber: String,
    val deviceModel: String,
    val osVersion: String,
    val screenWidth: String,
    val screenHeight: String,
    val locale: String,
)