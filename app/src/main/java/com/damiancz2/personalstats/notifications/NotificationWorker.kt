package com.damiancz2.personalstats.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.damiancz2.personalstats.INDEX
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.QUESTIONS
import com.damiancz2.personalstats.QuestionManager
import com.damiancz2.personalstats.QuestionnaireManager
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.REMINDERS_CHANNEL_ID
import com.damiancz2.personalstats.activities.SubmittedActivity
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParams: WorkerParameters,
    val gson: Gson,
    val questionManager: QuestionManager,
    val questionnaireManager: QuestionnaireManager)
    : Worker(context, workerParams) {

    override fun doWork(): Result {
        val questionnaireId = inputData.getInt(QUESTIONNAIRE_ID, 0)

        val questions = questionManager.getQuestions(context, questionnaireId)

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

        val questionnaires = questionnaireManager.getQuestionnaires(context)
        val questionnaire = questionnaires.filter { q -> q.id == questionnaireId }[0]
        val questionnaireName = questionnaire.name

        val notification: Notification = NotificationCompat.Builder(context, REMINDERS_CHANNEL_ID)
            .setContentTitle("Answer questionnaire $questionnaireName")
            .setContentText("It's time to answer questionnaire $questionnaireName")
            .setSmallIcon(R.drawable.ic_alarm)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager: NotificationManagerCompat = NotificationManagerCompat.from(context)
        manager.notify(questionnaireId, notification)

        return Result.success()
    }
}