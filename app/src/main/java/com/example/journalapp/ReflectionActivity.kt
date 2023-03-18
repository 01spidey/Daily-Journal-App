package com.example.journalapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.databinding.ActivityReflectionBinding
import com.google.firebase.firestore.FirebaseFirestore

class ReflectionActivity : AppCompatActivity() {

    private lateinit var recyclerView:RecyclerView
    private lateinit var db:FirebaseFirestore
    private lateinit var binding:ActivityReflectionBinding
    private lateinit var emotionAdapter:EmotionAdapter
    private lateinit var emotion_lst:ArrayList<String>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReflectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.emotionRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(this)

        db = FirebaseFirestore.getInstance()

        val day = intent.getStringExtra("day")
        val month = intent.getStringExtra("month")
        val year = intent.getStringExtra("year")
        val uid = intent.getStringExtra("uid")

        val doc_id = "$uid-$day-$month-$year"

        emotion_lst = ArrayList();

        Log.d("ERROR","In ReflectionActivity!!")

        db.collection("Journals")
            .document(doc_id)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result
                    val emotion_map:Map<String,String> = document.get("emotions") as HashMap<String,String>
                    Log.d("Emotions",emotion_map.toString())
                    val keys = emotion_map.keys
                    for(key in keys) emotion_lst.add("$key-${emotion_map[key]}")

                    emotionAdapter = EmotionAdapter(emotion_lst,this)

                    recyclerView.adapter = emotionAdapter

                } else Log.e("Error fetching document", "Document Varla vro!!")

            }
    }
}