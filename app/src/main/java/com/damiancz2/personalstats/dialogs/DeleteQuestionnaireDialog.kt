package com.damiancz2.personalstats.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.damiancz2.personalstats.QuestionnaireManager
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.adapter.QuestionnaireAdapter
import com.damiancz2.personalstats.model.Questionnaire
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeleteQuestionnaireDialog(private val questionnaire: Questionnaire,
                                private val adapter: QuestionnaireAdapter
) : DialogFragment() {

    @Inject lateinit var questionnaireManager: QuestionnaireManager

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.sure_delete_questionnaire) + " " + questionnaire.name +
                     "? "+ getString(R.string.it_will_delete_questionnaire_data))
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
                .setPositiveButton(getString(R.string.yes)) { _, _ -> deleteQuestionnaire() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun deleteQuestionnaire() {
        val resultQuestionnaires = questionnaireManager.deleteQuestionnaire(requireContext(), questionnaire.id)
        adapter.setQuestionnaires(resultQuestionnaires)
    }

}