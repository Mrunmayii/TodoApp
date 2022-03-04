package com.example.firebasepractice

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.firebasepractice.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore
    private lateinit var db: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        checkuser()

        binding.noAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        var name = binding.emailEt.text.toString().trim()
        var pass = binding.passwordEt.text.toString().trim()
        if (name.isEmpty()) {
            binding.emailEt.error = "Email Required!!!"
            binding.emailEt.requestFocus()
        } else if (pass.isEmpty()) {
            binding.passwordEt.error = "Password Required!!!"
            binding.passwordEt.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(name).matches()) {
            binding.emailEt.error = "Invalid Email Format"
            binding.emailEt.requestFocus()

        } else {
            firebaselogin()
        }
    }

    private fun firebaselogin() {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(
            binding.emailEt.text.toString(),
            binding.passwordEt.text.toString()
        )
            .addOnSuccessListener {
                progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                val intent = (Intent(this@LoginActivity, MainActivity::class.java))
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Log In Failed Due To${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun checkuser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {

            val intent = (Intent(this@LoginActivity, MainActivity::class.java))
            startActivity(intent)
        }
    }
}