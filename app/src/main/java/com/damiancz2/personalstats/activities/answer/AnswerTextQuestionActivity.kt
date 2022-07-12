package com.damiancz2.personalstats.activities.answer

import android.widget.TextView
import com.damiancz2.personalstats.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnswerTextQuestionActivity : AbstractAnswerQuestionActivity<TextView>() {

    override fun prepare() {
    }

    override fun getLayout(): Int {
        return R.layout.activity_answer_text_question
    }

    override fun getValue(inputView: TextView) : String {
        return inputView.text.toString()
    }

    override fun getInputViewId() : Int {
        return R.id.TextPicker
    }
}