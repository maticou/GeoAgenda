package com.example.geoagenda.ui.mygroups

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.R
import com.example.geoagenda.ui.deletegroup.GroupD
import com.example.geoagenda.ui.group.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MygroupsFragment : Fragment() {

    private lateinit var mygroupsViewModel: MygroupsViewModel
    private var userReference: DatabaseReference? = null
    private lateinit var auth: FirebaseAuth
     var groupIds: ArrayList<String> = ArrayList()
    lateinit var idGroup: String
    lateinit var arrayAdapter: ArrayAdapter<String>
    lateinit var listView : ListView
    lateinit var nombreGrupo: String
    lateinit var correoAdmin: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mygroupsViewModel =
            ViewModelProviders.of(this).get(MygroupsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mygroups, container, false)


        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        listView = root.findViewById<ListView>(R.id.my_groups_list)
        userReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/"+user?.uid+"/Grupos/")
        val query: Query = userReference!!.orderByChild("id")


        val userListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e(ContentValues.TAG,"Snapshot: "+snapshot)
                if (snapshot.exists()){
                    for (issue in snapshot.getChildren()) {
                        val group = issue.getValue(GroupD::class.java)
                        //Log.e(ContentValues.TAG,"IDs: "+group!!.getAdminId())
                        idGroup = group!!.getId()

                        nombreGrupo = group!!.getName()
                        correoAdmin = group!!.getAdminEmail()
                        groupIds.add(nombreGrupo)

                        Log.e(ContentValues.TAG,"group id"+ idGroup)

                    }
                    arrayAdapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,groupIds)
                    listView.adapter = arrayAdapter
                    arrayAdapter.notifyDataSetChanged()
                    Log.e(TAG,"lista de ids:"+groupIds)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(ContentValues.TAG,"falló!!")
            }
        }
        query.addListenerForSingleValueEvent(userListener)


        return root
    }
}