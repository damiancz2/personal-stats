package com.damiancz2.personalstats

import android.content.Context
import com.damiancz2.personalstats.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import javax.inject.Inject

class FileBasedQuestionManager @Inject constructor(): QuestionManager {
    @Inject lateinit var gson: Gson

    override fun saveQuestion(context: Context, questionnaireId: Int, question: Question) {
        val questions = getQuestions(context, questionnaireId)
        questions.add(question)
        val questionsString: String = gson.toJson(questions)
        val file = getQuestionsFile(context, questionnaireId)
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

    override fun deleteQuestion(context: Context, questionnaireId: Int, questionId: String) {
        val questions = getQuestions(context, questionnaireId)
        val resultQuestions = questions.filter{q -> q.id != questionId}
        val questionsString: String = gson.toJson(resultQuestions)
        val file = getQuestionsFile(context, questionnaireId)
        file.writeText(questionsString)
    }

    private fun getQuestionsFile(context: Context, questionnaireId: Int): File {
        return File(context.filesDir.absolutePath + "/questionnaires/" + questionnaireId + "/questions.txt")
    }

    private fun getQuestionnaireDirectory(context: Context, questionnaireId: Int) : File {
        return File(context.filesDir.absolutePath + "/questionnaires/" + questionnaireId)
    }
}