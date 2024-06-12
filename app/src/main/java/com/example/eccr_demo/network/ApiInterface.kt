import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @GET("helloworld")
    fun receiveData(): Call<ReceivedData>

    @POST("helloworld")
    fun postData(@Body postRequestData: PostRequestData): Call<PostResponseData>
}


data class ReceivedData(val data: String)//the data the server returns after a get request
data class PostResponseData(  val received: String? = null,
                              val error: String? = null) // the data the server returns after a post request
data class PostRequestData(val ip: String,val mac: String) // the data sent to the server in a post request