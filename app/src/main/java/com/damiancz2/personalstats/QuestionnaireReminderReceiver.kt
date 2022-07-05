package com.damiancz2.personalstats

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.damiancz2.personalstats.activities.SubmittedActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class QuestionnaireReminderReceiver: BroadcastReceiver() {

    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    override fun onReceive(context: Context?, intent: Intent?) {
        val questionnaireName = intent!!.getStringExtra(QUESTIONNAIRE_NAME)
        val questionnaireId = intent.extras!!.getInt(QUESTIONNAIRE_ID)
        val questions = getQuestions(context!!, questionnaireId)

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
    }

}