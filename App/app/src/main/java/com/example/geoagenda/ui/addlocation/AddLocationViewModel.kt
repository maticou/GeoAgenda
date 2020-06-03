package com.example.geoagenda.ui.addlocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class AddLocationViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is add location Fragment"
    }
    val text: LiveData<String> = _text
}