package com.example.geoagenda.ui.deletegroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class DeleteGroupViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is delete group Fragment"
    }
    val text: LiveData<String> = _text
}