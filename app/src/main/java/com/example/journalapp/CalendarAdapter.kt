package com.example.journalapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener,
    private val context : Context,
    private val month :String,
    private val year :String,
    private val journalDates : HashSet<String>
) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.calendar_cell,
            parent,
            false
        )
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.16666666).toInt()
        Toast.makeText(context, journalDates.toString(), Toast.LENGTH_SHORT).show()
        return ViewHolder(view, onItemListener)

    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = daysOfMonth[position]
        holder.dayOfMonth.text = day
        if(day!="") {
            val date = "$day-$month-$year"
            if (journalDates.contains(date)) {
                holder.dot.setBackgroundResource(R.drawable.circle_dot)
            }
        }
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String, dot:View)
    }
}

class ViewHolder(itemView: View, private val onItemListener: CalendarAdapter.OnItemListener) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDay)
    val dot:View = itemView.findViewById(R.id.dot)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        onItemListener.onItemClick(
            adapterPosition,
            dayOfMonth.text.toString(),
            dot
        )
    }


}

