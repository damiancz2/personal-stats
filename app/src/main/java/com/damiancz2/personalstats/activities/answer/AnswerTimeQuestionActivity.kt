package com.damiancz2.personalstats.activities.answer

import android.widget.TimePicker
import com.damiancz2.personalstats.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AnswerTimeQuestionActivity : AbstractAnswerQuestionActivity<TimePicker>() {

    override fun prepare() {
        val timePicker: TimePicker = findViewById(R.id.TimePicker)
        timePicker.setIs24HourView(true)
    }

    override fun getLayout(): Int {
        return R.layout.activity_answer_time_question
    }

    override fun getInputViewId() : Int {
        return R.id.TimePicker
    }

    override fun getValue(inputView: TimePicker): String {
        val reportedTime: LocalDateTime = LocalDateTime.now()
            .withHour(inputView.hour)
            .withMinute(inputView.minute)
            .withSecond(0)
            .withNano(0)
        return reportedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}