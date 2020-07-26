package com.example.geoagenda.ui.addgroup

class User
{

    private var Email: String = ""
    private var UID: String = ""

    constructor()

    constructor( Email:String,UID:String)
    {
        this.Email=Email
        this.UID=UID
    }


    fun getEmail():String
    {
        return Email
    }
    fun getUId():String
    {
        return UID
    }



}