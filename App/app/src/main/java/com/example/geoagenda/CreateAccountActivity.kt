package com.example.geoagenda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateAccountActivity : AppCompatActivity() {

    lateinit var createAccountButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        createAccountButton = findViewById(R.id.createAccountButton)

        createAccountButton.setOnClickListener {
            startActivity( Intent(this, MainActivity::class.java))
        }
    }
}