package com.example.geoagenda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {

    lateinit var loginButton : Button
    lateinit var createButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.login_button)
        createButton = findViewById(R.id.signup_button)

        loginButton.setOnClickListener {
            startActivity( Intent(this, MainActivity::class.java))
            Toast.makeText(this@LoginActivity, "Inicio de sesión correcto.", Toast.LENGTH_SHORT).show()
        }

        createButton.setOnClickListener {
            startActivity( Intent( this, CreateAccountActivity::class.java))
        }
    }
}

