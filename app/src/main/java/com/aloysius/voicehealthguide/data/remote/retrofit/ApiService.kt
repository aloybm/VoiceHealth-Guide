package com.aloysius.voicehealthguide.data.remote.retrofit

import com.aloysius.voicehealthguide.data.remote.response.DrugsResponse
import com.aloysius.voicehealthguide.data.remote.response.ForgotPasswordResponse
import com.aloysius.voicehealthguide.data.remote.response.HistoryResponse
import com.aloysius.voicehealthguide.data.remote.response.LoginResponse
import com.aloysius.voicehealthguide.data.remote.response.RegisterResponse
import com.aloysius.voicehealthguide.data.remote.response.NewsResponse
import com.aloysius.voicehealthguide.data.remote.response.ResetPasswordResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("news")
    suspend fun getNews(): NewsResponse

    @FormUrlEncoded
    @POST("drug")
    suspend fun getDrug(
        @Field("keluhan") keluhan: String,
    ): DrugsResponse

    @GET("history")
    suspend fun getHistory(): HistoryResponse

    @FormUrlEncoded
    @POST("auth/reset-password-request")
    suspend fun forgotPassword(
        @Field("email") email: String,
    ): ForgotPasswordResponse

    @FormUrlEncoded
    @PATCH("auth/reset-password")
    suspend fun resetPassword(
        @Query("token") token: String,
        @Field("password") password: String
    ): ResetPasswordResponse
}