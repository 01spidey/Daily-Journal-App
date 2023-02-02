package com.example.journalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(lst:ArrayList<String>,context:Context ): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val data_lst = lst
    private val data_context = context

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.custom_design,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:RecyclerAdapter.ViewHolder, position:Int) {
        holder.txt.text = data_lst.get(position)
        holder.txt.setOnClickListener {
            Toast.makeText(data_context, "${holder.txt.text} Clicked!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return data_lst.size;
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt : TextView = itemView.findViewById(R.id.txt)
    }
}
