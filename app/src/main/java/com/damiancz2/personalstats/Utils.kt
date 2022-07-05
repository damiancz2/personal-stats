package com.damiancz2.personalstats

import android.content.Context
import com.damiancz2.personalstats.model.Answer
import com.damiancz2.personalstats.model.Question
import com.damiancz2.personalstats.model.Questionnaire
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

fun saveAnswers(context: Context, questionnaireId: Int, questionId: String, answers: List<Answer>) {
    val file = getAnswersFile(context, questionnaireId, questionId)
    getQuestionnaireAnswersDirectory(context, questionnaireId).mkdirs()
    val answersString: String = GSON.toJson(answers)
    file.writeText(answersString)
}

fun getAnswers(context: Context, questionnaireId: Int, questionId: String): ArrayList<Answer> {
    val file = getAnswersFile(context, questionnaireId, questionId)
    getQuestionnaireAnswersDirectory(context, questionnaireId).mkdirs()
    if (file.exists()) {
        val answersString = file.readText()
        val itemType = object : TypeToken<ArrayList<Answer>>() {}.type
        return GSON.fromJson(answersString, itemType)
    } else {
        return ArrayList()
    }
}

fun saveQuestions(context: Context, questionnaireId: Int, questions: List<Question>) {
    val file = getQuestionsFile(context, questionnaireId)
    getQuestionnaireDirectory(context, questionnaireId).mkdirs()
    val questionsString: String = GSON.toJson(questions)
    file.writeText(questionsString)
}

fun getQuestions(context: Context, questionnaireId: Int): ArrayList<Question> {
    val file = getQuestionsFile(context, questionnaireId)
    getQuestionnaireDirectory(context, questionnaireId).mkdirs()
    if (file.exists()) {
        val answersString = file.readText()
        val itemType = object : TypeToken<ArrayList<Question>>() {}.type
        return GSON.fromJson(answersString, itemType)
    } else {
        return ArrayList()
    }
}

fun saveQuestionnaires(context: Context, questionnaires: List<Questionnaire>) {
    val file = getQuestionnairesFile(context)
    val questionnairesString: String = GSON.toJson(questionnaires)
    file.writeText(questionnairesString)
}

fun getQuestionnaires(context: Context): ArrayList<Questionnaire> {
    val file = getQuestionnairesFile(context)
    if (file.exists()) {
        val answersString = file.readText()
        val itemType = object : TypeToken<ArrayList<Questionnaire>>() {}.type
        return GSON.fromJson(answersString, itemType)
    } else {
        return ArrayList()
    }
}

fun deleteQuestionnaireDirectory(context: Context, questionnaireId: Int) {
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

private fun getQuestionsFile(context: Context, questionnaireId: Int): File {
    return File(context.filesDir.absolutePath + "/questionnaires/" + questionnaireId + "/questions.txt")
}

private fun getAnswersFile(context: Context, questionnaireId: Int, questionId: String) : File {
    return File(context.filesDir.absolutePath + "/questionnaires/" + questionnaireId + "/answers/" + questionId + ".txt")
}

private fun getQuestionnaireDirectory(context: Context, questionnaireId: Int) : File {
    return File(context.filesDir.absolutePath + "/questionnaires/" + questionnaireId)
}

private fun getQuestionnaireAnswersDirectory(context: Context, questionnaireId: Int) : File {
    return File(context.filesDir.absolutePath + "/questionnaires/" + questionnaireId + "/answers")
}