package com.example.geoagenda

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nav_header_main.*
import java.net.URL

enum class ProviderType {
    BASIC,
    GOOGLE
}

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Datos de inicio de sesión
        val bundle: Bundle? = intent.extras
        val email = bundle?.getString( "email")
        val username = bundle?.getString( "username")
        val avatar = bundle?.getString( "avatar")
        val provider = bundle?.getString( "provider")

        // Guardar sesion
        val prefs =  getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        prefs.edit().putString("email", email).commit()
        prefs.edit().putString("username", username).commit()
        prefs.edit().putString("avatar", avatar).commit()
        prefs.edit().putString("provider", provider).commit()

        //Toast que muestra el email de acceso
        Toast.makeText(this@MainActivity, prefs.getString("email", "null"), Toast.LENGTH_SHORT).show()
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        //Para obtener el TextView del nav_header_main que tiene la
        // imagen de perfil, nombre de usuario y correo del side menu
        val headerView = navView.getHeaderView(0)
        val userName = headerView.findViewById<TextView>(R.id.userNameTextView)
        val userEmail = headerView.findViewById<TextView>(R.id.userEmailTextView)
        userName.setText(username)
        userEmail.setText(email)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_rec, R.id.nav_categories, R.id.nav_logOut), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val bundle: Bundle? = intent.extras
        val avatar = bundle?.getString( "avatar")
        val userAvatar = userAvatarimageView.findViewById<ImageView>(R.id.userAvatarimageView)
        DownLoadImageTask(userAvatar).execute(avatar)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun logoutSession() {
        val prefs =  getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        prefs.edit().putString("email", null).commit()
        prefs.edit().putString("username", null).commit()
        prefs.edit().putString("avatar", null).commit()
        prefs.edit().putString("provider", null).commit()
        prefs.edit().commit()
        FirebaseAuth.getInstance().signOut()
        onBackPressed()
        val intent = Intent( this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun addReminder(){
        val intent = Intent(this, AddReminderActivity::class.java)
        startActivity(intent)
    }

    private class DownLoadImageTask(internal val imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg urls: String): Bitmap? {
            val urlOfImage = urls[0]
            return try {
                val inputStream = URL(urlOfImage).openStream()
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) { // Catch the download exception
                e.printStackTrace()
                null
            }
        }
        override fun onPostExecute(result: Bitmap?) {
            if(result!=null){
                // Display the downloaded image into image view
                Toast.makeText(imageView.context,"download success",Toast.LENGTH_SHORT).show()
                val resize = Bitmap.createScaledBitmap(result, 100, 100, false)
                imageView.setImageBitmap(resize)
            }else{
                Toast.makeText(imageView.context,"Error downloading",Toast.LENGTH_SHORT).show()
            }
        }
    }
}

