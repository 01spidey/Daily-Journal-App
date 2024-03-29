package com.example.journalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmotionAdapter(
    var emotion_lst:ArrayList<String>,
    val context:Context
) : RecyclerView.Adapter<EmotionAdapter.EmotionViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmotionViewHolder {
        val emotionView = LayoutInflater.from(parent.context).inflate(
            R.layout.emotion_item,
            parent,
            false
        )
        return EmotionViewHolder(emotionView)
    }

    override fun getItemCount(): Int {
        return emotion_lst.size 
    }

    override fun onBindViewHolder(holder: EmotionViewHolder, position: Int) {
        val arr = emotion_lst[position].split('-')
        val emotion:String = arr[0]
        val percent:String = arr[1]
        holder.emotion_txt.text = emotion
        holder.emotion_percent.text = percent
        var temp = percent.substring(0,percent.length-1).toInt()

        val layoutParams = holder.emotion_bar.layoutParams
        val pixels = ((temp*2) * context.resources.displayMetrics.density).toInt()
        layoutParams.width = pixels
        holder.emotion_bar.layoutParams = layoutParams
    }

    class EmotionViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val emotion_txt = itemView.findViewById<TextView>(R.id.emotion_txt)
        val emotion_percent = itemView.findViewById<TextView>(R.id.emotion_percent)
        var emotion_bar = itemView.findViewById<View>(R.id.emotion_bar)
    }
}