package com.damiancz2.personalstats

import android.content.Context
import com.damiancz2.personalstats.model.Questionnaire

interface QuestionnaireManager {

    fun saveQuestionnaire(context: Context, questionnaires: Questionnaire)

    fun getQuestionnaires(context: Context): ArrayList<Questionnaire>

    fun deleteQuestionnaire(context: Context, questionnaireId: Int): List<Questionnaire>

    fun replaceQuestionnaire(context: Context, questionnaireId: Int, questionnaire: Questionnaire)
}