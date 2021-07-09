package com.damiancz2.personalstats.model

class Question(
    val id: String,
    val question: String,
    val answerType: AnswerType = AnswerType.TEXT) {
}