package com.example.geoagenda

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.OnFocusChangeListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var loginButton : Button
    lateinit var createButton: Button
    lateinit var googleCreateButton: Button
    private val GOOGLE_SIGN_IN = 100
    //Valores donde se indica donde se encuentra la base de datos y el almacenamiento de los datos del usuario
    var user: FirebaseUser? = null
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/")

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
        googleCreateButton = findViewById(R.id.googleCreateButton)

        loginButton.setOnClickListener {
            title = "Autenticación"

            if (emailEditText.text?.isNotEmpty()!! && passwordEditText.text?.isNotEmpty()!!) {

                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                val username = "null"
                val avatar = "null"

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            user = auth.currentUser
                            getUserData()
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


    private fun showHome(email: String, username: String, avatar: String, provider: ProviderType) {
        val homeIntent = Intent( this, MainActivity::class.java).apply {
            putExtra( "email", email)
            putExtra( "username", username)
            putExtra( "avatar", avatar)
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
        val username: String? = prefs.getString("username", null)
        val avatar: String? = prefs.getString("avatar", null)
        val provider: String? = prefs.getString("provider", null)
        val currcat: String? = prefs.getString("currcat", null)

        if(email != null && username != null && avatar != null && provider != null) {
            showHome(email, username, avatar, ProviderType.valueOf(provider))
        }
    }

    private fun getUserData(){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/")

        auth = FirebaseAuth.getInstance()

        val rootRef = myRef.child(user?.uid.toString()).child("Datos-Personales")

        rootRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val values = dataSnapshot.children

                values.forEach {
                    val data = it.value as HashMap<String, String>

                    val newUser = User(data.get("id").toString(),
                        data.get("email").toString(),
                        data.get("username").toString(),
                        data.get("avatar").toString(),
                        ProviderType.BASIC)
                    showHome(newUser.email, newUser.username, newUser.avatar, ProviderType.BASIC)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            if(account != null) {
                val email = account.email.toString()
                val username = account.displayName.toString()
                val avatar = account.photoUrl.toString()
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        val myUser = User(user?.uid.toString(), email, username, avatar, ProviderType.GOOGLE)
                        myRef.child(user?.uid.toString()).child("Datos-Personales").child(myUser.id).setValue(myUser)
                        showHome(email, username, avatar, ProviderType.GOOGLE)
                    }
                    else {
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

