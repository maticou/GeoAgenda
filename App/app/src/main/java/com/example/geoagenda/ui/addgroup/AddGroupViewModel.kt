package com.example.geoagenda.ui.addgroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class AddGroupViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is add group Fragment"
    }
    val text: LiveData<String> = _text
}