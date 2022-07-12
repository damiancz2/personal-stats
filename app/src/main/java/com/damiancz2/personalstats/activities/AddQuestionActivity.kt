package com.damiancz2.personalstats.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.QUESTIONNAIRE_NAME
import com.damiancz2.personalstats.QuestionManager
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.model.AnswerType
import com.damiancz2.personalstats.model.Question
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class AddQuestionActivity : AppCompatActivity() {

    @Inject lateinit var questionManager: QuestionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)

        val addQuestionsButton: Button = findViewById(R.id.AddQuestionSubmitButton)

        addQuestionsButton.setOnClickListener{
            saveQuestion()
            val questionnaireId = intent.getIntExtra(QUESTIONNAIRE_ID, 1)
            val questionnaireName = intent.getStringExtra(QUESTIONNAIRE_NAME)
            val intent = Intent(this, EditQuestionnaireActivity::class.java)
            intent.putExtra(QUESTIONNAIRE_ID, questionnaireId)
            intent.putExtra(QUESTIONNAIRE_NAME, questionnaireName)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun saveQuestion() {
        val extras = intent.extras!!
        val questionnaireId = extras.getInt(QUESTIONNAIRE_ID)

        val inputText : EditText = findViewById(R.id.AddQuestionInputTextBox)
        val question = inputText.text.toString()

        val radioGroup : RadioGroup = findViewById(R.id.AnswerTypeRadioGroup)
        val checkedButtonId: Int = radioGroup.checkedRadioButtonId

        val questionList: ArrayList<Question> = questionManager.getQuestions(this, questionnaireId)

        val questionId = UUID.randomUUID().toString()

        val sampleQuestion = Question(
            id = questionId,
            question = question,
            answerType = getAnswerType(checkedButtonId)
        )

        questionList.add(sampleQuestion)

        questionManager.saveQuestions(this, questionnaireId, questionList)
    }

    private fun getAnswerType(buttonId: Int) : AnswerType {
        return when(buttonId) {
            R.id.NumberAnswerTypeRadioButton -> AnswerType.NUMBER
            R.id.TextAnswerTypeRadioButton -> AnswerType.TEXT
            R.id.TimeAnswerTypeRadioButton -> AnswerType.TIME
            R.id.YesNoAnswerTypeRadioButton -> AnswerType.YESNO
            else -> AnswerType.TEXT
        }
    }
}