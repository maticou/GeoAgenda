package com.example.geoagenda

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.text.Html
import android.view.Window
import android.widget.Button
import android.widget.Chronometer
import androidx.core.app.ActivityCompat
import com.example.geoagenda.ui.reminder.Reminder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_reminder.*
import kotlinx.android.synthetic.main.voice_recoding_popup_layout.*
import java.io.IOException

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class AddReminderActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var recording: Boolean = true
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

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
                start_recording()
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
                play_recording()
            }

            saveRecording.setOnClickListener{

            }

            discardRecording.setOnClickListener{

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
    fun start_recording(){
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile("${externalCacheDir?.absolutePath}/audio_test.3gp")
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
    
    private fun play_recording() {
        player = MediaPlayer().apply {
            try {
                setDataSource("${externalCacheDir?.absolutePath}/audio_test.3gp")
                prepare()
                start()
            } catch (e: IOException) {
                println(e)
            }
        }
    }


}