package com.example.firebasepractice

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.firebasepractice.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Creating An Account")
        progressDialog.setCanceledOnTouchOutside(false)

        auth = FirebaseAuth.getInstance()

        binding.signUpBtn.setOnClickListener{
            validateData()
        }
    }

    private fun validateData() {
        var email = binding.emailEt.text.toString().trim()
        var password = binding.passwordEt.text.toString().trim()
        var confPass = binding.confpass.text.toString().trim()
        var username = binding.usernameEt.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.setError("Inavlid Email Format")
            binding.emailEt.requestFocus()
        }
        else if(username.isEmpty()){
            binding.usernameEt.error="Username required!!!"
            binding.usernameEt.requestFocus()
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
        progressDialog.show()
        auth.createUserWithEmailAndPassword(binding.emailEt.text.toString(),binding.passwordEt.text.toString()).addOnSuccessListener {
            progressDialog.dismiss()
            val db = FirebaseFirestore.getInstance()
            val user:MutableMap<String,Any> = HashMap()
            user["Name"]=binding.usernameEt.text.toString()
            user["Email"]=binding.emailEt.text.toString()
            user["user_UID"]= FirebaseAuth.getInstance().currentUser?.uid.toString()

            Firebase.auth.uid?.let { it1 ->
                db.collection("users").document(it1).set(user,
                    SetOptions.merge())
            }

            Toast.makeText(this,"Account created successfully ", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
            .addOnFailureListener{e->
                progressDialog.dismiss()

                Toast.makeText(this, "Sign Up FAILED DUE TO $e", Toast.LENGTH_SHORT).show()
            }
    }
}