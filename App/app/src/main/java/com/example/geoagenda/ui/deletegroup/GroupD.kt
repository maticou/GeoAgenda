package com.example.geoagenda.ui.deletegroup

class GroupD {
    private var id: String = ""
    private var name: String = ""
    private var description: String = ""
    private var adminId: String = ""
    private var image: String = ""
    private var adminEmail: String = ""
    private var Miembros: ArrayList<Miembro> = ArrayList()

    constructor()

    constructor(
        id: String,
        name: String,
        description: String,
        adminId: String,
        image: String,
        adminEmail: String,
        Miembros: ArrayList<Miembro>
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.adminId = adminId
        this.image = image
        this.adminEmail = adminEmail
        this.Miembros = Miembros
    }

    fun getId(): String {
        return id
    }

    fun getName(): String {
        return name
    }

    fun getDescripcion(): String {
        return description
    }

    fun getAdminId(): String {
        return adminId
    }

    fun getImage(): String {
        return image
    }

    fun getAdminEmail(): String {
        return adminEmail
    }

    fun getMiembrosList (): ArrayList<Miembro>{
        return Miembros
    }
}