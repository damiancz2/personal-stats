package com.damiancz2.personalstats.activities.answer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.damiancz2.personalstats.AnswerManager
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.activities.SubmittedActivity
import com.damiancz2.personalstats.model.Answer
import com.damiancz2.personalstats.model.AnswerType
import com.damiancz2.personalstats.model.Question
import com.damiancz2.personalstats.saveQuestions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

abstract class AbstractAnswerQuestionActivity<V>: AppCompatActivity() {

    @Inject
    lateinit var answerManager : AnswerManager

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())

        val questions: ArrayList<Question>
        val index: Int
        val bundle: Bundle

        if (intent.extras != null) {
            bundle = intent.extras!!
            val questionsString = bundle.getString(QUESTIONS)
            val itemType = object : TypeToken<ArrayList<Question>>() {}.type
            questions = gson.fromJson(questionsString, itemType)
            index = bundle.getInt(INDEX)
        } else {
            index = 0
            questions = ArrayList()
            questions.add(Question("dummy id", "No questions", AnswerType.TEXT))
            bundle = Bundle()
            bundle.putInt(INDEX, index)
            bundle.putString(QUESTIONS, gson.toJson(questions))
        }

        val textView: TextView = findViewById(R.id.QuestionTextView)
        textView.text = questions[index].question

        prepare()

        configureBackButton(bundle, questions, index)
        configureSubmitButton(bundle, questions, index)
        configureSkipButton(bundle, questions, index)
        configureDeleteButton(bundle, questions, index)
    }

    private fun save(questionId: String) {
        val bundle = intent.extras!!
        val questionnaireId = bundle.getInt(QUESTIONNAIRE_ID)

        val inputView: V = findViewById(getInputViewId())
        val answers: ArrayList<Answer> = answerManager.getAnswers(this, questionnaireId!!, questionId)

        val now : LocalDateTime = LocalDateTime.now()

        val answerId: String = UUID.randomUUID().toString();

        val answer = Answer(
            questionId = questionId,
            answer = getValue(inputView),
            timestamp = now.format(DateTimeFormatter.ISO_DATE_TIME),
            id = answerId
        )
        answers.add(answer)

        answerManager.saveAnswers(this, questionnaireId, questionId, answers)
    }

    private fun goToQuestionAt(bundle: Bundle, index: Int, question: Question) {
        val intent = question.answerType.createIntent(this)
        bundle.putInt(INDEX, index)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun configureSkipButton(bundle: Bundle, questions: ArrayList<Question>, index: Int) {
        val isLast: Boolean = index >= questions.size - 1
        val skipButton: Button = findViewById(R.id.SkipButton)
        skipButton.setOnClickListener{
            if (isLast) {
                val intent = Intent(this, SubmittedActivity::class.java)
                startActivity(intent)
            } else {
                goToQuestionAt(bundle, index + 1, questions[index + 1])
            }
        }
    }

    private fun configureSubmitButton(bundle: Bundle, questions: ArrayList<Question>, index: Int) {
        val isLast: Boolean = index >= questions.size - 1
        val submitButton: Button = findViewById(R.id.SubmitButton)
        submitButton.setOnClickListener{
            save(questions[index].id)
            if (isLast) {
                val intent = Intent(this, SubmittedActivity::class.java)
                startActivity(intent)
            } else {
                goToQuestionAt(bundle, index + 1, questions[index + 1])
            }
        }
    }

    private fun configureDeleteButton(bundle: Bundle, questions: ArrayList<Question>, index: Int) {
        val isLast: Boolean = index >= questions.size - 1
        val deleteButton: Button = findViewById(R.id.DeleteButton)
        val questionnaireId: Int = bundle.getInt(QUESTIONNAIRE_ID)
        deleteButton.setOnClickListener{
            questions.removeAt(index)
            saveQuestions(this, questionnaireId, questions)
            val jsonQuestions : String = gson.toJson(questions)
            bundle.putString(QUESTIONS, jsonQuestions)
            if (isLast) {
                startActivity(Intent(this, SubmittedActivity::class.java))
            } else {
                goToQuestionAt(bundle, index, questions[index])
            }
        }
    }

    private fun configureBackButton(bundle: Bundle, questions: ArrayList<Question>, index: Int) {
        val backButton: Button = findViewById(R.id.PreviousQuestionButton)
        if (index == 0) {
            backButton.visibility = View.INVISIBLE
        } else {
            backButton.setOnClickListener{
                goToQuestionAt(bundle, index - 1, questions[index - 1])
            }
        }
    }

    abstract fun prepare()

    abstract fun getLayout() : Int

    abstract fun getInputViewId() : Int

    abstract fun getValue(inputView : V) : String

    companion object {
        const val QUESTIONS = "questions"
        const val INDEX = "index"
    }
}