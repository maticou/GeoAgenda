package com.example.geoagenda.ui.editlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.R
import com.example.geoagenda.ui.editgroup.EditGroupViewModel


class EditLocationFragment : Fragment() {
    private lateinit var editLocationViewModel : EditLocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editLocationViewModel =
            ViewModelProviders.of(this).get(EditLocationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_editlocation, container, false)
        return root
    }
}