package com.example.geoagenda


import android.Manifest
import android.app.*
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.text.Html
import android.text.format.DateFormat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.geoagenda.ui.Category.Category
import com.example.geoagenda.ui.reminder.Reminder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_reminder.*
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap
import com.example.geoagenda.ui.addlocation.Location
import kotlinx.android.synthetic.main.activity_add_category.*
import kotlinx.android.synthetic.main.activity_add_reminder.discard
import kotlinx.android.synthetic.main.activity_add_reminder.save
import kotlinx.android.synthetic.main.fragment_joingroup.*
import kotlinx.android.synthetic.main.reminder_card.*
import kotlin.time.milliseconds

private const val REQUEST_GALLERY = 2

class AddCategoryActivity:AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    private var imagePath: String? = null
    private var permissions: Array<String> = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private lateinit var storage: FirebaseStorage
    private lateinit var preview: ImageView
    private  var imguri: Uri? = null
    private lateinit var category: Category

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_GALLERY)

        //Valores donde se indica donde se encuentra la base de datos y el almacenamiento de los datos del usuario
        val database = FirebaseDatabase.getInstance()
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mementos-da7d9.appspot.com")
        val myRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/")

        //Parametros que se utilizaran en la creacion de un recordatorio
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        var itemId = myRef.child(user?.uid.toString()).push()

        //Esta linea crea un recordatorio con todos sus campos vacios para luego rellenarlos
        //cuando se termina de crear un recordatorio
        category = Category("","","")


        //Estos valores modifican datos de la barra de la ventana para crear recordatorios
        val actionBar = supportActionBar
        val backgroundColor = ColorDrawable(getColor(R.color.colorPrimary))

        //Le asigno el nombre de la actividad a editar recordatorio
        //de lo contrario se queda como agregar recordatorio
        actionBar!!.title = intent.getStringExtra("ACTIVITY_TITLE")
        if(actionBar!!.title == null){
            actionBar!!.title = "Agregar Categoria"
        }

        //Estos valores modifican datos de la barra de la ventana para crear recordatorios
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>${actionBar!!.title} </font>"));
        actionBar.setBackgroundDrawable(backgroundColor)
        actionBar.setDisplayHomeAsUpEnabled(true)

        //Le asigno el id de un recordatorio ya creado para
        // actualizarlo, pero si no existe, se crea desde cero
        var categoryID = intent.getStringExtra("CATEGORY_ID")
        if(categoryID == null){
            categoryID = itemId.key.toString()
        }
        var categoryImage = ""


        //codigo para el boton de guardar
        cat_save.setOnClickListener(){
            val titleInput: String = cat_title_input.text.toString()

            if(categoryImage == "") {
                categoryImage = imagePath.toString()
            }


            category.id = categoryID
            category.title = titleInput
            category.image = categoryImage

            myRef.child(user?.uid.toString()).child("Categorias").child(category.id).setValue(category)
            onBackPressed()
        }

        //codigo par el boton de descartar
        cat_discard.setOnClickListener(){
            onBackPressed()
        }

    }

}