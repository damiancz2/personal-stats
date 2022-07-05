package com.damiancz2.personalstats.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.damiancz2.personalstats.QUESTIONNAIRE_ID
import com.damiancz2.personalstats.QUESTION_ID
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.activities.ViewAnswersActivity
import com.damiancz2.personalstats.model.Question


class QuestionAdapter(private val data: List<Question>,
                      private val questionnaireId: Int)
    : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.list_question, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question: Question = data[position]
        holder.textView.text = question.question
        holder.relativeLayout.setOnClickListener {
            val intent = Intent(holder.relativeLayout.context, ViewAnswersActivity::class.java)
            val bundle = Bundle()
            bundle.putString(QUESTION_ID, question.id)
            bundle.putInt(QUESTIONNAIRE_ID, questionnaireId)
            intent.putExtras(bundle)
            holder.relativeLayout.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.SingleQuestion)
        val relativeLayout: RelativeLayout = itemView.findViewById(R.id.relativeLayoutQuestion)
    }
}