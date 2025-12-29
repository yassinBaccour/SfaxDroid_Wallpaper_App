package com.sfaxdroid.data.call

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun <T> OkHttpClient.createService(
  baseUrl: String,
  converter: GsonConverterFactory,
  service: Class<T>
) = Retrofit.Builder()
  .baseUrl(baseUrl)
  .addConverterFactory(converter)
  .client(this.newBuilder().build())
  .build()
  .create(service)
