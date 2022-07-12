package com.damiancz2.personalstats

import android.content.Context
import com.damiancz2.personalstats.model.Question

interface QuestionManager {

    fun saveQuestion(context: Context, questionnaireId: Int, question: Question)

    fun getQuestions(context: Context, questionnaireId: Int): ArrayList<Question>

    fun deleteQuestion(context: Context, questionnaireId: Int, questionId: String)
}