import com.example.eccr_demo.data.DeviceIdentifiers
import com.example.eccr_demo.data.Location
import com.example.eccr_demo.data.Locator
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
    fun postDeviceIdentifiers(@Body deviceIdentifiers:  DeviceIdentifiers): Call<PostResponseData>


    @POST("germanAd")
    fun postLocation(@Body location: Location): Call<PostResponseData>


    @POST("germanAd")
    fun postLocator(@Body locator: Locator): Call<PostResponseData>

}



