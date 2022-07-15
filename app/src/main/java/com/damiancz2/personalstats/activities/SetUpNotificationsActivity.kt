package com.damiancz2.personalstats.activities

import android.app.AlarmManager
import android.app.PendingIntent
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
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.QUESTIONNAIRE_NAME
import com.damiancz2.personalstats.QuestionnaireReminderReceiver
import com.damiancz2.personalstats.R
import java.time.DayOfWeek
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
                    setDailyNotifications(questionnaireId, questionnaireName!!, dailyTimePicker.hour, dailyTimePicker.minute)
                }
                getString(R.string.weekly) -> {
                    setWeeklyNotifications(questionnaireId, questionnaireName!!,
                        getDayOfWeek(dayOfWeekSpinner), weeklyTimePicker.hour, weeklyTimePicker.minute)
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


    private fun setDailyNotifications(questionnaireId: Int, questionnaireName: String, hour: Int, minute:Int) {
        val pendingIntent: PendingIntent = createNotificationIntent(questionnaireId, questionnaireName)

        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_WEEK, 1)
        }

        val day: Long = 24 * 60 * 60 * 1000

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, day, pendingIntent)
    }

    private fun setWeeklyNotifications(questionnaireId: Int, questionnaireName: String,
                                       dayOfWeek: DayOfWeek, hour: Int, minute:Int) {
        val pendingIntent: PendingIntent = createNotificationIntent(questionnaireId, questionnaireName)

        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager


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

        val week: Long = 7 * 24 * 60 * 60 * 1000

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, week, pendingIntent)
    }

    private fun createNotificationIntent(
        questionnaireId: Int,
        questionnaireName: String,
    ): PendingIntent {
        val reminderIntent = Intent(this, QuestionnaireReminderReceiver::class.java)
        reminderIntent.putExtra(QUESTIONNAIRE_ID, questionnaireId)
        reminderIntent.putExtra(QUESTIONNAIRE_NAME, questionnaireName)
        return PendingIntent.getBroadcast(this, questionnaireId, reminderIntent, PendingIntent.FLAG_CANCEL_CURRENT)
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