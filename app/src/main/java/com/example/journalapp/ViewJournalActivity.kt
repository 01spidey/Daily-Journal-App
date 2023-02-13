package com.example.journalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.journalapp.databinding.ActivityViewJournalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewJournalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal)

        val binding = ActivityViewJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val day = intent.getStringExtra("day").toString()
        val month = intent.getStringExtra("month").toString()
        val year = intent.getStringExtra("year").toString()
        val date = "$day-$month-$year"
        binding.day.text = day
        binding.monthYear.text = String.format(getString(R.string.month_year), month, year)


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

        binding.edit.setOnClickListener {
            val intent = Intent(this,WriteActivity::class.java)
            intent.putExtra("day", day)
            intent.putExtra("month", month)
            intent.putExtra("year", year)
            intent.putExtra("from", "ViewJournal")
            intent.putExtra("content",content_txt)
            intent.putExtra("grateful", grateful_txt)
            intent.putExtra("title", title_txt)
            startActivity(intent)
            finish()
        }
    }
}