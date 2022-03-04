package com.example.practice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.practice.databinding.ActivityHome2Binding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHome2Binding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        checkUser()

        binding.logoutBtn.setOnClickListener{
            auth.signOut()
            checkUser()
        }
    }


    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if(firebaseUser!=null){
//            val name = firebaseUser.displayName
            val email = firebaseUser.email
            binding.emailTv.text=email
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}