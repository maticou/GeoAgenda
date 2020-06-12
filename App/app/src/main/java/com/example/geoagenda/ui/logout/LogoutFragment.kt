package com.example.geoagenda.ui.logout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.LoginActivity
import com.example.geoagenda.MainActivity
import com.example.geoagenda.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_logout.*


class LogoutFragment  : Fragment() {

    private lateinit var logoutViewModel: LogoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logoutViewModel =
            ViewModelProviders.of(this).get(LogoutViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_logout, container, false)
        FirebaseAuth.getInstance().signOut()
        (activity as MainActivity).exit()
        return root
    }
}