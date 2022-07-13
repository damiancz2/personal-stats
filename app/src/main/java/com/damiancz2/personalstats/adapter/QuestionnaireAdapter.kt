package com.damiancz2.personalstats.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.damiancz2.personalstats.AdapterEntryPoint
import com.damiancz2.personalstats.INDEX
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.QUESTIONNAIRE_NAME
import com.damiancz2.personalstats.QUESTIONS
import com.damiancz2.personalstats.QuestionManager
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.activities.EditQuestionnaireActivity
import com.damiancz2.personalstats.activities.SubmittedActivity
import com.damiancz2.personalstats.activities.ViewQuestionsActivity
import com.damiancz2.personalstats.dialogs.DeleteQuestionnaireDialog
import com.damiancz2.personalstats.model.Question
import com.damiancz2.personalstats.model.Questionnaire
import com.google.gson.Gson
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class QuestionnaireAdapter(private var data: List<Questionnaire>,
                           private val fragmentManager: FragmentManager)
    : RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder>() {

    @Inject lateinit var questionManager: QuestionManager
    @Inject lateinit var gson: Gson

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val myEntryPoint = EntryPointAccessors.fromApplication(parent.context, AdapterEntryPoint::class.java)
        questionManager = myEntryPoint.getQuestionManager()
        gson = myEntryPoint.getGson()

        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.list_questionnaire, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val questionnaire: Questionnaire = data.get(position)
        holder.textView.text = questionnaire.name

        configureAnswerButton(holder, questionnaire)
        configureEditButton(holder, questionnaire)
        configureViewButton(holder, questionnaire)
        configureDeleteButton(holder, questionnaire)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setQuestionnaires(questionnaires: List<Questionnaire>) {
        data = questionnaires
        notifyDataSetChanged()
    }

    private fun configureDeleteButton(
        holder: ViewHolder,
        questionnaire: Questionnaire,
    ) {
        holder.deleteButton.setOnClickListener {
            DeleteQuestionnaireDialog(questionnaire, this).show(fragmentManager, null)
        }
    }

    private fun configureViewButton(
        holder: ViewHolder,
        questionnaire: Questionnaire,
    ) {
        holder.viewButton.setOnClickListener {
            val intent = Intent(holder.cardView.context, ViewQuestionsActivity::class.java)
            val extras = Bundle()
            extras.putInt(QUESTIONNAIRE_ID, questionnaire.id)
            intent.putExtras(extras)
            holder.cardView.context.startActivity(intent)
        }
    }

    private fun configureEditButton(
        holder: ViewHolder,
        questionnaire: Questionnaire,
    ) {
        holder.editButton.setOnClickListener {
            val intent =
                Intent(holder.cardView.context, EditQuestionnaireActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(QUESTIONNAIRE_ID, questionnaire.id)
            bundle.putString(QUESTIONNAIRE_NAME, questionnaire.name)
            intent.putExtras(bundle)
            holder.cardView.context.startActivity(intent)
        }
    }

    private fun configureAnswerButton(
        holder: ViewHolder,
        questionnaire: Questionnaire,
    ) {
        holder.answerButton.setOnClickListener {

            val questionList: ArrayList<Question> = questionManager.getQuestions(holder.cardView.context, questionnaire.id)

            val intent: Intent
            if (questionList.size == 0) {
                intent = Intent(holder.cardView.context, SubmittedActivity::class.java)
            } else {
                val bundle: Bundle = Bundle()
                bundle.putString(QUESTIONS, gson.toJson(questionList))
                bundle.putInt(INDEX, 0)
                bundle.putInt(QUESTIONNAIRE_ID, questionnaire.id)
                intent = questionList[0].answerType.createIntent(holder.cardView.context)
                intent.putExtras(bundle)
            }
            holder.cardView.context.startActivity(intent)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.SingleQuestionnaire)
        val editButton: ImageView = itemView.findViewById(R.id.imageViewEdit)
        val answerButton: ImageView = itemView.findViewById(R.id.imageViewAnswer)
        val viewButton: ImageView = itemView.findViewById(R.id.imageViewView)
        val deleteButton: ImageView = itemView.findViewById(R.id.imageViewDelete)
        val cardView: CardView = itemView.findViewById(R.id.cardViewQuestionnaire)

    }
}