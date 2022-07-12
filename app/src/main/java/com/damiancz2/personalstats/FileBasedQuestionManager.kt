package com.damiancz2.personalstats

import android.content.Context
import com.damiancz2.personalstats.model.Question
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import javax.inject.Inject

class FileBasedQuestionManager @Inject constructor(): QuestionManager {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun saveQuestions(context: Context, questionnaireId: Int, questions: List<Question>) {
        val file = getQuestionsFile(context, questionnaireId)
        getQuestionnaireDirectory(context, questionnaireId).mkdirs()
        val questionsString: String = gson.toJson(questions)
        file.writeText(questionsString)
    }

    override fun getQuestions(context: Context, questionnaireId: Int): ArrayList<Question> {
        val file = getQuestionsFile(context, questionnaireId)
        getQuestionnaireDirectory(context, questionnaireId).mkdirs()
        if (file.exists()) {
            val answersString = file.readText()
            val itemType = object : TypeToken<ArrayList<Question>>() {}.type
            return gson.fromJson(answersString, itemType)
        } else {
            return ArrayList()
        }
    }

    private fun getQuestionsFile(context: Context, questionnaireId: Int): File {
        return File(context.filesDir.absolutePath + "/questionnaires/" + questionnaireId + "/questions.txt")
    }

    private fun getQuestionnaireDirectory(context: Context, questionnaireId: Int) : File {
        return File(context.filesDir.absolutePath + "/questionnaires/" + questionnaireId)
    }
}