package com.damiancz2.personalstats.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.QUESTION_ID
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.adapter.AnswerAdapter
import com.damiancz2.personalstats.getAnswers
import com.damiancz2.personalstats.model.Answer

class ViewAnswersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_answers)

        val backToMainButton: Button = findViewById(R.id.BackToMainViewAnswers)
        backToMainButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        if (intent.extras != null) {
            val extras: Bundle = intent.extras!!
            val questionId = extras.getString(QUESTION_ID)
            val questionnaireId = extras.getString(QUESTIONNAIRE_ID)
            val answers : ArrayList<Answer> = getAnswers(this, questionnaireId!!, questionId!!)
            val filteredAnswers: List<Answer> = answers
                .filter { answer -> answer.questionId == questionId }
            val adapter = AnswerAdapter(filteredAnswers)
            val answerListView : RecyclerView = findViewById(R.id.AnswerListView)
            answerListView.setHasFixedSize(true)
            answerListView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
            answerListView.adapter = adapter
        }

    }
}