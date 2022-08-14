package com.damiancz2.personalstats

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.damiancz2.personalstats.activities.SubmittedActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class QuestionnaireReminderReceiver: BroadcastReceiver() {

    @Inject lateinit var questionManager: QuestionManager

    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    override fun onReceive(context: Context?, intent: Intent?) {
        val questionnaireName = intent!!.getStringExtra(QUESTIONNAIRE_NAME)
        val questionnaireId = intent.extras!!.getInt(QUESTIONNAIRE_ID)
        val frequency = intent.extras!!.getString(NOTIFICATION_FREQUENCY)
        val questions = questionManager.getQuestions(context!!, questionnaireId)

        val goToQuestionnaireIntent: Intent
        if (questions.size == 0) {
            goToQuestionnaireIntent = Intent(context, SubmittedActivity::class.java)
        } else {
            goToQuestionnaireIntent = questions[0].answerType.createIntent(context)
            goToQuestionnaireIntent.putExtra(QUESTIONS, gson.toJson(questions))
            goToQuestionnaireIntent.putExtra(INDEX, 0)
            goToQuestionnaireIntent.putExtra(QUESTIONNAIRE_ID, questionnaireId)
        }

        val pendingIntent = PendingIntent.getActivity(context,
            questionnaireId, goToQuestionnaireIntent, 0)

        val notification: Notification = NotificationCompat.Builder(context, REMINDERS_CHANNEL_ID)
            .setContentTitle("Personal Stats")
            .setContentText("It's time to answer questionnaire " + questionnaireName)
            .setSmallIcon(R.drawable.ic_alarm)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager: NotificationManagerCompat = NotificationManagerCompat.from(context)
        manager.notify(questionnaireId, notification)

        val next: Calendar = Calendar.getInstance()
        next.timeInMillis = System.currentTimeMillis()
        next.set(Calendar.SECOND, 0)
        if (DAILY == frequency) {
            next.add(Calendar.DAY_OF_YEAR, 1)
        } else if (WEEKLY == frequency) {
            next.add(Calendar.WEEK_OF_YEAR, 1)
        }
        val intent: PendingIntent = createNotificationIntent(context, questionnaireId,
            questionnaireName!!, frequency!!)

        val alarmManager: AlarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(RTC_WAKEUP, next.timeInMillis, intent)
    }

    private fun createNotificationIntent(
        context: Context,
        questionnaireId: Int,
        questionnaireName: String,
        frequency: String
    ): PendingIntent {
        val reminderIntent = Intent(context, QuestionnaireReminderReceiver::class.java)
        reminderIntent.putExtra(QUESTIONNAIRE_ID, questionnaireId)
        reminderIntent.putExtra(QUESTIONNAIRE_NAME, questionnaireName)
        reminderIntent.putExtra(NOTIFICATION_FREQUENCY, frequency)
        return PendingIntent.getBroadcast(context, questionnaireId, reminderIntent, PendingIntent.FLAG_CANCEL_CURRENT)
    }


}