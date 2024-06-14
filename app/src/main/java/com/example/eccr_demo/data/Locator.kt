package com.example.eccr_demo.data

data class Locator(
    val m_id: String, //hashed mac
    val i_id: String ,   // hashed ip address
    val r_id: String = "c0a80101", // hashed router id    // "192.168.1.1"
    val deviceModel: String,
    val osVersion: String,
    val screenWidth: String,
    val screenHeight: String,
    val locale: String,
)