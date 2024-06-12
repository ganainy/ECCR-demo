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
import PostRequestData
import ReceivedData
import PostResponseData
import android.content.res.Resources
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


/**
 * UI state for the Home screen
 */
sealed interface DemoUiState {
   // data class Success(val photos: List<MarsPhoto>) : MarsUiState
    object Error : DemoUiState
    object Loading : DemoUiState
}

class DemoViewModel : ViewModel() {
    private val TAG= "ECCRViewModel"
    private lateinit var apiInterface: ApiInterface
    /** The mutable State that stores the status of the most recent request */
    var demoUiState: DemoUiState by mutableStateOf(DemoUiState.Loading)
        private set


    init {
        getApiInterface()

        collectDeviceInfo()

        /*getData()
        postData()*/
    }

    private fun collectDeviceInfo() {
        val deviceModel = Build.MODEL
        val osVersion = Build.VERSION.RELEASE
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val locale = Locale.getDefault()

        Log.d("AppInfo", "Device Model: $deviceModel")
        Log.d("AppInfo", "Operating System: $osVersion")
        Log.d("AppInfo", "Screen Resolution: $screenWidth x $screenHeight")
        Log.d("AppInfo", "Locale: $locale")
    }
    //ip address, location, mac adress, android version
    //burpsuite fiddler everywhere




    //monkey ui automator
    //jadx apktool
    private fun postData() {
        val call = apiInterface.postData(PostRequestData("1.1.1.1","DFSDGSGSDGSH"))

        call.enqueue(object : Callback<PostResponseData> {
            override fun onResponse(call: Call<PostResponseData>, response: Response<PostResponseData>) {
                if (response.isSuccessful) {
                    val returnedResponse = response.body()
                    if (returnedResponse != null) {
                        Log.d(TAG, "postData(): Data: ${returnedResponse.received}")
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


    private fun getApiInterface() {
        apiInterface = RetrofitInstance.getInstance().create(ApiInterface::class.java)
    }

    private fun getData(){
        val call = apiInterface.receiveData()
        call.enqueue(object : Callback<ReceivedData> {
            override fun onResponse(call: Call<ReceivedData>, response: Response<ReceivedData>) {
                if (response.isSuccessful && response.body()!=null){
                    Log.d(TAG, " getData(): onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<ReceivedData>, t: Throwable) {
                t.printStackTrace()
                Log.d(TAG, " getData(): onFailure: ${t.message}")
            }
        })
    }


}
