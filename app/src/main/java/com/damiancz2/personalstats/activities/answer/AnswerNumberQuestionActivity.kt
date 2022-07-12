package com.damiancz2.personalstats.activities.answer

import android.widget.TextView
import com.damiancz2.personalstats.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnswerNumberQuestionActivity : AbstractAnswerQuestionActivity<TextView>() {

    override fun prepare() {
    }

    override fun getLayout(): Int {
        return R.layout.activity_answer_number_question;
    }

    override fun getInputViewId(): Int {
        return R.id.NumberPicker;
    }

    override fun getValue(inputView: TextView): String {
        val text: String = inputView.text.toString()
        return if (text.isBlank()) {
            "0"
        } else {
            text
        }
    }
}