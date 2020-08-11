package com.example.geoagenda.ui.joingroup

class GroupF {
    private var id: String = ""
    private var name: String = ""

    constructor()

    constructor(
        id: String,
        name: String
    ) {
        this.id = id
        this.name = name}

    fun getId(): String {
        return id
    }

    fun getName(): String {
        return name
    }



}