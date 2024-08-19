package com.example.weatherapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen) // Ensure you have a splash layout

        firebaseAuth = FirebaseAuth.getInstance()

        // Delay for 1.5 seconds
        Handler().postDelayed({
            // Check if the user is logged in
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null && currentUser.isEmailVerified) {
                // User is logged in and email is verified, go to Homepage
                val intent = Intent(this, Homepage::class.java)
                startActivity(intent)
            } else {
                // User is not logged in, go to LoginPage
                val intent = Intent(this, LoginPage::class.java)
                startActivity(intent)
            }
            finish() // Close the splash screen activity
        }, 1500) // 1.5 seconds delay
    }
}