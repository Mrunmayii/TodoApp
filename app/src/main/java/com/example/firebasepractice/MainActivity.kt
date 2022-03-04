package com.example.firebasepractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.firebasepractice.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
            var display : TextView = findViewById(R.id.nameTv)
            var id = FirebaseAuth.getInstance().currentUser?.uid.toString()

            FirebaseFirestore.getInstance().collection("users").get()
                .addOnCompleteListener() {
                    val result : StringBuffer = StringBuffer()
                    if(it.isSuccessful){
                        for(document in it.result){
                            var it_id = document.get("user_UID")

                            if(it_id.toString()==id) {
                                result.append("Hi, ").append(document.data.getValue("Name"))
                            }
                        }
                        display.setText(result)
                    }
            }
        }
        else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}