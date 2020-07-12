package com.example.geoagenda.ui.addgroup

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.R
import com.example.geoagenda.ui.group.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.enter_email_invite_dialog.view.*
import kotlinx.android.synthetic.main.fragment_addgroup.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class AddGroupFragment : Fragment(), View.OnClickListener {
    private lateinit var addGroupViewModel: AddGroupViewModel
    private  val TAG = "MyActivity"
    private var selectedList = booleanArrayOf(false, false, false, false,false)
    val SELECT_PICTURE = 2
    private lateinit var auth: FirebaseAuth
    public  var imguri: Uri? = null
    lateinit var userList: ArrayList<User>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //btnImg.setOnClickListener(this);
        addGroupViewModel =
            ViewModelProviders.of(this).get(AddGroupViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_addgroup, container, false)

        // TODOS LOS ELEMENTOS QUE SE UTILIZAN CON
        // METODOS COMO setOnClickListener SE DEBEN
        // INICIALIZAR PRIMERO COMO APARECE A CONTINUACIÓN.
        val btnImg: Button = root.findViewById(R.id.btnImg)
        btnImg.setOnClickListener{
            dispatchGalleryIntent()
        }


        //Parámetros para la base de datos en firebase
        val database = FirebaseDatabase.getInstance()
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mementos-da7d9.appspot.com")
        val myRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/")
        auth = FirebaseAuth.getInstance()

        //Parámetros que tendrá un nuevo grupo al ser creado
        val user = auth.currentUser
        var itemId = myRef.child(user?.uid.toString()).push()
        val groupID = itemId.key.toString()
        userList = arrayListOf()
        //val nombreGrupo: EditText = root.findViewById(R.id.nombreGrupo)

        //val inputValue: String = nombreGrupo.text.toString()

        //val nombreGrupo: TextInputEditText = root.findViewById(R.id.btnCrearGrupo)
        //val textoNombreGrupo = nombreGrupo.text.toString()

        val btnCrearGrupo: Button = root.findViewById(R.id.createGroupButton)

        val botonAgregarMiembro: ImageButton = root.findViewById(R.id.btnAgregarMiembro)
        botonAgregarMiembro.setOnClickListener {
        showBasicDialog(null)
        }


       //Llamar al evento para obtener los usuarios actuales del sistema para poder agregarlos

        var groupImage = ""

        //Aqui se deberia crear un grupo con los campos de nombre y descripción
        btnCrearGrupo.setOnClickListener{
            //Texto obtenido del campo nombre de grupo
            val nombreGrp: String = nombreGrupo.text.toString()
            //Texto obtenido del campo descripción del grupo
            val desc : String = groupDescriptionEditText.text.toString()

           //Creacion de un grupo al presionar el boton y enviar datos a firebase

            groupImage ="${getActivity()?.getExternalCacheDir()?.absolutePath}/${groupID}.jpg"
            var group = Group(groupID,nombreGrp, desc, user?.uid.toString(),groupImage)


            myRef.child(user?.uid.toString()).child("Grupos").child(group.id).setValue(group)

            //se crea el directorio necesario para guardar el archivo en firebase y luego se almacena
            //var imagen = Uri.fromFile(File("${""}/${groupID}.png"))
            val imagenRef = storageRef.child("${groupID}/Imagen/"+ UUID.randomUUID().toString())

            var uploadTask = imagenRef.putFile(imguri!!)

            uploadTask.addOnFailureListener {
                println("Ocurrio un error al subir el archivo")
            }.addOnSuccessListener {
                println("El archivo se ha subido correctamente")
                activity?.onBackPressed()
            }




            //Tostadas para testeo del ingreso de campos
            //val toast = Toast.makeText(context, "Nombre grupo: "+nombreGrp, Toast.LENGTH_LONG)
            //val toast2 = Toast.makeText(context,"Descripcion: "+desc,Toast.LENGTH_LONG)
            //toast.setGravity(Gravity.TOP or Gravity.LEFT, 0, 0)
            //toast2.setGravity(Gravity.TOP or Gravity.RIGHT,0,0)
            //toast.show()
            //toast2.show()


        }


        return root



    }

    /*
    Funcion para mostrar el dialogo para agregar miembros a un grupo
     */
    fun showBasicDialog(view: View?) {

       val mDialogView = LayoutInflater.from(context).inflate(R.layout.enter_email_invite_dialog,null)

        val mBuilder= AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("Ingresa correo para invitar a un usuario al grupo")

        val mAlertDialog = mBuilder.show()
        mDialogView.EnviarBtn.setOnClickListener{
            mAlertDialog.dismiss()
            val email = mDialogView.edit_text_email.text.toString()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK){
            try{
                imguri = data!!.data
                imagenGrupo.setImageURI(imguri)


                //storageRef.putFile(uri2)
            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    fun dispatchGalleryIntent(){
        val intent = Intent()
        intent.type ="image/*"
        intent.action =  Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select image"),SELECT_PICTURE)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}