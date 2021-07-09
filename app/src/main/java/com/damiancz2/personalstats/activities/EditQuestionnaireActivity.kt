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
        val questionnaireId: String?
        val bundle: Bundle
        if (intent.extras != null) {
            bundle = intent.extras!!
            questionnaireId = bundle.getString(QUESTIONNAIRE_ID)
            questionnaireName = bundle.getString(QUESTIONNAIRE_NAME)

            val textView: TextView = findViewById(R.id.QuestionnaireNameInputTextBox)
            textView.text = questionnaireName

            val addQuestionButton: Button = findViewById(R.id.AddQuestionButton)
            addQuestionButton.setOnClickListener {
                val intent = Intent(this, AddQuestionActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }


    }
}