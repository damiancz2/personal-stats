package com.damiancz2.personalstats.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.QuestionManager
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.adapter.QuestionAdapter
import com.damiancz2.personalstats.model.Question
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViewQuestionsActivity : AppCompatActivity() {

    @Inject lateinit var questionManager: QuestionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_questions)

        val extras = intent.extras!!
        val questionnaireId = extras.getInt(QUESTIONNAIRE_ID)

        val questions: ArrayList<Question> = questionManager.getQuestions(this, questionnaireId)
        val adapter = QuestionAdapter(questions, questionnaireId!!)
        val questionListView : RecyclerView = findViewById(R.id.QuestionListView)
        questionListView.setHasFixedSize(true)
        questionListView.layoutManager = LinearLayoutManager(this)
        questionListView.adapter = adapter
    }
}