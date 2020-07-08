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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_login.*


class CreateAccountActivity : AppCompatActivity() {
    //UI elements
    private lateinit var auth: FirebaseAuth
    lateinit var createAccountButton: Button
    lateinit var cancelButton: Button
    var username = ""
    var email = ""
    var password = ""

    //Valores donde se indica donde se encuentra la base de datos y el almacenamiento de los datos del usuario
    val database = FirebaseDatabase.getInstance()
    val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mementos-da7d9.appspot.com")
    var user: FirebaseUser? = null
    val myRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/")

    //private val RC_SIGN_IN = 9001
    //val email = newEmailText.text.toString()
    //val password = newPasswordText.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        createAccountButton = findViewById(R.id.createAccountButton)
        cancelButton = findViewById(R.id.discardButton)


        //val email = newEmailText.text.toString()
        //val password = newPasswordText.text.toString()

        createAccountButton.setOnClickListener {
            username = newUsernameText.text.toString()
            email = newEmailText.text.toString()
            password = newPasswordText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && isEmailValid(email) && isPasswordValid(password)) {
                        user = auth.currentUser
                        showHome(email, ProviderType.BASIC)
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
        cancelButton.setOnClickListener {
            val intent = Intent( this, LoginActivity::class.java)
            startActivity(intent)
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
            putExtra( "username", username)
            putExtra( "email", email)
            putExtra( "provider", provider.name)
        }
        val myUser = User(user?.uid.toString(), email, username, "", ProviderType.BASIC)
        myRef.child(user?.uid.toString()).child("Datos-Personales").child(myUser.id).setValue(myUser)
        startActivity(homeIntent)
    }
}