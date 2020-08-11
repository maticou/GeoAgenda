package com.example.geoagenda.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geoagenda.AddReminderActivity
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

class HomeFragment : Fragment(), ReminderViewAdapter.OnReminderItemClickListener {
    var cate:String = "St"
    companion object {
        fun newInstance(cident: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("categ", cident)
            fragment.arguments = args
            fragment.cate = cident
            Log.d("categoria2",fragment.cate)
            return fragment
        }
    }

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
        recycler_view.adapter = ReminderViewAdapter(reminderList, this)
        recycler_view.layoutManager = LinearLayoutManager(this.context)
        recycler_view.setHasFixedSize(true)
    }

    override fun onItemClick(reminders: Reminder, position: Int) {
        val intent = Intent(context, AddReminderActivity::class.java)
        intent.putExtra("ACTIVITY_TITLE", "Editar Recordatorio")
        intent.putExtra("REMINDER_ID", reminders.id)
        intent.putExtra("REMINDER_TITLE", reminders.title)
        intent.putExtra("REMINDER_NOTE", reminders.note)
        intent.putExtra("REMINDER_AUDIO", reminders.recording)
        intent.putExtra("REMINDER_IMAGE", reminders.image)
        intent.putExtra("REMINDER_DAY", reminders.day)
        intent.putExtra("REMINDER_MONTH", reminders.month)
        intent.putExtra("REMINDER_YEAR", reminders.year)
        intent.putExtra("REMINDER_HOUR", reminders.hour)
        intent.putExtra("REMINDER_MINUTE", reminders.minute)
        intent.putExtra("REMINDER_LOCATION", reminders.location)
        startActivity(intent)
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
                Log.d("categoria3",cate)
                reminderList.clear()

                values.forEach {
                    val data = it.value as HashMap<String,String>
                    /*val newReminder = Reminder(
                        data.get("id").toString(),
                        data.get("tittle").toString(),
                        data.get("note").toString(),
                        data.get("recording").toString(),
                        data.get("image").toString(),
                        data.get("category").toString())
                    reminderList.add(newReminder)*/
                    if (cate == "St"){
                        val newReminder = Reminder(
                            data.get("id").toString(),
                            data.get("title").toString(),
                            data.get("note").toString(),
                            data.get("recording").toString(),
                            data.get("location").toString(),
                            data.get("image").toString(),
                            data.get("day").toString(),
                            data.get("month").toString(),
                            data.get("year").toString(),
                            data.get("hour").toString(),
                            data.get("minute").toString(),
                            data.get("category").toString())
                        reminderList.add(newReminder)
                    }
                    else{
                        if (data.get("category")==cate){
                            val newReminder = Reminder(
                                data.get("id").toString(),
                                data.get("title").toString(),
                                data.get("note").toString(),
                                data.get("recording").toString(),
                                data.get("location").toString(),
                                data.get("image").toString(),
                                data.get("day").toString(),
                                data.get("month").toString(),
                                data.get("year").toString(),
                                data.get("hour").toString(),
                                data.get("minute").toString(),
                                data.get("category").toString())
                            reminderList.add(newReminder)
                        }
                    }

                    //println(newReminder)


                }

                recycler_view.adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }


}
