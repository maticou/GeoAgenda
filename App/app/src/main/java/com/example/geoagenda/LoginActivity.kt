package com.example.geoagenda

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.OnFocusChangeListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var loginButton : Button
    lateinit var createButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Funcion para login
        setup()
    }

    override fun onStart() {
        super.onStart()
        // Verificar sesion
        session()
    }

    private fun setup() {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        loginButton = findViewById(R.id.login_button)
        createButton = findViewById(R.id.signup_button)

        loginButton.setOnClickListener {
            title = "Autenticación"

            if (emailEditText.text?.isNotEmpty()!! && passwordEditText.text?.isNotEmpty()!!) {

                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()


                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            showHome(email, ProviderType.BASIC)
                        } else {
                            // If sign in fails, display a message to the user.
                            emailEditTextLayout.error = getString(R.string.error)
                            passwordEditTextLayout.error = getString(R.string.error)
                        }
                    }
            } else {
                emailEditTextLayout.error = getString(R.string.no_empty_email)
                passwordEditTextLayout.error = getString(R.string.no_empty_password)
            }
        }
        createButton.setOnClickListener {
            startActivity( Intent( this, CreateAccountActivity::class.java))
        }
    }


    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent( this, MainActivity::class.java).apply {
            putExtra( "email", email)
            putExtra( "provider", provider.name)
        }

        startActivity(homeIntent)
    }

    // Despues de cerrar sesion no se permite volver a la actividad anterior
    override fun onBackPressed() {

    }

    private fun session() {
        val prefs =  getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email: String? = prefs.getString("email", null)
        val provider: String? = prefs.getString("provider", null)

        if(email != null && provider != null) {
            showHome(email, ProviderType.valueOf(provider))
        }
    }
}

