package com.example.journalapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.journalapp.databinding.ActivityWriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class WriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        val binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val grateful = binding.grateful
        val content = binding.content
        val save = binding.save
        val title = binding.title

        val day: String
        val month: String
        val year: String
        var saved = false
        var count = 0

        val from: String = intent.getStringExtra("from").toString()

        if (from == "null") {
            Toast.makeText(this, "From Home Fragment !!", Toast.LENGTH_SHORT).show()
            val date = LocalDate.now()
            day = date.dayOfMonth.toString()
            month = date.month.toString().lowercase()
            year = date.year.toString()
        } else {
            val content_txt = intent.getStringExtra("content").toString()
            val grateful_txt = intent.getStringExtra("grateful").toString()
            val title_txt = intent.getStringExtra("title").toString()

            day = intent.getStringExtra("day").toString()
            month = intent.getStringExtra("month").toString()
            year = intent.getStringExtra("year").toString()

            content.textSize = 17f
            content.setText(content_txt)
            grateful.setText(grateful_txt)
            title.setText(title_txt)
        }

        binding.day.text = day
        binding.monthYear.text = String.format(getString(R.string.month_year), month, year)


        content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (from == "null") {
                    if (s?.isNotEmpty() == true) content.textSize = 17f
                    else content.textSize = 40f
                } else content.textSize = 17f
            }
        })

        save.setOnClickListener {
            if (binding.grateful.text.toString().isEmpty() || binding.content.text.toString().isEmpty()) Toast.makeText(
                this,
                "Complete the Journal Bruh !!",
                Toast.LENGTH_SHORT
            ).show()
            else {
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val journal = HashMap<String, String>()
                val date = "$day-$month-$year"
                journal["title"] = binding.title.text.toString()
                journal["date"] = date
                journal["entry"] = content.text.toString()
                journal["grateful"] = binding.grateful.text.toString()
                journal["userID"] = userId

                Toast.makeText(this, journal["title"], Toast.LENGTH_SHORT).show()
                Toast.makeText(this, journal["date"], Toast.LENGTH_SHORT).show()
                Toast.makeText(this, journal["entry"], Toast.LENGTH_SHORT).show()
                val journalRef =  FirebaseFirestore.getInstance().collection("Journals").document("$userId-$date")
                journalRef.set(journal)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Journal is Saving !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Error Saving the Journal !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                saved = true
            }
        }

        binding.back.setOnClickListener {
            count++
            if (saved || count == 2) finish()
            else Toast.makeText(
                this,
                "Changes are not saved!! Press again to discard changes !!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}