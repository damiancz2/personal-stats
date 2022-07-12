package com.damiancz2.personalstats

import android.content.Context
import com.damiancz2.personalstats.model.Question

interface QuestionManager {
    fun saveQuestions(context: Context, questionnaireId: Int, questions: List<Question>)
    fun getQuestions(context: Context, questionnaireId: Int): ArrayList<Question>
}