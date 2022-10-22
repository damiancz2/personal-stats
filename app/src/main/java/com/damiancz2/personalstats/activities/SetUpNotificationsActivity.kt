package com.damiancz2.personalstats.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.QUESTIONNAIRE_NAME
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.getQuestionnaireTag
import com.damiancz2.personalstats.notifications.NotificationWorker
import java.time.DayOfWeek
import java.time.Duration
import java.util.Calendar

class SetUpNotificationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up_notifications)

        val questionnaireId = intent.extras!!.getInt(QUESTIONNAIRE_ID)
        val questionnaireName = intent.getStringExtra(QUESTIONNAIRE_NAME)
        val questionnaireNameTextBox : TextView = findViewById(R.id.setUpNotificationsQuestionnaireName)
        questionnaireNameTextBox.text = questionnaireName

        val weeklyTimePicker: TimePicker = findViewById(R.id.weeklyNotificationTimePicker)
        weeklyTimePicker.setIs24HourView(true)
        val dailyTimePicker: TimePicker = findViewById(R.id.dailyNotificationTimePicker)
        dailyTimePicker.setIs24HourView(true)
        val dayOfWeekSpinner: Spinner = findViewById(R.id.dayOfWeekSpinner)

        val notificationFrequencySpinner: Spinner = findViewById(R.id.notificationFrequencySpinner)

        val daysOfWeek = ArrayList<String>()
        daysOfWeek.add(getString(R.string.monday))
        daysOfWeek.add(getString(R.string.tuesday))
        daysOfWeek.add(getString(R.string.wednesday))
        daysOfWeek.add(getString(R.string.thursday))
        daysOfWeek.add(getString(R.string.friday))
        daysOfWeek.add(getString(R.string.saturday))
        daysOfWeek.add(getString(R.string.sunday))

        val dayOfWeekAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            daysOfWeek
        )

        dayOfWeekSpinner.adapter = dayOfWeekAdapter

        val notificationFrequencies = ArrayList<String>()
        notificationFrequencies.add(getString(R.string.daily))
        notificationFrequencies.add(getString(R.string.weekly))

        val notificationFrequenciesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            notificationFrequencies
        )

        notificationFrequencySpinner.adapter = notificationFrequenciesAdapter

        notificationFrequencySpinner.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                dailyTimePicker.visibility = View.GONE
                weeklyTimePicker.visibility = View.GONE
                dayOfWeekSpinner.visibility = View.GONE
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (notificationFrequencySpinner.selectedItem.toString()) {
                    getString(R.string.daily) -> {
                        dailyTimePicker.visibility = View.VISIBLE
                        weeklyTimePicker.visibility = View.GONE
                        dayOfWeekSpinner.visibility = View.GONE
                    }
                    getString(R.string.weekly) -> {
                        dailyTimePicker.visibility = View.GONE
                        weeklyTimePicker.visibility = View.VISIBLE
                        dayOfWeekSpinner.visibility = View.VISIBLE
                    }
                    else -> {
                        dailyTimePicker.visibility = View.GONE
                        weeklyTimePicker.visibility = View.GONE
                        dayOfWeekSpinner.visibility = View.GONE
                    }
                }
            }
        }

        val submitButton: Button = findViewById(R.id.setUpNotificationsSubmitButton)
        submitButton.setOnClickListener{
            when (notificationFrequencySpinner.selectedItem.toString()) {
                getString(R.string.daily) -> {
                    setDailyNotifications(questionnaireId,
                        dailyTimePicker.hour, dailyTimePicker.minute)
                }
                getString(R.string.weekly) -> {
                    setWeeklyNotifications(questionnaireId, getDayOfWeek(dayOfWeekSpinner),
                        weeklyTimePicker.hour, weeklyTimePicker.minute)
                }
                else -> { }
            }

            val intent = Intent(this, EditQuestionnaireActivity::class.java)
            intent.putExtra(QUESTIONNAIRE_NAME, questionnaireName)
            intent.putExtra(QUESTIONNAIRE_ID, questionnaireId)
            startActivity(intent)
        }

        dailyTimePicker.visibility = View.VISIBLE
    }

    private fun getDayOfWeek(dayOfWeekSpinner: Spinner): DayOfWeek {
        return when (dayOfWeekSpinner.selectedItem.toString()) {
            getString(R.string.monday) -> DayOfWeek.MONDAY
            getString(R.string.tuesday) -> DayOfWeek.TUESDAY
            getString(R.string.wednesday) -> DayOfWeek.WEDNESDAY
            getString(R.string.thursday) -> DayOfWeek.THURSDAY
            getString(R.string.friday) -> DayOfWeek.FRIDAY
            getString(R.string.saturday) -> DayOfWeek.SATURDAY
            getString(R.string.sunday) -> DayOfWeek.SUNDAY
            else -> throw IllegalArgumentException("Invalid day of the week: "
                    + dayOfWeekSpinner.selectedItem.toString())
        }
    }


    private fun setDailyNotifications(questionnaireId: Int, hour: Int, minute:Int) {
        WorkManager.getInstance(this).cancelAllWorkByTag(getQuestionnaireTag(questionnaireId))

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_WEEK, 1)
        }

        val nextTime = calendar.timeInMillis
        val now = System.currentTimeMillis()

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(Duration.ofDays(1))
            .setInputData(workDataOf(QUESTIONNAIRE_ID to questionnaireId))
            .setInitialDelay(Duration.ofMillis(nextTime - now))
            .addTag(getQuestionnaireTag(questionnaireId))
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
      }

    private fun setWeeklyNotifications(questionnaireId: Int,
                                       dayOfWeek: DayOfWeek, hour: Int, minute:Int) {
        WorkManager.getInstance(this).cancelAllWorkByTag(getQuestionnaireTag(questionnaireId))

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        while(calendar.get(Calendar.DAY_OF_WEEK) != convertDayOfWeek(dayOfWeek)) {
            calendar.add(Calendar.DAY_OF_WEEK, 1)
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }

        val nextTime = calendar.timeInMillis
        val now = System.currentTimeMillis()

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(Duration.ofDays(7))
                    .setInputData(workDataOf(QUESTIONNAIRE_ID to questionnaireId))
            .setInitialDelay(Duration.ofMillis(nextTime - now))
            .addTag(getQuestionnaireTag(questionnaireId))
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun convertDayOfWeek(dayOfWeek: DayOfWeek): Int {
        return when(dayOfWeek) {
            DayOfWeek.MONDAY -> Calendar.MONDAY
            DayOfWeek.TUESDAY -> Calendar.TUESDAY
            DayOfWeek.WEDNESDAY -> Calendar.WEDNESDAY
            DayOfWeek.THURSDAY -> Calendar.THURSDAY
            DayOfWeek.FRIDAY -> Calendar.FRIDAY
            DayOfWeek.SATURDAY -> Calendar.SATURDAY
            DayOfWeek.SUNDAY -> Calendar.SUNDAY
        }
    }
}