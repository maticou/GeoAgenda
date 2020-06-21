package com.example.geoagenda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.example.geoagenda.ui.reminder.Reminder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_reminder.*

class AddReminderActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        val actionBar = supportActionBar

        actionBar!!.title = "Agregar Recordatorio"
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Agregar Recordatorio </font>"));
        actionBar.setDisplayHomeAsUpEnabled(true)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/")

        auth = FirebaseAuth.getInstance()

        //codigo para el boton de guardar
        save.setOnClickListener(){
            val titleInput: String = title_input.text.toString()
            val noteInput: String = note_input.text.toString()
            val user = auth.currentUser

            var reminder = Reminder("",titleInput, noteInput)

            var itemId = myRef.child(user?.uid.toString()).push()
            reminder.id = itemId.key.toString()

            myRef.child(user?.uid.toString()).child("Notas").child(reminder.id).setValue(reminder)
            onBackPressed()
        }

        //codigo par el boton de descartar
        discard.setOnClickListener(){
            onBackPressed()
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}