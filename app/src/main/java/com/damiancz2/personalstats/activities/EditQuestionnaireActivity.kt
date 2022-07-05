package com.damiancz2.personalstats.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.QUESTIONNAIRE_NAME
import com.damiancz2.personalstats.R

class EditQuestionnaireActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_questionnaire)
        val questionnaireName: String?
        val questionnaireId: Int
        val bundle: Bundle
        if (intent.extras != null) {
            bundle = intent.extras!!
            questionnaireId = bundle.getInt(QUESTIONNAIRE_ID)
            questionnaireName = bundle.getString(QUESTIONNAIRE_NAME)

            val textView: TextView = findViewById(R.id.QuestionnaireNameInputTextBox)
            textView.text = questionnaireName

            val addQuestionButton: Button = findViewById(R.id.AddQuestionButton)
            addQuestionButton.setOnClickListener {
                val intent = Intent(this, AddQuestionActivity::class.java)
                intent.putExtra(QUESTIONNAIRE_NAME, questionnaireName)
                intent.putExtra(QUESTIONNAIRE_ID, questionnaireId)
                startActivity(intent)
            }
        }


    }
}