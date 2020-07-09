package com.example.geoagenda.ui.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geoagenda.R
import com.example.geoagenda.ui.addlocation.Location
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_locations.*

class LocationsFragment : Fragment() {

    private lateinit var locationsViewModel: LocationsViewModel
    lateinit var addLocationButton: Button
    private lateinit var auth: FirebaseAuth
    private var locationList = ArrayList<Location>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        locationsViewModel =
                ViewModelProviders.of(this).get(LocationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_locations, container, false)

        getLocations()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //println(reminderList.size)
        locationRecyclerView.adapter = LocationViewAdapter(locationList)
        locationRecyclerView.layoutManager = LinearLayoutManager(this.context)
        locationRecyclerView.setHasFixedSize(true)
    }

    private fun getLocations(){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/")

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val rootRef = myRef.child(user?.uid.toString()).child("Ubicaciones")

        rootRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val values = dataSnapshot.children

                locationList.clear()

                values.forEach {
                    val data = it.value as HashMap<String, String>

                    val newLocation = Location(
                        data.get("id").toString(),
                        data.get("nombre").toString(),
                        data.get("latitud") as Double,
                        data.get("longitud") as Double
                    )

                    //println(newReminder)
                    locationList.add(newLocation)
                }

                //esto hay que mejorarlo
                if(locationRecyclerView != null){
                    locationRecyclerView.adapter!!.notifyDataSetChanged()
                }

            }
        })
    }
}
