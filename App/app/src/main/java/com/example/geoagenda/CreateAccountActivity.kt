package com.example.geoagenda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_create_account.*


class CreateAccountActivity : AppCompatActivity() {

    //UI elements

    private lateinit var auth: FirebaseAuth
    lateinit var createAccountButton : Button
    lateinit var googleCreateButton: Button
    //private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        createAccountButton = findViewById(R.id.createAccountButton)
        googleCreateButton = findViewById(R.id.googleCreateButton)

        createAccountButton.setOnClickListener {
            val email = newEmailText.text.toString()
            val password = newPasswordText.text.toString()

            Log.d( "CreateAccountActivity", "El correo es: " + email)
            Log.d( "CreateAccountActivity", "La contraseña es: " + password)


            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && isEmailValid(email)) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d( "CreateAccountActivity", "createUserWithEmail:success")
                        val user = auth.currentUser
                        startActivity( Intent(this, MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("CreateAccountActivity",  "createUserWithEmail:failure", task.exception)
                        if(isEmailValid(email) == false)
                        {
                            Toast.makeText(baseContext, "Correo no valido.",
                                Toast.LENGTH_SHORT).show()
                            //startActivity( Intent(this, CreateAccountActivity::class.java))
                        }
                        /*if(isPasswordValid(password) == false)
                        {
                            Toast.makeText(baseContext, "Ingrese una contraseña.",
                                Toast.LENGTH_SHORT).show()
                        }*/
                        /*Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()*/
                        //startActivity( Intent(this, MainActivity::class.java))
                    }
                }
        }

        googleCreateButton.setOnClickListener {
            /*val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
             */

            startActivity( Intent(this, MainActivity::class.java))


        }
    }

    fun isEmailValid(email: String): Boolean {
        if(!email.isEmpty() or android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            return true
        }
        else
        {
            return false
        }
    }

    /*fun isPasswordValid(password: String): Boolean {
        if(!password.isEmpty())
        {
            return true
        }
        else
        {
            return false
        }
    }*/
}