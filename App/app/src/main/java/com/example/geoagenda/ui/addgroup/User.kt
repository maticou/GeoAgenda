package com.example.geoagenda.ui.addgroup

class User
{
    private var avatar: String = ""
    private var email: String = ""
    private var id: String = ""
    private var provider: String = ""
    private var username: String = ""

    constructor()

    constructor(avatar:String, email:String,id:String,provider:String,username:String)
    {
        this.avatar=avatar
        this.email=email
        this.id=id
        this.provider=provider
        this.username=username
    }

    fun getAvatar():String
    {
        return avatar
    }
    fun getEmail():String
    {
        return email
    }
    fun getId():String
    {
        return id
    }
    fun getProvider():String
    {
        return provider
    }
    fun getUsername():String
{
    return username
}



}