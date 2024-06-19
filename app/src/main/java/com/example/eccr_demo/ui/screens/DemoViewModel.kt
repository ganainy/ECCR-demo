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

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import androidx.compose.runtime.MutableState
import com.example.eccr_demo.data.AdvertisingIdentifiers
import com.example.eccr_demo.data.FakeNews
import com.example.eccr_demo.data.Location
import com.example.eccr_demo.data.MinimalIdentifiers
import com.example.eccr_demo.data.NetworkInfo
import com.example.eccr_demo.data.PostResponseData
import com.example.eccr_demo.data.ScreenType
import com.example.eccr_demo.data.fakeNewsList

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
    }


  /*  fun getLocation(context: Context) {
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
    }*/


    fun postAdvertisingIdentifiers() {

        val networkInfo = NetworkInfo(
            carrier = "Verizon",
            wifiSsid = "HomeWiFi"
        )
        val advertisingIdentifiers = AdvertisingIdentifiers(
            advertisingId = "e5b8a8f6-65cd-43b4-a6f1-97f88c9f2f6c",
            androidId = "550e8400-e29b-41d4-a716-446655440000",
            locale = "en_US",
            screenWidth = 1080,
            screenHeight = 1920,
            deviceModel = "Pixel",
            osVersion = "Android 10",
            networkInfo = networkInfo
        )


        val call = apiInterface.postAdvertisingIdentifiers(advertisingIdentifiers)

        call.enqueue(object : Callback<PostResponseData> {
            override fun onResponse(
                call: Call<PostResponseData>,
                response: Response<PostResponseData>
            ) {
                if (response.isSuccessful) {
                    val returnedResponse = response.body()
                    if (returnedResponse != null) {
                        Log.d(TAG, "postData(): Data: ${returnedResponse.ad_link}")
                        updateHomeAdLink(ad_link = returnedResponse.ad_link)
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

/**Home Screen */
    private fun updateHomeAdLink(ad_link: String?) {
        demoUiState.value = demoUiState.value.copy(
            homeAdLink = ad_link
        )
    }


    private fun getApiInterface() {
        apiInterface = RetrofitInstance.getInstance().create(ApiInterface::class.java)
    }

    //Navigation
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

    fun showSettingsScreen() {
        demoUiState.value = demoUiState.value.copy(
            screenType = ScreenType.Settings
        )
    }




    val minimalIdentifiers = MinimalIdentifiers(
        advertisingType = "Anonymous",
        locale = "en_US",
        screenWidth = 1080,
        screenHeight = 1920,
        deviceModel = "Pixel",
        osVersion = "Android 10",
    )


    fun postHomeRandomAdRequest() {



        val call = apiInterface.postRandomAdRequest(minimalIdentifiers)

        call.enqueue(object : Callback<PostResponseData> {
            override fun onResponse(
                call: Call<PostResponseData>,
                response: Response<PostResponseData>
            ) {
                if (response.isSuccessful) {
                    val returnedResponse = response.body()
                    if (returnedResponse != null) {
                        Log.d(TAG, "postRandomAdRequest(): Data: ${returnedResponse.ad_link}")
                        updateHomeAdLink(ad_link = returnedResponse.ad_link)
                        Log.d(TAG, "postRandomAdRequest(): Error: ${returnedResponse.error}")

                        // Handle the response here
                    }
                } else {
                    Log.e(
                        TAG,
                        "postRandomAdRequest(): Request failed with status: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<PostResponseData>, t: Throwable) {
                Log.e(TAG, "postRandomAdRequest(): Request failed: ${t.message}")
            }
        })
    }

    /**Settings Screen */

   fun onSwitchCheckedChange(isLocationEnabled: Boolean) {
       demoUiState.value = demoUiState.value.copy(
           showLocationPermissionPrompt = isLocationEnabled
       )
   }


    /**Details Screen */
    private fun updateDetailsAdLink(ad_link: String?) {
        Log.d(TAG, "updateDetailsAdLink(): ad_link: $ad_link")
        demoUiState.value = demoUiState.value.copy(
            detailsAdLink = ad_link
        )
    }
    fun postSuitableAdRequest(){
        if (demoUiState.value.isLocationGranted) {
            // Permission granted, update the location
            postLocalAdRequest(40.7128, -74.0060)
        } else {
            // Permission was not granted
            postDetailsRandomAdRequest()
        }
    }

    fun postLocalAdRequest(lat: Double, lng: Double) {

        val call = apiInterface.postLocation(Location(lat.toString(), lng.toString()))

        call.enqueue(object : Callback<PostResponseData> {
            override fun onResponse(
                call: Call<PostResponseData>,
                response: Response<PostResponseData>
            ) {
                if (response.isSuccessful) {
                    val returnedResponse = response.body()
                    if (returnedResponse != null) {
                        Log.d(TAG, "postLocation(): Data: ${returnedResponse.ad_link}")
                        updateDetailsAdLink(ad_link = returnedResponse.ad_link)
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
    fun postDetailsRandomAdRequest() {

        val call = apiInterface.postRandomAdRequest(minimalIdentifiers)

        call.enqueue(object : Callback<PostResponseData> {
            override fun onResponse(
                call: Call<PostResponseData>,
                response: Response<PostResponseData>
            ) {
                if (response.isSuccessful) {
                    val returnedResponse = response.body()
                    if (returnedResponse != null) {
                        Log.d(TAG, "postDetailsRandomAdRequest(): Data: ${returnedResponse.ad_link}")
                        updateDetailsAdLink(ad_link = returnedResponse.ad_link)
                        Log.d(TAG, "postDetailsRandomAdRequest(): Error: ${returnedResponse.error}")

                        // Handle the response here
                    }
                } else {
                    Log.e(
                        TAG,
                        "postDetailsRandomAdRequest(): Request failed with status: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<PostResponseData>, t: Throwable) {
                Log.e(TAG, "postDetailsRandomAdRequest(): Request failed: ${t.message}")
            }
        })
    }

}

data class DemoUiState(
    var isPrivacyPolicyDismissed: Boolean = false,
    var homeAdLink: String? = null,
    var detailsAdLink: String? = null,
    var screenType: ScreenType = ScreenType.HomeScreen,
    var currentNews: FakeNews = fakeNewsList[0],
    val showLocationPermissionPrompt: Boolean = false,
    var isLocationGranted: Boolean = false,
)