package com.example.practice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.practice.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var auth: FirebaseAuth
    private var email = ""
    private  var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        checkUser()

        binding.loginBtn.setOnClickListener {
            validateData()
        }

        binding.noAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun login() {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(this, "LOGGED IN SUCCESSFULLY", Toast.LENGTH_SHORT).show()
            val firebaseUser = auth.currentUser
            val email = firebaseUser!!.email
            Toast.makeText(this,"Logged in as $email", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }.addOnFailureListener{e->
            Toast.makeText(this, "LOGGED IN FAILED DUE TO $e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if(firebaseUser!=null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun validateData() {
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.setError("Inavlid Email Format")
            binding.emailEt.requestFocus()
        }
        else if(binding.passwordEt.text.toString().isEmpty()){
            binding.passwordEt.error="Password can not be empty!"
            binding.passwordEt.requestFocus()
        }
        else{
            login()
        }
    }

}