package com.example.journalapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.databinding.ActivityReflectionBinding
import com.google.firebase.firestore.FirebaseFirestore

class ReflectionActivity : AppCompatActivity() {
    private lateinit var recyclerView:RecyclerView
    private lateinit var db:FirebaseFirestore
    private lateinit var binding:ActivityReflectionBinding
    private lateinit var emotion_lst:ArrayList<String>
    private lateinit var emotionAdapter:EmotionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReflectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.emotionRecyclerView
        emotion_lst = ArrayList()

        recyclerView.layoutManager = LinearLayoutManager(this)
        db = FirebaseFirestore.getInstance()

        emotion_lst.add("Sad-50%")
        emotion_lst.add("Happy-10%")
        emotion_lst.add("Fear-20%")
        emotion_lst.add("Anger-20%")

        emotionAdapter = EmotionAdapter(emotion_lst)

        recyclerView.adapter = emotionAdapter
        val linearLayoutManager:LinearLayoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = linearLayoutManager

        get_emotions()
    }

    private fun get_emotions(){

    }
}