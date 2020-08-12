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
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
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
import com.example.geoagenda.ui.reminder.Reminder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_reminder.*
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap
import com.example.geoagenda.ui.addlocation.Location
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_joingroup.*
import kotlinx.android.synthetic.main.reminder_card.*
import java.io.ByteArrayOutputStream
import kotlin.time.milliseconds

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
private const val REQUEST_GALLERY = 2
private const val REQUEST_IMAGE_CAPTURE = 1


class AddReminderActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var auth: FirebaseAuth
    private var recording: Boolean = true
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var recordingPath: String? = null
    private var imagePath: String? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var preview: ImageView
    private  var imguri: Uri? = null
    private lateinit var dropMenu: AutoCompleteTextView
    private lateinit var dropMenucat: AutoCompleteTextView
    private var locationsList = ArrayList<String>()
    private var locationsIdList = ArrayList<String>()
    private var categoriesList = ArrayList<String>()
    private var categoriesIdList = ArrayList<String>()
    private var category: String = ""
    private lateinit var reminder: Reminder
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay: Int = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0
    var deleteOption: String = "FALSE"
    private var mNotificationTime: Long = 0
    private var mNotified = false
    private var tiempoTotal: Long = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_GALLERY)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_IMAGE_CAPTURE)

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
        reminder = Reminder("","","","","", "","","","","","", "")

        //Este codigo se encarga de obtener las ubicaciones almacenadas en la base de datos
        val locations_ref = myRef.child(user?.uid.toString()).child("Ubicaciones")
        //igual para las categorias
        val categories_ref = myRef.child(user?.uid.toString()).child("Categorias")

        locations_ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                //error al obtener datos
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val values = snapshot.children
                locationsList.clear()
                locationsIdList.clear()

                values.forEach {
                    val data = it.value as HashMap<String, String>

                    locationsList.add(data.get("nombre").toString())
                    locationsIdList.add(data.get("id").toString())
                }

                //codigo para rellenar el campo de ubicacion
                var locationID = intent.getStringExtra("REMINDER_LOCATION")
                val locationPosition = locationsIdList.indexOf(locationID)

                if(locationPosition != -1){
                    dropMenu.setText(locationsList[locationPosition], false)
                }
            }
        })


        categories_ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                //error al obtener datos
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val values = snapshot.children
                categoriesList.clear()
                categoriesIdList.clear()

                values.forEach {
                    val data = it.value as HashMap<String, String>

                    categoriesList.add(data.get("title").toString())
                    categoriesIdList.add(data.get("id").toString())
                }

                //codigo para rellenar el campo de categoria
                var categoryID = intent.getStringExtra("REMINDER_CATEGORY")
                val categoryPosition = categoriesIdList.indexOf(categoryID)

                if(categoryPosition != -1){
                    dropMenucat.setText(categoriesList[categoryPosition], false)
                }
            }
        })
        dropMenu = findViewById(R.id.filled_exposed_dropdown)
        dropMenucat = findViewById(R.id.filled_exposed_dropdown_cat)

        //Aqui se rellenan los formularios con los datos del recordatorio clickeado
        title_input.setText(intent.getStringExtra("REMINDER_TITLE"))
        note_input.setText(intent.getStringExtra("REMINDER_NOTE"))
        recordingPath = intent.getStringExtra("REMINDER_AUDIO")
        imagePath = intent.getStringExtra("REMINDER_IMAGE")
        if(!intent.getStringExtra("REMINDER_DELETE_OPTION").isNullOrEmpty()){
            deleteOption = intent.getStringExtra("REMINDER_DELETE_OPTION")
        }
        if(!intent.getStringExtra("REMINDER_DAY").isNullOrEmpty()){
            myDay = intent.getStringExtra("REMINDER_DAY").toInt()
        }
        if(!intent.getStringExtra("REMINDER_MONTH").isNullOrEmpty()){
            myMonth = intent.getStringExtra("REMINDER_MONTH").toInt()
        }
        if(!intent.getStringExtra("REMINDER_YEAR").isNullOrEmpty()){
            myYear = intent.getStringExtra("REMINDER_YEAR").toInt()
        }
        if(!intent.getStringExtra("REMINDER_HOUR").isNullOrEmpty()){
            myHour = intent.getStringExtra("REMINDER_HOUR").toInt()
        }
        if(!intent.getStringExtra("REMINDER_MINUTE").isNullOrEmpty()){
            myMinute = intent.getStringExtra("REMINDER_MINUTE").toInt()
        }

        //Estos valores modifican datos de la barra de la ventana para crear recordatorios
        val actionBar = supportActionBar
        val backgroundColor = ColorDrawable(getColor(R.color.colorPrimary))

        //Le asigno el nombre de la actividad a editar recordatorio
        //de lo contrario se queda como agregar recordatorio
        actionBar!!.title = intent.getStringExtra("ACTIVITY_TITLE")
        if(actionBar!!.title == null){
            actionBar!!.title = "Agregar Recordatorio"
        }

        //Estos valores modifican datos de la barra de la ventana para crear recordatorios
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>${actionBar!!.title} </font>"));
        actionBar.setBackgroundDrawable(backgroundColor)
        actionBar.setDisplayHomeAsUpEnabled(true)

        //Le asigno el id de un recordatorio ya creado para
        // actualizarlo, pero si no existe, se crea desde cero
        var reminderID = intent.getStringExtra("REMINDER_ID")
        if(reminderID == null){
            reminderID = itemId.key.toString()
        }
        var reminderRecording = ""
        var reminderImage = ""


        //codigo encargado del menu desplegable para seleccionar una ubicacion
        val adapter = ArrayAdapter<String>(this, R.layout.drop_menu_item, locationsList )
        dropMenu.setAdapter(adapter)
        dropMenu.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view, position, id->
            //Toast.makeText(applicationContext,"Position : $position",Toast.LENGTH_SHORT).show()
            reminder.location = locationsIdList[position]
        }

        //codigo encargado del menu desplegable para seleccionar una categoria
        val adapter2 = ArrayAdapter<String>(this, R.layout.drop_menu_item, categoriesList )
        dropMenucat.setAdapter(adapter2)
        dropMenucat.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view, position, id->
            //Toast.makeText(applicationContext,"Position : $position",Toast.LENGTH_SHORT).show()
            reminder.category = categoriesIdList[position]
        }

        //codigo para el boton de guardar
        save.setOnClickListener(){
            val titleInput: String = title_input.text.toString()
            val noteInput: String = note_input.text.toString()

            if(reminderRecording == "") {
                reminderRecording = recordingPath.toString()
            }
            if(reminderImage == "") {
                reminderImage = imagePath.toString()
            }


            reminder.id = reminderID
            reminder.title = titleInput
            reminder.note = noteInput
            reminder.recording = reminderRecording
            reminder.image = reminderImage
            reminder.day = myDay.toString()
            reminder.month =  myMonth.toString()
            reminder.year = myYear.toString()
            reminder.hour = myHour.toString()
            reminder.minute = myMinute.toString()

            myRef.child(user?.uid.toString()).child("Notas").child(reminder.id).setValue(reminder)
            if (!mNotified) {
                if(reminder.day != "0"){
                    mNotificationTime = Calendar.getInstance().timeInMillis + 10000 //Set after 10 seconds from the current time.
                    NotificationUtils().setNotification(mNotificationTime, reminder.title, reminder.note, this@AddReminderActivity)
                    //NotificationUtils().setNotification(tiempoTotal, reminder.title, reminder.note, this@AddReminderActivity)
                }
            }
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
                add_voice_recording.setImageDrawable(resources.getDrawable(R.drawable.ic_keyboard_voice_green))
                voiceRecordingDialog.dismiss()
            }

            discardRecording.setOnClickListener{
                //verificar si el archivo esxiste y si existe se elimina, sino solo se cierra el
                //dialog

                voiceRecordingDialog.dismiss()
            }

            voiceRecordingDialog.show()
        }

        add_image.setOnClickListener {
            val addImageDialog = Dialog(this)
            addImageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            addImageDialog.setContentView(R.layout.add_image_popup_layout)

            preview = addImageDialog.findViewById(R.id.image_from_gallery_preview) as ImageView

            var openGallery = addImageDialog.findViewById(R.id.button_open_gallery) as Button
            var saveImage = addImageDialog.findViewById(R.id.button_save_image) as Button
            var discardImage = addImageDialog.findViewById(R.id.button_discard_image) as Button
            var openCamera = addImageDialog.findViewById(R.id.button_open_camera) as Button

            openGallery.setOnClickListener{
                val intentGaleria = Intent(Intent.ACTION_PICK)
                intentGaleria.type = "image/*"
                startActivityForResult(intentGaleria, REQUEST_GALLERY)
            }

            saveImage.setOnClickListener {

                //var image_preview = Uri.fromFile(File("${externalCacheDir?.absolutePath}/${reminderID}.jpg"))
                //val imageRef = storageRef.child("${user?.uid.toString()}/Image/${image_preview.lastPathSegment}")
                val imageRef = storageRef.child("${user?.uid.toString()}/Imagen/"+ UUID.randomUUID().toString())
                reminderImage = "${externalCacheDir?.absolutePath}/${reminderID}.jpg"

                var uploadTask = imageRef.putFile(imguri!!).addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnCompleteListener { taskSnapshot ->

                        var url = taskSnapshot.result
                        reminderImage = url.toString()

                        println ("url =" + url.toString ())
                    }
                }

                uploadTask.addOnFailureListener {
                    println("Ocurrio un error al subir el archivo")
                }.addOnSuccessListener {
                    println("El archivo se ha subido correctamente")
                }

                add_image.setImageDrawable(resources.getDrawable(R.drawable.ic_photo_green))
                addImageDialog.dismiss()
            }

            openCamera.setOnClickListener {
                dispatchTakePictureIntent()
            }

            discardImage.setOnClickListener {
                addImageDialog.dismiss()
            }

            addImageDialog.show()

        }


        alarmButton.setOnClickListener {
            tiempoTotal = 0
            showDateTimeDialog()
        }

        delete_reminder.setOnClickListener {
            if(deleteOption == "TRUE") {
                val deleteRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/${user?.uid.toString()}/Notas/")
                val reminderQuery: Query = deleteRef.orderByChild("id").equalTo(reminderID)
                reminderQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (reminderSnapshot in dataSnapshot.children) {
                            reminderSnapshot.ref.removeValue()
                            onBackPressed()
                        }
                    }
                })
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showDateTimeDialog() {
        val calendar: Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(this, R.style.DialogTheme, this, year, month,day)
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.colorPrimary))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimary))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY)
        {
            imguri = data!!.data
            preview.setImageURI(imguri)
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras?.get("data") as Bitmap
            imguri = getImageUriFromBitmap(this, imageBitmap)
            //preview.setImageBitmap(imageBitmap)
            //imguri = data!!.data
            preview.setImageURI(imguri)
        }
        else{

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDateSet(view: DatePicker?, year1: Int, month1: Int, day1: Int) {
        myDay = day1
        myYear = year1
        myMonth = month1
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, R.style.DialogTheme, this, hour, minute, DateFormat.is24HourFormat(this))
        timePickerDialog.show()
        timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.colorPrimary))
        timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimary))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute1: Int) {
        myHour = hourOfDay
        myMinute = minute1
        val calendar2: Calendar = Calendar.getInstance()
        calendar2.set(myYear, myMonth, myDay, myHour, myMinute)
        tiempoTotal = calendar2.timeInMillis
        //val text: String = "Year: " + myYear + "\n" + "Month: " + myMonth + "\n" + "Day: " + myDay + "\n" + "Hour: " + myHour + "\n" + "Minute: " + myMinute
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show()

        alarmButton.setImageDrawable(resources.getDrawable(R.drawable.ic_alarm_on))
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }



}