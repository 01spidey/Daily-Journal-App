package com.example.journalapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        val day: String = intent.getStringExtra("day").toString()
        val month: String = intent.getStringExtra("month").toString()
        val year: String = intent.getStringExtra("year").toString()
        binding.day.text = day
        binding.monthYear.text = String.format(getString(R.string.month_year), month, year)


        var saved: Boolean = false
        var count = 0

        val from: String = intent.getStringExtra("from").toString()

        val grateful = binding.grateful
        val content = binding.content
        val save = binding.save
        val title = binding.title

        val title_txt = intent.getStringExtra("title")
        val content_txt = intent.getStringExtra("content")
        val grateful_txt = intent.getStringExtra("grateful")

        if(content_txt?.isNotEmpty() == true){
            content.textSize = 17f
        }

        title.setText(title_txt)
        content.setText(content_txt)
        grateful.setText(grateful_txt)


        content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (from == "Main") {
                    if (s?.isNotEmpty() == true) content.textSize = 17f
                    else content.textSize = 40f
//                }
            }
        })


        save.setOnClickListener {
            if (binding.grateful.text.toString().isEmpty()) Toast.makeText(this, "Complete the Gratefulness Section !!", Toast.LENGTH_SHORT).show()

            else {
                Toast.makeText(this, "Journal is Saving !!", Toast.LENGTH_SHORT).show()
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val date = "$day-$month-$year"
                val journalRef = FirebaseFirestore.getInstance().collection("Journals").document("$userId-$date")
                val journal = HashMap<String, String>()
                journal["title"] = binding.title.text.toString()
                journal["date"] = date
                journal["entry"] = binding.content.text.toString()
                journal["grateful"] = binding.grateful.text.toString()
                journal["userID"] = userId

                journalRef.set(journal)
                    .addOnSuccessListener { Toast.makeText(this, "Journal is Saving !!", Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { Toast.makeText(this, "Error Saving the Journal !!", Toast.LENGTH_SHORT).show() }

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