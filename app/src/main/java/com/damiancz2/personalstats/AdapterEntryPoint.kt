package com.damiancz2.personalstats

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AdapterEntryPoint {
    fun getQuestionManager() : QuestionManager
}