package com.example.geoagenda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.create_account.*

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var createAccountButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        createAccountButton = findViewById(R.id.createAccountButton)

        createAccountButton.setOnClickListener {
            val email = newEmailText.text.toString()
            val password = newPasswordText.text.toString()

            Log.d( "CreateAccountActivity", "El correo es: " + email)
            Log.d( "CreateAccountActivity", "La contraseña es: " + password)

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d( "CreateAccountActivity", "createUserWithEmail:success")
                        val user = auth.currentUser
                        startActivity( Intent(this, MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("CreateAccountActivity",  "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        startActivity( Intent(this, MainActivity::class.java))
                    }
                }
        }
    }
}