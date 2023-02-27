package com.example.journalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import com.example.journalapp.databinding.ActivityViewJournalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class ViewJournalActivity : AppCompatActivity() {

    private var liked: Boolean = false;
    private lateinit var dayText: String
    private lateinit var monthText: String
    private lateinit var yearText: String
    private lateinit var liked_txt: String
    private lateinit var uid: String
    private lateinit var journalRef: DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal)

        val binding = ActivityViewJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dayText = intent.getStringExtra("day").toString()
        monthText = intent.getStringExtra("month").toString()
        yearText = intent.getStringExtra("year").toString()
        liked_txt = intent.getStringExtra("liked").toString()
        binding.day.text = dayText
        binding.monthYear.text = String.format(getString(R.string.month_year), monthText, yearText)


        val title = binding.title
        val content = binding.content
        val grateful = binding.gratefulness

        val title_txt = intent.getStringExtra("title")
        val content_txt = intent.getStringExtra("content")
        val grateful_txt = intent.getStringExtra("grateful")

        title.text = title_txt
        content.text = content_txt
        grateful.text = grateful_txt


        binding.back.setOnClickListener {
            finish()
        }

        liked = (liked_txt == "true")

        if (liked) binding.like.setBackgroundResource(R.drawable.heart_fill)

        uid = FirebaseAuth.getInstance().currentUser!!.uid
        journalRef = FirebaseFirestore.getInstance().collection("Journals")
            .document("$uid-$dayText-$monthText-$yearText")


        binding.like.setOnClickListener {
            if (!liked) {
                binding.like.setBackgroundResource(R.drawable.heart_fill)
                liked = true
                journalRef.update("liked", true)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Added to Liked Journals!!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { Log.d("ERROR", "Failed to write!!") }
            }
            else {
                binding.like.setBackgroundResource(R.drawable.heart_outline)
                liked = false
                journalRef.update("liked", false)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Removed from Liked Journals!!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { Log.d("ERROR", "Failed to write!!") }
            }
        }

        binding.edit.setOnClickListener {
            val intent = Intent(this, WriteActivity::class.java)
            intent.putExtra("day", dayText)
            intent.putExtra("month", monthText)
            intent.putExtra("year", yearText)
            intent.putExtra("from", "ViewJournal")
            intent.putExtra("content", content_txt)
            intent.putExtra("grateful", grateful_txt)
            intent.putExtra("title", title_txt)
            intent.putExtra("liked", liked.toString())
            startActivity(intent)
            finish()
        }
    }

}