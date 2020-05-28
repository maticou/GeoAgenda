package com.example.geoagenda.ui.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.R

class LocationsFragment : Fragment() {

    private lateinit var locationsViewModel: LocationsViewModel
    lateinit var addLocationButton: Button

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        locationsViewModel =
                ViewModelProviders.of(this).get(LocationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_locations, container, false)

        return root
    }
}
