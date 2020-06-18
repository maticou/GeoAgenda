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
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_login.*


class CreateAccountActivity : AppCompatActivity() {

    //UI elements

    private lateinit var auth: FirebaseAuth
    lateinit var createAccountButton: Button
    lateinit var googleCreateButton: Button
    private val GOOGLE_SIGN_IN = 100
    //private val RC_SIGN_IN = 9001
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

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
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
            //setup(email, password)
            }
        googleCreateButton.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            val signInIntent = googleClient.signInIntent
            // startActivity( Intent(this, MainActivity::class.java))
            startActivityForResult(
                signInIntent, GOOGLE_SIGN_IN
            )
            //startActivity(Intent(this, MainActivity::class.java))

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN)
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            if(account != null)
            {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    else
                    {
                        Toast.makeText(
                            baseContext, "Sign Up failed. Try again after some time.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }
}