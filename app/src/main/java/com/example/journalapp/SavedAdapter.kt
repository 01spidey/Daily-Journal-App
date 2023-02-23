package com.example.journalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class SavedAdapter(
    private val onItemListener: OnItemListener,
    private val likedJournals:ArrayList<Journal>
): RecyclerView.Adapter<SavedViewHolder>( ) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val context:Context = parent.context

        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_layout,
            parent,
            false
        )
        return SavedViewHolder(view, onItemListener)
    }

    override fun getItemCount(): Int {
        return likedJournals.size
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
        val journalEntry:Journal  = likedJournals[position]
        holder.title.text = journalEntry.title
        holder.date.text = "${journalEntry.day},${journalEntry.month},${journalEntry.year}"
        val entry = journalEntry.entry
        holder.content.text = entry.substring(0, min(entry.length, entry.length/3))+"..."
    }

    interface OnItemListener {
        fun onItemClick(position: Int)
    }

}

class SavedViewHolder(itemView: View, private val onItemListener: SavedAdapter.OnItemListener) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var title:TextView
    var date:TextView
    var content:TextView

    init {
        title = itemView.findViewById(R.id.title)
        date = itemView.findViewById(R.id.date)
        content = itemView.findViewById(R.id.content)
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        onItemListener.onItemClick(
            adapterPosition
        )
    }
}