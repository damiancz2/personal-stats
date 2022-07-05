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
import com.damiancz2.personalstats.QuestionnaireReminderReceiver
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.getQuestionnaires
import com.damiancz2.personalstats.model.Questionnaire
import com.damiancz2.personalstats.saveQuestionnaires
import java.time.LocalDateTime
import java.time.ZoneOffset
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
        setNotifications(questionnaire)
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

    private fun setNotifications(questionnaire: Questionnaire) {
        val reminderIntent = Intent(this, QuestionnaireReminderReceiver::class.java)
        reminderIntent.putExtra(QUESTIONNAIRE_ID, questionnaire.id)
        reminderIntent.putExtra(QUESTIONNAIRE_NAME, questionnaire.name)
        val pendingIntent: PendingIntent = PendingIntent
            .getBroadcast(this, questionnaire.id, reminderIntent, 0)

        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val day: Long = 24 * 60 * 60 * 1000

        val today = LocalDateTime.now()
            .withHour(12)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        val firstNotificationTime = today.toEpochSecond(ZoneOffset.UTC)

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstNotificationTime, day, pendingIntent)
    }
}