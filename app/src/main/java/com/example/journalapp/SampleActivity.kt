package com.example.journalapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.journalapp.databinding.ActivitySampleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class SampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        val binding = ActivitySampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val gender = binding.gender
        val write = binding.write
        val read = binding.read
        val db = FirebaseFirestore.getInstance()
        val doc_lst = ArrayList<QueryDocumentSnapshot>()

        write.setOnClickListener {
            val sampleRef = db.collection("Sample").document()
            val sample = HashMap<String, String>()
            val username_txt = username.text.toString()
            val gender_txt = if(gender.isEnabled) "Male" else "Female"

            sample["username"] = username_txt
            sample["gender"] = gender_txt

            sampleRef.set(sample)
                .addOnSuccessListener { Toast.makeText(this, "Writing Successful !!", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { Toast.makeText(this, "Error Saving the Journal !!", Toast.LENGTH_SHORT).show() }
        }

        read.setOnClickListener {

            val gender_txt = if(gender.isEnabled) "Male" else "Female"

            db.collection("Sample")
                .whereEqualTo("gender",gender_txt)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Reading Successful!!", Toast.LENGTH_SHORT).show()
                        val sb = java.lang.StringBuilder()
                        for(document in it.result){
//                            doc_lst.add(document)
                            sb.append("${document.get("username")}\n")
                        }
                        Log.d("result",sb.toString())
                        binding.res.text = sb.toString()
                    } else {
                        Toast.makeText(this, "Reading Failed!!", Toast.LENGTH_SHORT).show()
                    }
                }


        }

    }

}