package com.example.journalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AlertDialog
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
    private lateinit var journalRef:DocumentReference

    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser!!.uid


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

        val doc_id = "$uid-$dayText-$monthText-$yearText"
        journalRef = db.collection("Journals").document(doc_id)

        val title = binding.title
        val content = binding.content
        val grateful = binding.gratefulness

        val title_txt = intent.getStringExtra("title")
        val content_txt = intent.getStringExtra("content")
        val grateful_txt = intent.getStringExtra("grateful")
//        val doc_id = intent.getStringExtra("doc_id")

        title.text = title_txt
        content.text = content_txt
        grateful.text = grateful_txt


        binding.back.setOnClickListener {
            finish()
        }

        liked = (liked_txt == "true")

        if (liked) binding.like.setBackgroundResource(R.drawable.heart_fill)

        binding.like.setOnClickListener {
            if (!liked) {
                binding.like.setBackgroundResource(R.drawable.heart_fill)
                liked = true
                journalRef.update("liked", true)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Added to Liked Journals!!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { Log.d("ERROR", "Failed to write!!") }
            } else {
                binding.like.setBackgroundResource(R.drawable.heart_outline)
                liked = false
                journalRef.update("liked", false)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Removed from Liked Journals!!", Toast.LENGTH_SHORT)
                            .show()
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

        binding.delete.setOnClickListener {
            showAlertDialog()
        }

        binding.emotion.setOnClickListener{
            val intent:Intent = Intent(this, ReflectionActivity::class.java)
            intent.putExtra("uid", uid)
            intent.putExtra("day", dayText)
            intent.putExtra("month", monthText)
            intent.putExtra("year", yearText)
            startActivity(intent)
        }
    }

    private fun showAlertDialog(){
        val view:View = layoutInflater.inflate(R.layout.delete_dialog_layout, null)
        val builder:AlertDialog.Builder = AlertDialog.Builder(this,R.style.dialog)
        builder.setView(view).create()
        val dialog:AlertDialog = builder.show()



        view.findViewById<TextView>(R.id.yes).setOnClickListener {
            journalRef.delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Journal Successfully Deleted !!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Log.d("ERROR","Error Vro !!");
                }
        }

        view.findViewById<TextView>(R.id.no).setOnClickListener {
            dialog.cancel()
        }
    }

}