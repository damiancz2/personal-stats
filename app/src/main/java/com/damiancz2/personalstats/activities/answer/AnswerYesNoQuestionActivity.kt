package com.damiancz2.personalstats.activities.answer

import android.widget.RadioGroup
import com.damiancz2.personalstats.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnswerYesNoQuestionActivity : AbstractAnswerQuestionActivity<RadioGroup>() {

    override fun prepare() {
    }

    override fun getLayout(): Int {
        return R.layout.activity_answer_yes_no_question
    }

    override fun getInputViewId(): Int {
        return R.id.yesNoRadioGroup
    }

    override fun getValue(inputView: RadioGroup): String {
        return when (inputView.checkedRadioButtonId) {
            R.id.yesRadioButton -> getString(R.string.yes)
            R.id.noRadioButton -> getString(R.string.no)
            else -> getString(R.string.no)
        }
    }
}