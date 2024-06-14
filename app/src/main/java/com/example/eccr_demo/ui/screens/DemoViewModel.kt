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

import ApiInterface

import android.content.res.Resources
import android.os.Build
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import androidx.compose.runtime.MutableState
import com.example.eccr_demo.data.DeviceIdentifiers
import com.example.eccr_demo.data.FakeNews
import com.example.eccr_demo.data.Location
import com.example.eccr_demo.data.Locator
import com.example.eccr_demo.data.PostResponseData
import com.example.eccr_demo.data.ScreenType
import com.example.eccr_demo.data.fakeNewsList
import java.net.NetworkInterface
import java.util.*
import java.security.MessageDigest

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
        postDeviceIdentifiers()
    }


    private fun getIPAddress(useIPv4: Boolean = true): String {
        return "172.16.254.1"
   /*     try {
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
        return ""*/
    }

    private fun getMACAddressThroughSideChannel(): String {
        return "02:42:AC:11:00:02"
        /*try {
            val all = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue

                val macBytes = nif.hardwareAddress ?: return "02:42:AC:11:00:02"
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
        return "02:00:00:00:00:00"*/
    }

    private fun postDeviceIdentifiers() {

        val deviceIdentifiers = DeviceIdentifiers(
            imei = "354992107942763",
            imsi = "310260000034000",
            simNumber = "89014103211118510720",
            serialNumber = "4A1F8042BAF88334",
            androidId = "a7e5e1f66a0e8d26",
            phoneNumber = "+11234567890",
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
                        updateHomeAdLink(ad_link = returnedResponse.ad_link)
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PostResponseData>, t: Throwable) {
            }
        })
    }

    private fun updateHomeAdLink(ad_link: String?) {
        demoUiState.value = demoUiState.value.copy(
            homeAdLink = ad_link
        )
    }

    private fun updateDetailsAdLink(ad_link: String?) {
        demoUiState.value = demoUiState.value.copy(
            detailsAdLink = ad_link
        )
    }


    private fun getApiInterface() {
        apiInterface = RetrofitInstance.getInstance().create(ApiInterface::class.java)
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
        val call = apiInterface.postLocation(Location(lat.toString(), lng.toString()))

        call.enqueue(object : Callback<PostResponseData> {
            override fun onResponse(
                call: Call<PostResponseData>,
                response: Response<PostResponseData>
            ) {
                if (response.isSuccessful) {
                    val returnedResponse = response.body()
                    if (returnedResponse != null) {
                        updateDetailsAdLink(ad_link = returnedResponse.ad_link)
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PostResponseData>, t: Throwable) {
            }
        })
    }

    private fun getMd5Hash(input: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun postHashedMacAndIp() {

        val mac = getMACAddressThroughSideChannel()
        val ip = getIPAddress(true)
        val macMd5Hash = getMd5Hash(mac)
        val ipMd5Hash = getMd5Hash(ip)

        val call = apiInterface.postLocator(
            Locator(
                m_id = macMd5Hash, i_id = ipMd5Hash, deviceModel = Build.MODEL,
                osVersion = Build.VERSION.RELEASE,
                screenWidth = Resources.getSystem().displayMetrics.widthPixels.toString(),
                screenHeight = Resources.getSystem().displayMetrics.heightPixels.toString(),
                locale = Locale.getDefault().toString()
            )
        )

        call.enqueue(object : Callback<PostResponseData> {
            override fun onResponse(
                call: Call<PostResponseData>,
                response: Response<PostResponseData>
            ) {
                if (response.isSuccessful) {
                    val returnedResponse = response.body()
                    if (returnedResponse != null) {
                        updateDetailsAdLink(ad_link = returnedResponse.ad_link)
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<PostResponseData>, t: Throwable) {
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

    fun selectDetailsAdType(granted: Boolean) {
        if (granted) {
            // Permission granted, update the location
            postLocation(40.7128, -74.0060)
        } else {
            // Permission was not granted, post MAC and IP
            postHashedMacAndIp()
        }
        //update settings switch value to show location given status
        enableLocation(granted)


    }

}

data class DemoUiState(
    var isPrivacyPolicyDismissed: Boolean = false,
    var homeAdLink: String? = null,
    var detailsAdLink: String? = null,
    var screenType: ScreenType = ScreenType.HomeScreen,
    var currentNews: FakeNews = fakeNewsList[0],
    val enableLocation: Boolean = false,
)
