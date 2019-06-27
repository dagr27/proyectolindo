package com.proyect.ecologic.entities

class User(){
    lateinit var username:String
    lateinit var name: String
    lateinit var lastname: String
    lateinit var password: String
    lateinit var profilePicture: String
    var status: Int = 0
    lateinit var lastdate: String
    var type: Int = 0
    lateinit var email : String

    constructor(username:String, name: String, lastname: String, password: String, profilePicture: String, status: Int, lastDate: String, type: Int, email:String): this() {
        this.username = username
        this.name = name
        this.lastname = lastname
        this.password = password
        this.profilePicture = profilePicture
        this.status = status
        this.lastdate = lastDate
        this.type = type
        this.email = email
    }
}