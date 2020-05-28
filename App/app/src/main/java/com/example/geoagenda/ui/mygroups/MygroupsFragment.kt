package com.example.geoagenda.ui.mygroups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.R


class MygroupsFragment : Fragment() {

    private lateinit var mygroupsViewModel: MygroupsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mygroupsViewModel =
            ViewModelProviders.of(this).get(MygroupsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }
}