package com.example.journalapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresPermission.Write
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.journalapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.lang.ref.Reference
import java.time.LocalDate
import java.time.LocalTime
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private var backCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mail = intent.getStringExtra("email")
        val name = intent.getStringExtra("name")

        binding.logout.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val client = GoogleSignIn.getClient(this, gso)

            auth = FirebaseAuth.getInstance()

            val user = auth.currentUser

            if (user != null) {
                auth.signOut()
                client.revokeAccess()
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        }


        supportFragmentManager.beginTransaction().replace(R.id.main_container, HomeFragment())
            .commit()
        var fragment: Fragment? = null
        var reference: Query? = null
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        binding.write.setOnClickListener {
            val date: LocalDate = LocalDate.now()
            val day = date.dayOfMonth.toString()
            val month =
                "${date.month.toString()[0]}${(date.month.toString().substring(1)).lowercase()}"
            val year = date.year.toString()

            reference = db.collection("Journals")
                .whereEqualTo("day", day)
                .whereEqualTo("month", month)
                .whereEqualTo("year", year)
                .whereEqualTo("userID", uid)

            reference!!.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    var title_txt = ""
                    var content_txt = ""
                    var grateful_txt = ""
                    for (document in it.result) {
                        Log.d("document", document.toString())
                        title_txt = document.get("title").toString()
                        content_txt = document.get("entry").toString()
                        grateful_txt = document.get("grateful").toString()
                    }

                    val intent = Intent(this, WriteActivity::class.java)

                    intent.putExtra("month", month)
                    intent.putExtra("year", year)
                    intent.putExtra("day", day)
                    intent.putExtra("title", title_txt)
                    intent.putExtra("content", content_txt)
                    intent.putExtra("grateful", grateful_txt)
                    intent.putExtra("from", "Main")
                    startActivity(intent)

                } else {
                    Log.e("Error fetching document", "Document Varla vro!!")
                }
            }
        }
        binding.info.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.main_container, InfoFragment())
                .commit()
        }

        val btmNav: BottomNavigationView = binding.btmNavView


        btmNav.background = null
        (btmNav.menu.getItem(2)).isEnabled = false
        var flag = false
        btmNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    fragment = HomeFragment()
                }
                R.id.read -> {
                    fragment = ReadFragment()
                }

                R.id.bookmark -> {
                    fragment = SavedFragment()
                    flag = true
                }

                R.id.chat -> {
                    // Navigate to another activity
                    val intent = Intent(this, ChatActivity::class.java)
                    startActivity(intent)
                    true
                }


            }

            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, fragment!!).commit()
                return@setOnItemSelectedListener true
            }
            return@setOnItemSelectedListener false
        }


    }

    override fun onBackPressed() {
        backCount++
        if (backCount == 2) finishAffinity() // Terminate the whole application
        else {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ backCount = 0 }, 2000)
        }
    }
}