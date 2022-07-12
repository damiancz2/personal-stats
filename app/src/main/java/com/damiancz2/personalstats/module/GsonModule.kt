package com.damiancz2.personalstats.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GsonModule {

    @Provides
    fun providesGson() : Gson {
        return GsonBuilder().setPrettyPrinting().create()
    }
}