package com.damiancz2.personalstats.activities.answer

import android.widget.RadioGroup
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.fragments.YesNoAnswerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnswerYesNoQuestionActivity : AbstractAnswerQuestionActivity<RadioGroup>() {

    override fun prepare() {
        val yesNoFragment = YesNoAnswerFragment()

        supportFragmentManager.beginTransaction().apply{
            replace(R.id.answerFrameLayout, yesNoFragment)
            commit()
        }
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