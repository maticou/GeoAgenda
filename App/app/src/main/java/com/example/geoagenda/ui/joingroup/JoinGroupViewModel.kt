package com.example.geoagenda.ui.joingroup


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class JoinGroupViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is add location Fragment"
    }
    val text: LiveData<String> = _text
}