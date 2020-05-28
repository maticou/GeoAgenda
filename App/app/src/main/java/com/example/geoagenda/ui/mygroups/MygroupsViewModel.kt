package com.example.geoagenda.ui.mygroups


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MygroupsViewModel :  ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Mis grupos"
    }
    val text: LiveData<String> = _text
}