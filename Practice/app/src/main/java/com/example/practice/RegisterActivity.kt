package com.example.practice

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth

    private var email = ""
    private  var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.signUpBtn.setOnClickListener {
            validateData()
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
        else if(binding.passwordEt.text.toString().length<6){
            binding.passwordEt.error="Password length should be at least 6!"
            binding.passwordEt.requestFocus()
        }
        else if(binding.confpass.text.toString()!=binding.passwordEt.text.toString()){
            binding.confpass.error="Password mismatched!"
            binding.confpass.requestFocus()
        }
        else{
            firebaseSignUp()
        }

    }

    private fun firebaseSignUp() {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val firebaseUser = auth.currentUser
            val email = firebaseUser!!.email
            Toast.makeText(this,"Account creataed sucessfully ", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
            .addOnFailureListener{e->
            Toast.makeText(this, "Sign Up FAILED DUE TO $e", Toast.LENGTH_SHORT).show()
        }
    }

}