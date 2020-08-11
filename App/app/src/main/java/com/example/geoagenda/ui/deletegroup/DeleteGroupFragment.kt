package com.example.geoagenda.ui.deletegroup

import com.example.geoagenda.ui.joingroup.Invitacion
import com.example.geoagenda.ui.joingroup.JoinGroupViewModel


import android.app.AlertDialog
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
import com.example.geoagenda.ui.addgroup.GroupC
import com.example.geoagenda.ui.addgroup.Miembro
import com.example.geoagenda.ui.addgroup.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.confirm_delete_group.view.*
import kotlinx.android.synthetic.main.confirm_join_group.view.*
import kotlinx.android.synthetic.main.enter_email_invite_dialog.view.*
import java.security.acl.Group


class DeleteGroupFragment: Fragment(), View.OnClickListener {
    private lateinit var deleteGroupViewModel: DeleteGroupViewModel
    private var userReference: DatabaseReference? = null
    private lateinit var auth: FirebaseAuth
    lateinit var listView : ListView
    lateinit var adminGroupId: String
    lateinit var adminGroupEmail: String
    lateinit var adminGroupName: String
    lateinit var adminGroupAdminId: String

    //Listas
    var adminGroupEmails: ArrayList<String> = ArrayList()
    var adminGroupIDList: ArrayList<String> = ArrayList()
    lateinit var arrayAdapter: ArrayAdapter<String>

    var groupPos = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        deleteGroupViewModel =
            ViewModelProviders.of(this).get(DeleteGroupViewModel::class.java)
        val contex: FragmentActivity
        val root = inflater.inflate(R.layout.fragment_joingroup, container, false)

        //Obtener el usuario actual en la aplicación
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        //Obtener la lista de correos en las invitaciones que tenga el usuario
        userReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/grupos")
        val query: Query = userReference!!.orderByChild("adminId").equalTo(user?.uid.toString())

        Log.e(TAG,"refrencia: "+userReference.toString())
        //asignamos la listview en la vista a la variable
        listView = root.findViewById<ListView>(R.id.invite_list)


        //


        val userListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e(TAG,"Snapshot: "+snapshot)
                if (snapshot.exists()){
                    for (issue in snapshot.getChildren()) {
                        val group = issue.getValue(GroupD::class.java)
                        Log.e(TAG,"IDs: "+group!!.getAdminId())
                        adminGroupId = group!!.getId()
                        adminGroupEmail = group!!.getAdminEmail()
                        adminGroupName = group!!.getName()
                        adminGroupEmails.add(adminGroupEmail+ ": "+ adminGroupName)
                        adminGroupIDList.add(adminGroupId)

                        Log.e(TAG,"admin group id"+ adminGroupId)
                        Log.e(TAG,"admin group email"+adminGroupEmail)
                        Log.e(TAG,"admin group name"+adminGroupName)

                    }
                    arrayAdapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,adminGroupEmails)
                    listView.adapter = arrayAdapter
                    arrayAdapter.notifyDataSetChanged()
                    Log.e(TAG,"Lista de ids de grupo completo"+ adminGroupIDList)

                    listView.setOnItemClickListener{parent,view,position,id ->

                        groupPos = position

                        showBasicDialog(view, position)
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG,"falló!!")
            }
        }
        query.addListenerForSingleValueEvent(userListener)
        return root
    }


    fun showBasicDialog(view: View?,position: Int) {

        val mDialogView = LayoutInflater.from(context).inflate(R.layout.confirm_delete_group,null)

        val mBuilder= AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("Confirmación")

        val mAlertDialog = mBuilder.show()
        Log.e(TAG,"Posicion: "+ position)
        mDialogView.buttonSi.setOnClickListener{
            /*
            val user = auth.currentUser
            var miembro = Miembro(user?.uid.toString(),user?.email.toString())
            var database = FirebaseDatabase.getInstance()
            val groupRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/grupos/")
            groupRef.child(adminGroupIDList.get(position)).child("Miembros").child(user?.uid.toString()).setValue(miembro)


            //borrar la invitacion aceptada de la base de datos

            val inviteRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/"+user?.uid.toString()+"/")
            inviteRef.child("Invitaciones").child(listInviteID.get(position)).removeValue()
            arrayListEmails.removeAt(position)
            listInviteID.removeAt(position)
            arrayAdapter.notifyDataSetChanged()*/

            val database = FirebaseDatabase.getInstance()
            val deleteRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/grupos/")
            Log.d(TAG,"ID a borrar: "+ adminGroupIDList.get(position))
            deleteRef.child(adminGroupIDList.get(position)).removeValue()
            adminGroupEmails.removeAt(position)
            arrayAdapter.notifyDataSetChanged()


            val deleteGroupMiembros = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/")

            mAlertDialog.dismiss()

        }

        mDialogView.buttonNo.setOnClickListener{
            val database = FirebaseDatabase.getInstance()
            val deleteRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/grupos/")
            Log.d(TAG,"ID a borrar: "+ adminGroupIDList.get(position))
            deleteRef.child(adminGroupIDList.get(position)).removeValue()
            adminGroupEmails.removeAt(position)
            arrayAdapter.notifyDataSetChanged()
            mAlertDialog.dismiss()
        }


    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}