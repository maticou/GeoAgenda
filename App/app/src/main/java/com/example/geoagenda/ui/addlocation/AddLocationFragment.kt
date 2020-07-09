package com.example.geoagenda.ui.addlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_addlocation.*


class AddLocationFragment : Fragment(), OnMapReadyCallback {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var addLocationViewModel: AddLocationViewModel
    private var latitude = -35.4333
    private var longitude = -71.6667
    private lateinit var saveButton : Button

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

        //datos de la base de datos
        auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/")

        //datos utilizados para generar el id unico de la nueva ubicacion
        val user = auth.currentUser
        var itemId = myRef.child(user?.uid.toString()).push()
        val locationID = itemId.key.toString()

        saveButton = root.findViewById(R.id.save_location) as Button

        saveButton.setOnClickListener(){

            if(location_name.text?.isNotEmpty()!!){
                var locationName = location_name.text.toString()
                var location = Location( locationID, locationName, latitude, longitude)

                myRef.child(user?.uid.toString()).child("Ubicaciones").child(location.id).setValue(location)
                activity?.onBackPressed()
            }
            else{
                nameInputLayout.error = "El nombre no puede estar vacio"
            }
        }

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