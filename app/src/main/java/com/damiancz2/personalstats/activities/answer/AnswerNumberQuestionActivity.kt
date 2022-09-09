package com.damiancz2.personalstats.activities.answer

import android.widget.TextView
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.fragments.NumberAnswerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnswerNumberQuestionActivity : AbstractAnswerQuestionActivity<TextView>() {

    override fun prepare() {
        val numberFragment = NumberAnswerFragment()

        supportFragmentManager.beginTransaction().apply{
            replace(R.id.answerFrameLayout, numberFragment)
            commit()
        }
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