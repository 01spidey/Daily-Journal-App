package com.example.journalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.journalapp.databinding.ActivityViewJournalBinding

class ViewJournalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal)

        val binding = ActivityViewJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val day = intent.getStringExtra("day").toString()
        val month = intent.getStringExtra("month").toString()
        val year = intent.getStringExtra("year").toString()
        val content = binding.content.text.toString()
        val grateful = binding.gratefulness.text.toString()
//        Toast.makeText(this, grateful, Toast.LENGTH_SHORT).show()

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
            intent.putExtra("from", "ViewJournal")
            intent.putExtra("content",content)
            intent.putExtra("grateful", grateful)
            startActivity(intent)
        }
    }
}