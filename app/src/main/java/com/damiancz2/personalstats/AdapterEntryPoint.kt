package com.damiancz2.personalstats

import com.google.gson.Gson
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AdapterEntryPoint {
    fun getQuestionManager() : QuestionManager
    fun getGson(): Gson
}