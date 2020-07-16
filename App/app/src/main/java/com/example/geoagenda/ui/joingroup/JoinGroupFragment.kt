package com.example.geoagenda.ui.joingroup

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_joingroup.*
import com.example.geoagenda.ui.addgroup.AddGroupViewModel

class JoinGroupFragment: Fragment(), View.OnClickListener {
    private lateinit var joinGroupViewModel: JoinGroupViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        joinGroupViewModel =
            ViewModelProviders.of(this).get(JoinGroupViewModel::class.java)
        val contex: FragmentActivity
        val root = inflater.inflate(R.layout.fragment_joingroup, container, false)
        //se crea un arreglo hardcodeado para simular los corres de los admin que invitan al usuario a unirse al grupo
        val invites = arrayOf<String>("correo1@hotmail.com","correo2@hotmail.com","correo3@hotmail.com","correo4@hotmail.com")

        //asignamos la listview en la vista a la variable
        val listView = root.findViewById<ListView>(R.id.invite_list)

        listView.adapter=ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,invites)

        //A continuación viene el método que se ejecuta al presionar un elemento de la lista, en este caso un email
        listView.setOnItemClickListener{parent,view,position,id ->
            val toast = Toast.makeText(requireContext(),"Has hecho click en la invitación de\n"+invites[position],Toast.LENGTH_LONG)

            toast.setGravity(Gravity.CENTER,0,0)
            toast.show()
        }



        return root
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}