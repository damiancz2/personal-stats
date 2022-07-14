package com.damiancz2.personalstats.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.QUESTIONNAIRE_NAME
import com.damiancz2.personalstats.QuestionnaireManager
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.model.Questionnaire
import dagger.hilt.android.AndroidEntryPoint
import java.util.stream.Collectors
import javax.inject.Inject

@AndroidEntryPoint
class NewQuestionnaireActivity : AppCompatActivity() {
    @Inject lateinit var questionnaireManager: QuestionnaireManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_questionnaire)

        val submitButton: Button = findViewById(R.id.NewQuestionnaireSubmitButton)
        submitButton.setOnClickListener{
            val questionnaire: Questionnaire = save()
            val intent = Intent(this, EditQuestionnaireActivity::class.java)
            intent.putExtra(QUESTIONNAIRE_ID, questionnaire.id)
            intent.putExtra(QUESTIONNAIRE_NAME, questionnaire.name)
            startActivity(intent)
        }
    }

    private fun save(): Questionnaire {
        val questionnaireId = createUniqueQuestionnaireId()

        val inputText: EditText = findViewById(R.id.NewQuestionnaireNameInputTextBox)
        val questionnaire = Questionnaire(
            id = questionnaireId,
            name = inputText.text.toString()
        )
        questionnaireManager.saveQuestionnaire(this, questionnaire)
        return questionnaire
    }

    private fun createUniqueQuestionnaireId(): Int {
        val maxCurrent: Int? = questionnaireManager.getQuestionnaires(this).stream()
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