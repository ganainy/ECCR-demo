package com.example.eccr_demo.utils

import android.content.Context
import android.content.SharedPreferences

//   managing permission state with SharedPreferences
class PermissionManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("permissions", Context.MODE_PRIVATE)
    }

    fun wasPermissionRefused(permission: String): Boolean {
        return sharedPreferences.getBoolean(permission, false)
    }

    fun setPermissionRefused(permission: String, refused: Boolean) {
        sharedPreferences.edit().putBoolean(permission, refused).apply()
    }
}