package com.example.geoagenda.ui.deletegroup

class Miembro {
    private var id: String = ""
    private var email: String = ""

    constructor()

    constructor(
        id: String,
        email: String
    ) {
        this.id = id
        this.email = email
    }

    fun getId(): String {
        return id
    }

    fun getEmail(): String {
        return email
    }

}