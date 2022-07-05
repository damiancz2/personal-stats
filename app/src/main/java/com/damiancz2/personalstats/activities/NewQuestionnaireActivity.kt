package com.damiancz2.personalstats.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.getQuestionnaires
import com.damiancz2.personalstats.model.Questionnaire
import com.damiancz2.personalstats.saveQuestionnaires
import java.util.ArrayList
import java.util.stream.Collectors

class NewQuestionnaireActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_questionnaire)

        val submitButton: Button = findViewById(R.id.SubmitButton)
        submitButton.setOnClickListener{
            save()
            val intent = Intent(this, SubmittedActivity::class.java)
            startActivity(intent)
        }
    }

    fun save() {
        val questionnaireId = createUniqueQuestionnaireId()

        val inputText : EditText = findViewById(R.id.QuestionnaireNameInputTextBox)
        val questionnaire = Questionnaire(
            id = questionnaireId,
            name = inputText.text.toString()
        )
        addToSavedQuestionnaires(questionnaire)
    }

    private fun addToSavedQuestionnaires(questionnaire: Questionnaire) {
        val questionnaireList: ArrayList<Questionnaire> = getQuestionnaires(this)
        questionnaireList.add(questionnaire)
        saveQuestionnaires(this, questionnaireList)
    }

    private fun createUniqueQuestionnaireId(): Int {
        val maxCurrent: Int? = getQuestionnaires(this).stream()
            .map{q -> q.id}
            .collect(Collectors.toSet())
            .maxOrNull()

        return if (maxCurrent == null) {
            1
        } else {
            maxCurrent + 1
        }
    }
}