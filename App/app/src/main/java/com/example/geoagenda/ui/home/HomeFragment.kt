package com.example.geoagenda.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geoagenda.MainActivity
import com.example.geoagenda.R
import com.example.geoagenda.ui.reminder.Reminder
import com.example.geoagenda.ui.reminder.ReminderViewAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var auth: FirebaseAuth
    private var reminderList = ArrayList<Reminder>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val addreminder: FloatingActionButton = root.findViewById(R.id.addreminder)
        addreminder.setOnClickListener { view ->
            (activity as MainActivity).addReminder()
        }

        getReminders()

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //println(reminderList.size)
        recycler_view.adapter = ReminderViewAdapter(reminderList)
        recycler_view.layoutManager = LinearLayoutManager(this.context)
        recycler_view.setHasFixedSize(true)
    }

    private fun getReminders(){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/")

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val rootRef = myRef.child(user?.uid.toString()).child("Notas")

        rootRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val values = dataSnapshot.children

                reminderList.clear()

                values.forEach {
                    val data = it.value as HashMap<String, String>

                    val newReminder = Reminder(data.get("id").toString(),
                        data.get("title").toString(),
                        data.get("note").toString(),
                        data.get("recording").toString())

                    //println(newReminder)
                    reminderList.add(newReminder)
                }

                recycler_view.adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}
