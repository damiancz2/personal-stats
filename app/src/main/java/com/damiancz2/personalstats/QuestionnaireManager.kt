package com.damiancz2.personalstats

import android.content.Context
import com.damiancz2.personalstats.model.Questionnaire

interface QuestionnaireManager {
    fun saveQuestionnaires(context: Context, questionnaires: List<Questionnaire>)
    fun getQuestionnaires(context: Context): ArrayList<Questionnaire>
    fun deleteQuestionnaire(context: Context, questionnaireId: Int)
}