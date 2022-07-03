package com.damiancz2.personalstats.model

import android.content.Context
import android.content.Intent
import com.damiancz2.personalstats.activities.answer.AbstractAnswerQuestionActivity
import com.damiancz2.personalstats.activities.answer.AnswerNumberQuestionActivity
import com.damiancz2.personalstats.activities.answer.AnswerTextQuestionActivity
import com.damiancz2.personalstats.activities.answer.AnswerTimeQuestionActivity
import com.damiancz2.personalstats.activities.answer.AnswerYesNoQuestionActivity

enum class AnswerType(
    private val answerActivityClass : Class<out AbstractAnswerQuestionActivity<out Any>>)
{
    TEXT(AnswerTextQuestionActivity::class.java),
    NUMBER(AnswerNumberQuestionActivity::class.java),
    TIME(AnswerTimeQuestionActivity::class.java),
    YESNO(AnswerYesNoQuestionActivity::class.java);

    fun createIntent(context: Context) : Intent {
        return Intent(context, this.answerActivityClass)
    }
}
