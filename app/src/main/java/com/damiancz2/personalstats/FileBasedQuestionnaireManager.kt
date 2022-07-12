package com.damiancz2.personalstats

import android.content.Context
import com.damiancz2.personalstats.model.Questionnaire
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import javax.inject.Inject

class FileBasedQuestionnaireManager @Inject constructor() : QuestionnaireManager {

    @Inject lateinit var gson: Gson

    override fun saveQuestionnaire(context: Context, questionnaire: Questionnaire) {
        val questionnaires = getQuestionnaires(context)
        questionnaires.add(questionnaire)
        val file = getQuestionnairesFile(context)
        val questionnairesString: String = gson.toJson(questionnaires)
        file.writeText(questionnairesString)
    }

    override fun getQuestionnaires(context: Context): ArrayList<Questionnaire> {
        val file = getQuestionnairesFile(context)
        if (file.exists()) {
            val answersString = file.readText()
            val itemType = object : TypeToken<ArrayList<Questionnaire>>() {}.type
            return gson.fromJson(answersString, itemType)
        } else {
            return ArrayList()
        }
    }

    override fun deleteQuestionnaire(context: Context, questionnaireId: Int): List<Questionnaire> {
        val questionnaires = getQuestionnaires(context)
        val resultQuestionnaires = questionnaires.filter{q -> q.id != questionnaireId}
        val file = getQuestionnairesFile(context)
        file.writeText(gson.toJson(resultQuestionnaires))
        val questionnaireDirectory = getQuestionnaireDirectory(context, questionnaireId)
        deleteRecursive(questionnaireDirectory)
        return resultQuestionnaires
    }

    override fun replaceQuestionnaire(context: Context, questionnaireId: Int, questionnaire: Questionnaire) {
        val questionnaires = getQuestionnaires(context)
        val oldQuestionnaire = questionnaires.filter{q -> q.id == questionnaireId}[0]
        val index = questionnaires.indexOf(oldQuestionnaire)
        questionnaires.remove(oldQuestionnaire)
        questionnaires.add(index, questionnaire)
        val file = getQuestionnairesFile(context)
        file.writeText(gson.toJson(questionnaires))
    }

    private fun deleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) {
            for (file in fileOrDirectory.listFiles()) {
                deleteRecursive(file)
            }
        }

        fileOrDirectory.delete()
    }

    private fun getQuestionnairesFile(context: Context): File {
        return File(context.filesDir.absolutePath + "/questionnaires.txt")
    }

    private fun getQuestionnaireDirectory(context: Context, questionnaireId: Int) : File {
        return File(context.filesDir.absolutePath + "/questionnaires/" + questionnaireId)
    }
}