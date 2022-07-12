package com.damiancz2.personalstats.module

import com.damiancz2.personalstats.AnswerManager
import com.damiancz2.personalstats.FileBasedAnswerManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindAnswerManager(
        manager: FileBasedAnswerManager
    ) : AnswerManager
}