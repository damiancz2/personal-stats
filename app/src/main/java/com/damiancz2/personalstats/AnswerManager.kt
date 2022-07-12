package com.damiancz2.personalstats

import android.content.Context
import com.damiancz2.personalstats.model.Answer

interface AnswerManager {

    fun saveAnswers(context: Context, questionnaireId: Int, questionId: String, answers: List<Answer>)

    fun getAnswers(context: Context, questionnaireId: Int, questionId: String): ArrayList<Answer>
}