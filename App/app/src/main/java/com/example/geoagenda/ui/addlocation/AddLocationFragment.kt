package com.example.geoagenda.ui.addlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.R


class AddLocationFragment : Fragment() {
    private lateinit var addLocationViewModel: AddLocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addLocationViewModel =
            ViewModelProviders.of(this).get(AddLocationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_addlocation, container, false)
        return root
    }
}