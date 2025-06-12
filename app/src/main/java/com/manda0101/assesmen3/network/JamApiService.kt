package com.manda0101.assesmen3.network

import com.manda0101.assesmen3.model.Jam
import com.manda0101.assesmen3.model.JamStatus
import com.manda0101.assesmen3.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://ac92-35-229-205-246.ngrok-free.app/api/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface JamApiService {
    @GET("jam")
    suspend fun getJam(
        @Header("Authorization") token: String
    ): JamStatus

    @Multipart
    @POST("jam")
    suspend fun postJam(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @Multipart
    @POST("jam/{id_jam}")
    suspend fun updateJam(
        @Header("Authorization") token: String,
        @Part("_method") method: RequestBody,
        @Path("id_jam") id_jam: Long,
        @Part("name") name: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): OpStatus

    @DELETE("jam/{id_jam}")
    suspend fun deleteJam(
        @Header("Authorization") token: String,
        @Path("id_jam") id_jam: Long
    ): OpStatus

    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") nama: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): OpStatus
}

object JamApi {
    val service: JamApiService by lazy {
        retrofit.create(JamApiService::class.java)
    }

    fun getImageUrl(id: Long): String {
        return "${BASE_URL}jam/image/$id"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }