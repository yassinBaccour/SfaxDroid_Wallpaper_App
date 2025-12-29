package com.sfaxdroid.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sfaxdroid.data.call.ApiRequestImpl
import com.sfaxdroid.data.call.ApiRequester
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiCallModule {

  @Provides
  @Singleton
  fun provideGson(): Gson = GsonBuilder().create()

  @Provides
  @Singleton
  fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
    GsonConverterFactory.create(gson)

  @Provides
  @Singleton
  fun provideApiRequest(): ApiRequester = ApiRequestImpl()
}