/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.marsphotos.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import ApiInterface

import android.content.res.Resources
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import androidx.compose.runtime.MutableState
import com.example.eccr_demo.data.DeviceIdentifiers
import com.example.eccr_demo.data.FakeNews
import com.example.eccr_demo.data.Location
import com.example.eccr_demo.data.Locator
import com.example.eccr_demo.data.PostResponseData
import com.example.eccr_demo.data.ReceivedData
import com.example.eccr_demo.data.ScreenType
import com.example.eccr_demo.data.fakeNewsList
import java.net.NetworkInterface
import java.util.*

/**
 * UI state for the Home screen
 */

class DemoViewModel : ViewModel() {
    private val TAG = "ECCRViewModel"
    private lateinit var apiInterface: ApiInterface

    /** The mutable State that stores the status of the most recent request */


    var demoUiState: MutableState<DemoUiState> = mutableStateOf(DemoUiState())
        private set

    init {
        getApiInterface()

        Log.d(TAG, "getIPAddress(): ${getIPAddress(true)}")
        Log.d(TAG, "getMACAddress(): ${getMACAddress()}")

        /*getData()
        postData()*/
    }


    fun getIPAddress(useIPv4: Boolean = true): String {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = intf.inetAddresses
                for (addr in Collections.list(addrs)) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%')
                                return if (delim < 0) sAddr.uppercase(Locale.getDefault()) else sAddr.substring(
                                    0,
                                    delim
                                ).uppercase(Locale.getDefault())
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
    }


    private fun getMACAddress(): String {
        try {
            val all = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue

                val macBytes = nif.hardwareAddress ?: return ""
                val res1 = StringBuilder()
                for (b in macBytes) {
                    res1.append(String.format("%02X:", b))
                }

                if (res1.isNotEmpty()) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return "02:00:00:00:00:00"
    }

    fun getLocation(context: Context) {
        val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val networkLocation =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            Log.d(TAG, "GPS Location: $gpsLocation")
            Log.d(TAG, "Network Location: $networkLocation")
        }
    }


    fun postDeviceIdentifiers(context: Context) {

        val deviceIdentifiers = DeviceIdentifiers(
            imei = "123456789012345",
            imsi = "987654321098765",
            simNumber = "1234567890123456789",
            serialNumber = "ABC123DEF456",
            androidId = "abcdef1234567890",
            phoneNumber = "1234567890",
            deviceModel = Build.MODEL,
            osVersion = Build.VERSION.RELEASE,
            screenWidth = Resources.getSystem().displayMetrics.widthPixels.toString(),
            screenHeight = Resources.getSystem().displayMetrics.heightPixels.toString(),
            locale = Locale.getDefault().toString(),
        )


        val call = apiInterface.postDeviceIdentifiers(deviceIdentifiers)

        call.enqueue(object : Callback<PostResponseData> {
            override fun onResponse(
                call: Call<PostResponseData>,
                response: Response<PostResponseData>
            ) {
                if (response.isSuccessful) {
                    val returnedResponse = response.body()
                    if (returnedResponse != null) {
                        Log.d(TAG, "postData(): Data: ${returnedResponse.ad_link}")
                        updateHomeAdLink(ad_link=returnedResponse.ad_link)
                        Log.d(TAG, "postData(): Error: ${returnedResponse.error}")

                        // Handle the response here
                    }
                } else {
                    Log.e(TAG, "postData(): Request failed with status: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PostResponseData>, t: Throwable) {
                Log.e(TAG, "postData(): Request failed: ${t.message}")
            }
        })
    }

    private fun updateHomeAdLink(ad_link: String?) {
        demoUiState.value = demoUiState.value.copy(
            homeAdLink = ad_link
        )
    }

    private fun updateDetailsAdLink(ad_link: String?) {
        Log.d(TAG, "updateDetailsAdLink(): ad_link: $ad_link")
        demoUiState.value = demoUiState.value.copy(
            detailsAdLink = ad_link
        )
    }


    private fun getApiInterface() {
        apiInterface = RetrofitInstance.getInstance().create(ApiInterface::class.java)
    }

    private fun getData() {
        val call = apiInterface.receiveData()
        call.enqueue(object : Callback<ReceivedData> {
            override fun onResponse(call: Call<ReceivedData>, response: Response<ReceivedData>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG, " getData(): onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<ReceivedData>, t: Throwable) {
                t.printStackTrace()
                Log.d(TAG, " getData(): onFailure: ${t.message}")
            }
        })
    }

    fun updatePrivacyPolicyVisibilityState() {
        demoUiState.value = demoUiState.value.copy(
            isPrivacyPolicyDismissed = true
        )
    }


    fun navigateToDetails(news: FakeNews) {
        demoUiState.value = demoUiState.value.copy(
            currentNews = news,
            homeAdLink = null,
            screenType = ScreenType.Details
        )
    }

    fun backToHome() {
        demoUiState.value = demoUiState.value.copy(
            screenType = ScreenType.HomeScreen
        )
}

    fun postLocation(lat: Double, lng: Double) {

        val call = apiInterface.postLocation(Location(lat.toString(),lng.toString()))

        call.enqueue(object : Callback<PostResponseData> {
            override fun onResponse(
                call: Call<PostResponseData>,
                response: Response<PostResponseData>
            ) {
                if (response.isSuccessful) {
                    val returnedResponse = response.body()
                    if (returnedResponse != null) {
                        Log.d(TAG, "postLocation(): Data: ${returnedResponse.ad_link}")
                        updateDetailsAdLink(ad_link=returnedResponse.ad_link)
                        Log.d(TAG, "postLocation(): Error: ${returnedResponse.error}")

                        // Handle the response here
                    }
                } else {
                    Log.e(TAG, "postLocation(): Request failed with status: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PostResponseData>, t: Throwable) {
                Log.e(TAG, "postData(): Request failed: ${t.message}")
            }
        })
    }

    fun postMacAndIp() {

        val mac = getMACAddress()
        val ip = getIPAddress(true)

        val call = apiInterface.postLocator(Locator(mac,ip))

        call.enqueue(object : Callback<PostResponseData> {
            override fun onResponse(
                call: Call<PostResponseData>,
                response: Response<PostResponseData>
            ) {
                if (response.isSuccessful) {
                    val returnedResponse = response.body()
                    if (returnedResponse != null) {
                        Log.d(TAG, "postMacAndIp(): Data: ${returnedResponse.ad_link}")
                        updateDetailsAdLink(ad_link=returnedResponse.ad_link)
                        Log.d(TAG, "postMacAndIp(): Error: ${returnedResponse.error}")

                        // Handle the response here
                    }
                } else {
                    Log.e(TAG, "postMacAndIp(): Request failed with status: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PostResponseData>, t: Throwable) {
                Log.e(TAG, "postMacAndIp(): Request failed: ${t.message}")
            }
        })
    }

    fun showSettingsScreen() {
        demoUiState.value = demoUiState.value.copy(
            screenType = ScreenType.Settings
        )
    }

    fun enableLocation(isLocationEnabled: Boolean) {
        demoUiState.value = demoUiState.value.copy(
            enableLocation = isLocationEnabled
        )

    }

}
data class DemoUiState(
    var isPrivacyPolicyDismissed: Boolean = false,
    var homeAdLink: String? = null,
    var detailsAdLink: String? = null,
    var screenType: ScreenType = ScreenType.HomeScreen,
    var currentNews: FakeNews = fakeNewsList[0],
    val enableLocation: Boolean=false,)
