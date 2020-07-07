package com.example.geoagenda.ui.addlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class AddLocationFragment : Fragment(), OnMapReadyCallback {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    private lateinit var addLocationViewModel: AddLocationViewModel
    private var latitude = -35.4333
    private var longitude = -71.6667

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addLocationViewModel =
            ViewModelProviders.of(this).get(AddLocationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_addlocation, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map2) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return root
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        var position = LatLng(latitude, longitude)
        var marker = googleMap?.addMarker(MarkerOptions().position(position).draggable(true).title("Marcador"))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12.0f))

        googleMap?.setOnMapClickListener{ point ->
            latitude = point.latitude
            longitude = point.longitude

            marker?.position = LatLng(latitude, longitude)
        }
    }
}