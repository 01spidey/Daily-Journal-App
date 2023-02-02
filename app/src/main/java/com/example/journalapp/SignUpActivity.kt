package com.example.journalapp

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.journalapp.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nestedScrollView = binding.nsview
        val behavior = BottomSheetBehavior.from(nestedScrollView)

        behavior.apply {
            peekHeight = 800
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        firebaseAuth = FirebaseAuth.getInstance()

        val signin = binding.signIn
        signin.paintFlags = signin.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        signin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.google.setOnClickListener{
            signInGoogle()
        }

        binding.signUp.setOnClickListener {
            val mail = findViewById<EditText>(R.id.mail).text.toString()
            val pass = findViewById<EditText>(R.id.pass).text.toString()
            val repass = findViewById<EditText>(R.id.repass).text.toString()

            if (mail.isNotEmpty() && pass.isNotEmpty() && repass.isNotEmpty()) {
                if (pass == repass) {
                    firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener {

                        if (it.isSuccessful) {
                            val intent = Intent(this, RegSuccessActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else Toast.makeText(this, "Invalid Credentials !!", Toast.LENGTH_SHORT).show()
                    }

                } else Toast.makeText(this, "Password Doesn't Match !!", Toast.LENGTH_SHORT).show()

            } else Toast.makeText(this, "Fields Cannot be Empty !!", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            if(result.resultCode== Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account:GoogleSignInAccount? = task.result
            if(account!=null) updateUI(account)
        }else Toast.makeText(this, "Invalid Credentials !!", Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful) startActivity(Intent(this, MainActivity::class.java))
            else Toast.makeText(this, "Invalid Credentials !!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

}