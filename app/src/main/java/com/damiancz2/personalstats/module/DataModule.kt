package com.damiancz2.personalstats.module

import com.damiancz2.personalstats.AnswerManager
import com.damiancz2.personalstats.FileBasedAnswerManager
import com.damiancz2.personalstats.FileBasedQuestionManager
import com.damiancz2.personalstats.FileBasedQuestionnaireManager
import com.damiancz2.personalstats.QuestionManager
import com.damiancz2.personalstats.QuestionnaireManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class, ActivityComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindAnswerManager(
        manager: FileBasedAnswerManager
    ) : AnswerManager

    @Binds
    abstract fun bindQuestionManager(
        manager: FileBasedQuestionManager
    ) : QuestionManager

    @Binds
    abstract fun bindQuestionnaireManager(
        manager: FileBasedQuestionnaireManager
    ) : QuestionnaireManager
}