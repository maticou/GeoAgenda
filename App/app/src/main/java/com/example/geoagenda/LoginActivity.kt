package com.example.geoagenda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {

    lateinit var loginButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            startActivity( Intent(this, MainActivity::class.java))
            Toast.makeText(this@LoginActivity, "Inicio de sesión correcto.", Toast.LENGTH_SHORT).show()
        }
    }
}

