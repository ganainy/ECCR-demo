package com.example.eccr_demo.data


//the response that will be given back from server after a post request
data class PostResponseData(  val ad_link: String? = null,
                              val error: String? = null) // the data the server returns after a post request
