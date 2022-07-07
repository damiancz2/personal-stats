package com.damiancz2.personalstats.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.damiancz2.personalstats.R
import com.damiancz2.personalstats.model.Answer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AnswerAdapter (private val data: List<Answer>)
    : RecyclerView.Adapter<AnswerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.list_answer, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myListData: Answer = data.get(position)
        holder.textView.text = myListData.answer
        holder.timestamp.text = LocalDateTime.parse(myListData.timestamp)
            .format(DateTimeFormatter.ISO_DATE)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timestamp: TextView = itemView.findViewById(R.id.AnswerDate)
        val textView: TextView = itemView.findViewById(R.id.SingleAnswer)
    }
}