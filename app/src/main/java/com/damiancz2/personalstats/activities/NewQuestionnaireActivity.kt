package com.damiancz2.personalstats.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.QUESTIONNAIRE_NAME
import com.damiancz2.personalstats.QuestionnaireManager
import com.damiancz2.personalstats.QuestionnaireReminderReceiver
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.model.Questionnaire
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import java.util.Calendar
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

        val inputText : EditText = findViewById(R.id.NewQuestionnaireNameInputTextBox)
        val questionnaire = Questionnaire(
            id = questionnaireId,
            name = inputText.text.toString()
        )
        setNotifications(questionnaire)
        addToSavedQuestionnaires(questionnaire)
        return questionnaire
    }

    private fun addToSavedQuestionnaires(questionnaire: Questionnaire) {
        val questionnaireList: ArrayList<Questionnaire> = questionnaireManager.getQuestionnaires(this)
        questionnaireList.add(questionnaire)
        questionnaireManager.saveQuestionnaires(this, questionnaireList)
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

    private fun setNotifications(questionnaire: Questionnaire) {
        val reminderIntent = Intent(this, QuestionnaireReminderReceiver::class.java)
        reminderIntent.putExtra(QUESTIONNAIRE_ID, questionnaire.id)
        reminderIntent.putExtra(QUESTIONNAIRE_NAME, questionnaire.name)
        val pendingIntent: PendingIntent = PendingIntent
            .getBroadcast(this, questionnaire.id, reminderIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 20)
        calendar.set(Calendar.MINUTE, 40)
        calendar.set(Calendar.SECOND, 0)

        val day: Long = 24 * 60 * 60 * 1000

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, day, pendingIntent)
    }
}