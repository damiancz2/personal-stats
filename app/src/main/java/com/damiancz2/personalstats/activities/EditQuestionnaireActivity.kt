package com.damiancz2.personalstats.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.QUESTIONNAIRE_NAME
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.getQuestionnaires
import com.damiancz2.personalstats.model.Questionnaire
import com.damiancz2.personalstats.saveQuestionnaires

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

            val textView: TextView = findViewById(R.id.EditQuestionnaireNameTextBox)
            val editText: EditText = findViewById(R.id.QuestionnaireNameInputEditText)

            textView.text = questionnaireName
            editText.setText(questionnaireName, TextView.BufferType.EDITABLE)

            val addQuestionButton: Button = findViewById(R.id.AddQuestionButton)
            addQuestionButton.setOnClickListener {
                val intent = Intent(this, AddQuestionActivity::class.java)
                intent.putExtra(QUESTIONNAIRE_NAME, questionnaireName)
                intent.putExtra(QUESTIONNAIRE_ID, questionnaireId)
                startActivity(intent)
            }

            val editQuestionButton: ImageView = findViewById(R.id.btnEditQuestionnaireName)
            editQuestionButton.setOnClickListener{
                val tick: ImageView = findViewById(R.id.btnApproveQuestionnaireName)
                editText.visibility = View.VISIBLE
                tick.visibility = View.VISIBLE
                editQuestionButton.visibility = View.GONE
                textView.visibility = View.GONE
                tick.setOnClickListener{
                    val questionnaires: ArrayList<Questionnaire> = getQuestionnaires(this)
                    val questionnaire = questionnaires.filter{it.id == questionnaireId}[0]
                    val index = questionnaires.indexOf(questionnaire)
                    val newQuestionnaire = Questionnaire(questionnaireId, editText.text.toString())
                    questionnaires.remove(questionnaire)
                    questionnaires.add(index, newQuestionnaire)
                    saveQuestionnaires(this, questionnaires)

                    textView.text = editText.text.toString()
                    editText.visibility = View.GONE
                    tick.visibility = View.GONE
                    editQuestionButton.visibility = View.VISIBLE
                    textView.visibility = View.VISIBLE
                }
            }

            val backToMainButton: Button = findViewById(R.id.BackToMainEditQuestionnaire)
            backToMainButton.setOnClickListener{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }


    }
}