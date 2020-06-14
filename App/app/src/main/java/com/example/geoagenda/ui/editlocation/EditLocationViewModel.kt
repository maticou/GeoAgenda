package com.example.geoagenda.ui.editlocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class EditLocationViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is edit location Fragment"
    }
    val text: LiveData<String> = _text
}