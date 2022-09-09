package com.damiancz2.personalstats.activities.answer

import android.widget.TextView
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.fragments.TextAnswerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnswerTextQuestionActivity : AbstractAnswerQuestionActivity<TextView>() {

    override fun prepare() {
        val textFragment = TextAnswerFragment()

        supportFragmentManager.beginTransaction().apply{
            replace(R.id.answerFrameLayout, textFragment)
            commit()
        }
    }

    override fun getValue(inputView: TextView) : String {
        return inputView.text.toString()
    }

    override fun getInputViewId() : Int {
        return R.id.TextPicker
    }
}