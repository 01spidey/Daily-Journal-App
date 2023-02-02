package com.example.journalapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mail = intent.getStringExtra("email")
        val name = intent.getStringExtra("name")

        binding.logout.setOnClickListener{
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

        supportFragmentManager.beginTransaction().replace(R.id.main_container, HomeFragment()).commit()
        var fragment: Fragment? = null

        binding.write.setOnClickListener {
            startActivity(Intent(this, WriteActivity::class.java))
//            finish()
        }
        binding.info.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.main_container, InfoFragment()).commit()
        }

        val btmNav:BottomNavigationView = binding.btmNavView


        btmNav.background = null
        (btmNav.menu.getItem(2)).isEnabled = false

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
                }

                R.id.profile -> {
                    fragment = ProfileFragment()
                }
            }
            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment!!).commit()
                return@setOnItemSelectedListener true
            }
            return@setOnItemSelectedListener false
        }


    }
}