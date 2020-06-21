package com.example.geoagenda.ui.editgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geoagenda.R


class EditGroupFragment : Fragment() {
    private lateinit var editGroupViewModel: EditGroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editGroupViewModel =
            ViewModelProviders.of(this).get(EditGroupViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_editgroup, container, false)
        return root
    }
}