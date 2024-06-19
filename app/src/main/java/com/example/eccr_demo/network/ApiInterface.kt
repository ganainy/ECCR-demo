import com.example.eccr_demo.data.AdvertisingIdentifiers
import com.example.eccr_demo.data.Location
import com.example.eccr_demo.data.MinimalIdentifiers
import com.example.eccr_demo.data.PostResponseData
import com.example.eccr_demo.data.ReceivedData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @GET("helloworld")
    fun receiveData(): Call<ReceivedData>

    @POST("deviceIdentifiers")
    fun postAdvertisingIdentifiers(@Body deviceIdentifiers:  AdvertisingIdentifiers): Call<PostResponseData>

    @POST("randomAd")
    fun postRandomAdRequest(@Body minimalIdentifiers: MinimalIdentifiers): Call<PostResponseData>
    @POST("localAd")
    fun postLocation(@Body location: Location): Call<PostResponseData>


}



