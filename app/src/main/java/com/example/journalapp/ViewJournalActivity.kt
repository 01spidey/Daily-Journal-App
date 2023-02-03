package com.example.journalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.journalapp.databinding.ActivityViewJournalBinding

class ViewJournalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal)

        val binding = ActivityViewJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val day = intent.getStringExtra("day")
        val month = intent.getStringExtra("month")
        val year = intent.getStringExtra("year")

        binding.day.text = day
        binding.monthYear.text = String.format(getString(R.string.month_year), month, year)

        binding.back.setOnClickListener {
            finish()
        }
        binding.edit.setOnClickListener {
            val intent = Intent(this,WriteActivity::class.java)
            intent.putExtra("day", day)
            intent.putExtra("month", month)
            intent.putExtra("year", year)
            startActivity(intent)
        }
    }
}