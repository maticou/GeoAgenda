package com.example.geoagenda


import android.Manifest
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Html
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.geoagenda.ui.reminder.Reminder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_reminder.*
import java.io.File
import java.io.IOException

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class AddReminderActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var recording: Boolean = true
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private lateinit var storage: FirebaseStorage

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        //Aqui se rellenan los formularios con los datos del recordatorio clickeado
        title_input.setText(intent.getStringExtra("REMINDER_TITLE"))
        note_input.setText(intent.getStringExtra("REMINDER_NOTE"))

        //Estos valores modifican datos de la barra de la ventana para crear recordatorios
        val actionBar = supportActionBar
        val backgroundColor = ColorDrawable(getColor(R.color.colorPrimary))
        actionBar!!.title = "Agregar Recordatorio"
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Agregar Recordatorio </font>"));
        actionBar.setBackgroundDrawable(backgroundColor)
        actionBar.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()

        //Valores donde se indica donde se encuentra la base de datos y el almacenamiento de los datos del usuario
        val database = FirebaseDatabase.getInstance()
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mementos-da7d9.appspot.com")
        val myRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/")

        //Parametros que se utilizaran en la creacion de un recordatorio
        val user = auth.currentUser
        var itemId = myRef.child(user?.uid.toString()).push()

        //Le asigno el id de un recordatorio ya creado para
        // actualizarlo, pero si no existe, se crea desde cero
        var reminderID = intent.getStringExtra("REMINDER_ID")
        if(reminderID == null){
            reminderID = itemId.key.toString()
        }
        var reminderRecording = ""

        //codigo para el boton de guardar
        save.setOnClickListener(){
            val titleInput: String = title_input.text.toString()
            val noteInput: String = note_input.text.toString()

            var reminder = Reminder(reminderID,titleInput, noteInput, reminderRecording)

            myRef.child(user?.uid.toString()).child("Notas").child(reminder.id).setValue(reminder)
            onBackPressed()
        }

        //codigo par el boton de descartar
        discard.setOnClickListener(){
            onBackPressed()
        }

        //Codigo encargado de mostrar el dialog para grabar mensajes de voz y asignar los listeners a cada boton
        add_voice_recording.setOnClickListener{
            val  voiceRecordingDialog = Dialog(this)
            voiceRecordingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            voiceRecordingDialog.setContentView(R.layout.voice_recoding_popup_layout)

            var startRecording = voiceRecordingDialog.findViewById(R.id.start_recording) as FloatingActionButton
            var stopRecording = voiceRecordingDialog.findViewById(R.id.stop_recording) as FloatingActionButton
            var playRecording = voiceRecordingDialog.findViewById(R.id.play_recording) as FloatingActionButton
            var saveRecording = voiceRecordingDialog.findViewById(R.id.save_recording) as Button
            var discardRecording = voiceRecordingDialog.findViewById(R.id.discard_recording) as Button
            var chronometer = voiceRecordingDialog.findViewById(R.id.c_meter) as Chronometer

            startRecording.setOnClickListener{
                start_recording(reminderID)
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.start()
                recording = true
            }

            stopRecording.setOnClickListener{
                stop_recording()
                chronometer.stop()
                recording = false
            }

            playRecording.setOnClickListener{
                play_recording(reminderID)
            }

            saveRecording.setOnClickListener{
                //se crea el directorio necesario para guardar el archivo en firebase y luego se almacena
                var recording = Uri.fromFile(File("${externalCacheDir?.absolutePath}/${reminderID}.3gp"))
                val recordingsRef = storageRef.child("${user?.uid.toString()}/Audio/${recording.lastPathSegment}")

                reminderRecording = "${externalCacheDir?.absolutePath}/${reminderID}.3gp"
                var uploadTask = recordingsRef.putFile(recording)

                uploadTask.addOnFailureListener {
                    println("Ocurrio un error al subir el archivo")
                }.addOnSuccessListener {
                    println("El archivo se ha subido correctamente")
                }

                voiceRecordingDialog.dismiss()
            }

            discardRecording.setOnClickListener{
                //verificar si el archivo esxiste y si existe se elimina, sino solo se cierra el
                //dialog

                voiceRecordingDialog.dismiss()
            }

            voiceRecordingDialog.show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /*
    Funcion encargada de comenzar la grabacion de audio
     */
    fun start_recording(audio: String){
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile("${externalCacheDir?.absolutePath}/${audio}.3gp")
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                println("Ha ocurrido un problema con la grabacion")
            }

            start()
        }
    }

    /*
        Funcion encargada de detener la grabacion y liberar el objeto recorder
     */
    fun stop_recording(){
        recorder?.apply {
            stop()
            release()
        }
        recorder = null

    }
    
    private fun play_recording(audio: String) {
        player = MediaPlayer().apply {
            try {
                setDataSource("${externalCacheDir?.absolutePath}/${audio}.3gp")
                prepare()
                start()
            } catch (e: IOException) {
                println(e)
            }
        }
    }

    private fun delete_recording(audio: String){

    }


}