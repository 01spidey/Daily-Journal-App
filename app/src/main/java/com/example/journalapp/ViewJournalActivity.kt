package com.example.journalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val date = "$day-$month-$year"
        val journalRef = FirebaseFirestore.getInstance().collection("Journals")

        var title_txt:String? = "Title Here"
        var entry_txt:String? = "Reflect On Yourself"
        var grateful_txt:String? = "I'm Grateful for..."


        val query = journalRef.whereEqualTo("userID", userId)
            .whereEqualTo("date", date)
        query.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    title_txt = document.getString("title")
                    entry_txt = document.getString("entry")
                    grateful_txt = document.getString("grateful")
                    binding.title.text = title_txt
                    binding.content.text = entry_txt
                    binding.gratefulness.text = grateful_txt

                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Fetching Failed !!", Toast.LENGTH_SHORT).show()
            }



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
            intent.putExtra("content",entry_txt)
            intent.putExtra("grateful", grateful_txt)
            intent.putExtra("title", title_txt.toString())
            startActivity(intent)
        }
    }
}