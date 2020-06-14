package com.example.geoagenda.ui.addgroup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.R
import kotlinx.android.synthetic.main.fragment_addgroup.*
import java.io.IOException


class AddGroupFragment : Fragment(), View.OnClickListener {
    private lateinit var addGroupViewModel: AddGroupViewModel
    val SELECT_PICTURE = 2
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
        return root



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK){
            try{
                val uri = data!!.data
                imagenGrupo.setImageURI(uri)
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