package com.example.journalapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener,
    private val journalDates : ArrayList<String>
) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.calendar_cell,
            parent,
            false
        )
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.16666666).toInt()
        return ViewHolder(view, onItemListener)

    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = daysOfMonth[position]
        val set = HashSet<String>(journalDates)
        holder.dayOfMonth.text = day
        if(day!="") {
            if (day in set) {
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

