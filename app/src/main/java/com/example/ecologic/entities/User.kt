package com.example.ecologic.entities

class User(){
    lateinit var username:String
    lateinit var name: String
    lateinit var lastName: String
    lateinit var password: String
    lateinit var email: String
    lateinit var profilePicture: String
    var status: Int = 0
    lateinit var lastDate: String
    var type: Int = 0

    constructor(username:String, name: String, lastName: String, password: String, email: String, profilePicture: String, status: Int, lastDate: String, type: Int): this() {
        this.username = username
        this.name = name
        this.lastName = lastName
        this.password = password
        this.email = email
        this.profilePicture = profilePicture
        this.status = status
        this.lastDate = lastDate
        this.type = type
    }
}