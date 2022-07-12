package com.damiancz2.personalstats.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.adapter.QuestionnaireAdapter
import com.damiancz2.personalstats.getQuestionnaires
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val questionnaires = getQuestionnaires(this)
        val adapter = QuestionnaireAdapter(questionnaires, supportFragmentManager)
        val questionListView : RecyclerView = findViewById(R.id.QuestionnaireRecView)
        questionListView.setHasFixedSize(false)
        questionListView.layoutManager = LinearLayoutManager(this)
        questionListView.adapter = adapter

        val newQuestionnairesButton : Button = findViewById(R.id.NewQuestionnaireButton)
        newQuestionnairesButton.setOnClickListener{
            val intent = Intent(this, NewQuestionnaireActivity::class.java)
            startActivity(intent)
        }
    }
}