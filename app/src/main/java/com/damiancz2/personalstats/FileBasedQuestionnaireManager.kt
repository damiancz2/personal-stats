package com.damiancz2.personalstats

import android.content.Context
import com.damiancz2.personalstats.model.Questionnaire
import com.google.gson.reflect.TypeToken
import java.io.File
import javax.inject.Inject

class FileBasedQuestionnaireManager @Inject constructor() : QuestionnaireManager {

    override fun saveQuestionnaires(context: Context, questionnaires: List<Questionnaire>) {
        val file = getQuestionnairesFile(context)
        val questionnairesString: String = GSON.toJson(questionnaires)
        file.writeText(questionnairesString)
    }

    override fun getQuestionnaires(context: Context): ArrayList<Questionnaire> {
        val file = getQuestionnairesFile(context)
        if (file.exists()) {
            val answersString = file.readText()
            val itemType = object : TypeToken<ArrayList<Questionnaire>>() {}.type
            return GSON.fromJson(answersString, itemType)
        } else {
            return ArrayList()
        }
    }

    override fun deleteQuestionnaire(context: Context, questionnaireId: Int) {
        val questionnaireDirectory = getQuestionnaireDirectory(context, questionnaireId)
        deleteRecursive(questionnaireDirectory)
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