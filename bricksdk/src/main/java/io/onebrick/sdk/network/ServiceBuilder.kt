package io.onebrick.sdk.network
import io.onebrick.sdk.model.ConfigStorage
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ServiceBuilder {
    private val client = OkHttpClient.Builder()
        .connectTimeout(timeout = 60, unit = TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
    private val retrofitAPIV1 = Retrofit.Builder()
        .baseUrl(ConfigStorage.urlV2API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    fun<T> buildService(service: Class<T>): T{
        return retrofitAPIV1.create(service)
    }
}