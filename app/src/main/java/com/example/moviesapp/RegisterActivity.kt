package com.example.moviesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        // get variables
        var email_input = findViewById<TextView>(R.id.register_email)
        var pass_input = findViewById<TextView>(R.id.register_pass)
        var confirm_input = findViewById<TextView>(R.id.register_confirm)
        var register = findViewById<Button>(R.id.register)
        var login = findViewById<Button>(R.id.login_back)

        // register functionality
        register.setOnClickListener{
            var email: String = email_input.text.toString()
            var password: String = pass_input.text.toString()
            var confirm: String = confirm_input.text.toString()

            // perform various checks during registration process
            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }
            else if (password.length < 6){
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show()
            }
            else if(!TextUtils.equals(password, confirm)){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
            }
            else{
                // store registration information in firebase and log in the user
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val db = Firebase.firestore
                        val user = hashMapOf(
                            "email" to email,
                            "favorites" to listOf("")
                        )
                        db.collection("users").document(email)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error adding document $e", Toast.LENGTH_LONG).show()
                            }

                    }else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        // go back to login screen
        login.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}