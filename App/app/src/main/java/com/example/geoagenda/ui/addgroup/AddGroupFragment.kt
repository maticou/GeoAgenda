package com.example.geoagenda.ui.addgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.R


class AddGroupFragment : Fragment() {
    private lateinit var addGroupViewModel: AddGroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addGroupViewModel =
            ViewModelProviders.of(this).get(AddGroupViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_addgroup, container, false)
        return root
    }
}