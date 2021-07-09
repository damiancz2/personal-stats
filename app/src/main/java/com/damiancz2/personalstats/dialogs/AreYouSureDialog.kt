package com.damiancz2.personalstats.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.damiancz2.personalstats.adapter.QuestionnaireAdapter
import com.damiancz2.personalstats.getQuestionnaires
import com.damiancz2.personalstats.model.Questionnaire
import com.damiancz2.personalstats.saveQuestionnaires

class AreYouSureDialog(private val questionnaire: Questionnaire,
                       private val adapter: QuestionnaireAdapter
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Are you sure you want to delete questionnaire " + questionnaire.name +
                     "? It will delete all the data for this questionnaire")
                .setNegativeButton("No") { _, _ -> }
                .setPositiveButton("Yes") { _, _ -> deleteQuestionnaire() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun deleteQuestionnaire() {
        val questionnaires: ArrayList<Questionnaire> = getQuestionnaires(requireContext())
        val resultQuestionnaires =
            questionnaires.filter { qnare -> qnare.id != questionnaire.id }
        adapter.setQuestionnaires(resultQuestionnaires)
        saveQuestionnaires(requireContext(), resultQuestionnaires)
    }

}