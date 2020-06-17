package com.example.geoagenda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_login.*


class CreateAccountActivity : AppCompatActivity() {

    //UI elements

    private lateinit var auth: FirebaseAuth
    lateinit var createAccountButton: Button
    lateinit var googleCreateButton: Button
    //private val GOOGLE_SIGN_IN = 100
    //val email = newEmailText.text.toString()
    //val password = newPasswordText.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        createAccountButton = findViewById(R.id.createAccountButton)
        googleCreateButton = findViewById(R.id.googleCreateButton)

        //val email = newEmailText.text.toString()
        //val password = newPasswordText.text.toString()

        createAccountButton.setOnClickListener {
            val email = newEmailText.text.toString()
            val password = newPasswordText.text.toString()

            Log.d("CreateAccountActivity", "El correo es: " + email)
            Log.d("CreateAccountActivity", "La contraseña es: " + password)

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && isEmailValid(email) && isPasswordValid(password)) {
                        showHome(email, ProviderType.BASIC)
                    }
                    else {
                        /*if(!isEmailValid(email))
                        {
                            Toast.makeText(
                                baseContext, "Email invalido",
                                Toast.LENGTH_SHORT
                            ).show()
                        }*/
                        Toast.makeText(
                            baseContext, "Sign Up failed. Try again after some time.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            /* googleCreateButton.setOnClickListener {
             val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build()

             val googleClient = GoogleSignIn.getClient(this, googleConf)


             startActivity( Intent(this, MainActivity::class.java))


         }*/
            //setup(email, password)
        }

    }

    private fun isEmailValid(email: String): Boolean {
        if (!email.isEmpty() or android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        } else {
            return false
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        if(!password.isEmpty() or !passwordLength(password))
        {
            return true
        }
        else{
            return false
        }

    }

    private fun passwordLength(password: String): Boolean {
        if (password.length < 6)
        {
            return true
        }
        else
        {
            return false
        }
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent( this, MainActivity::class.java).apply {
            putExtra( "email", email)
            putExtra( "provider", provider.name)
        }

        startActivity(homeIntent)
    }
}