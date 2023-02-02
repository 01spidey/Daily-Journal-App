package com.example.journalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.journalapp.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResetPasswordBinding
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.submit.setOnClickListener{
            val mail:String = ((binding.mail.text).toString()).trim{it<=' '}
            if(mail.isEmpty()){
                Toast.makeText(this, "Please Enter the Mail ID !!", Toast.LENGTH_SHORT).show()
            }
            else{
                firebaseAuth.sendPasswordResetEmail(mail)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(this, "Password Reset mail sent successfully!!", Toast.LENGTH_SHORT).show()
                            finish()
                        }else {
                            Toast.makeText(this, "Account not found!!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}