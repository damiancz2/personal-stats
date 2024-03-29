package com.damiancz2.personalstats

import android.content.Context
import com.damiancz2.personalstats.model.Answer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import javax.inject.Inject

class FileBasedAnswerManager @Inject constructor() : AnswerManager {

    @Inject lateinit var gson: Gson

    override fun saveAnswer(context: Context, questionnaireId: Int, questionId: String, answer: Answer) {
        val answers = getAnswers(context, questionnaireId, questionId)
        answers.add(answer)
        val file = getAnswersFile(context, questionnaireId, questionId)
        getQuestionnaireAnswersDirectory(context, questionnaireId).mkdirs()
        val answersString: String = gson.toJson(answers)
        file.writeText(answersString)
    }

    override fun getAnswers(context: Context, questionnaireId: Int, questionId: String): ArrayList<Answer> {
        val file = getAnswersFile(context, questionnaireId, questionId)
        getQuestionnaireAnswersDirectory(context, questionnaireId).mkdirs()
        return if (file.exists()) {
            val answersString = file.readText()
            val itemType = object : TypeToken<ArrayList<Answer>>() {}.type
            gson.fromJson(answersString, itemType)
        } else {
            ArrayList()
        }
    }


    private fun getQuestionnaireAnswersDirectory(context: Context, questionnaireId: Int) : File {
        return File(context.filesDir.absolutePath + "/questionnaires/" + questionnaireId + "/answers")
    }


    private fun getAnswersFile(context: Context, questionnaireId: Int, questionId: String) : File {
        return File(context.filesDir.absolutePath + "/questionnaires/" + questionnaireId + "/answers/" + questionId + ".txt")
    }

}