package com.example.weatherapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapplication.databinding.SignupPageBinding
import com.google.firebase.auth.FirebaseAuth

class SignupPage : AppCompatActivity() {
    private lateinit var binding: SignupPageBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FirebaseAuth and binding
        firebaseAuth = FirebaseAuth.getInstance()
        binding = SignupPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSignUp.setOnClickListener {
            val name = binding.SignupName.text.toString()
            val email = binding.SignupEmail.text.toString()
            val pass = binding.SignupPassword.text.toString()
            val confirmPassword = binding.SignUpConfirmPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (pass == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { createUserTask ->
                            if (createUserTask.isSuccessful) {
                                firebaseAuth.currentUser?.sendEmailVerification()
                                    ?.addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Toast.makeText(this, "Email Sent! Please Verify", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this, LoginPage::class.java)
                                            startActivity(intent)
                                        } else {
                                            Toast.makeText(this, "Error in sending verification email! ${it.exception}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(this, "User creation failed! ${createUserTask.exception}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }

        binding.AlreadyAccount.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }
}
