package com.example.geoagenda.ui.editgroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class EditGroupViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is edit group Fragment"
    }
    val text: LiveData<String> = _text
}