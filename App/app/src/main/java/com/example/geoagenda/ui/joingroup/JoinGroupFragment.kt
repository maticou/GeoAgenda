package com.example.geoagenda.ui.joingroup

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class JoinGroupFragment: Fragment(), View.OnClickListener {
    private lateinit var joinGroupViewModel: JoinGroupViewModel
    private var userReference: DatabaseReference? = null
    private lateinit var auth: FirebaseAuth
    var arrayListEmails: ArrayList<String> = ArrayList()
    lateinit var arrayAdapter: ArrayAdapter<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        joinGroupViewModel =
            ViewModelProviders.of(this).get(JoinGroupViewModel::class.java)
        val contex: FragmentActivity
        val root = inflater.inflate(R.layout.fragment_joingroup, container, false)

        //Obtener el usuario actual en la aplicación
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        //Obtener la lista de correos en las invitaciones que tenga el usuario
        userReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/"+user?.uid+"/Invitaciones")


//se crea un arreglo hardcodeado para simular los corres de los admin que invitan al usuario a unirse al grupo
        //val invites = arrayOf<String>("correo1@hotmail.com","correo2@hotmail.com","correo3@hotmail.com","correo4@hotmail.com")

        //asignamos la listview en la vista a la variable
        val listView = root.findViewById<ListView>(R.id.invite_list)


        userReference!!.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e(TAG,"Snapshot: "+snapshot)
                if (snapshot.exists()){
                    for (issue in snapshot.getChildren()) {
                        val invitacion = issue.getValue(Invitacion::class.java)
                        Log.e(TAG,"Email invitador: "+invitacion!!.idGrupo)
                        var inviteGroupId = invitacion!!.idGrupo
                        var inviteGroupEmail = invitacion!!.email
                        arrayListEmails.add(inviteGroupEmail)


                        //userReference!!.child(invitedId).child("Invitaciones").child(group.id).setValue(invitation)
                        //Log.e(TAG,"reference: "+ userReference.toString())
                    }
                    arrayAdapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,arrayListEmails)
                    listView.adapter = arrayAdapter
                    arrayAdapter.notifyDataSetChanged()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG,"falló!!")
            }
        })




        //A continuación viene el método que se ejecuta al presionar un elemento de la lista, en este caso un email
        listView.setOnItemClickListener{parent,view,position,id ->
            //val toast = Toast.makeText(requireContext(),"Has hecho click en la invitación de\n"+invites[position],Toast.LENGTH_LONG)

            //toast.setGravity(Gravity.CENTER,0,0)
            //toast.show()
        }



        return root
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}