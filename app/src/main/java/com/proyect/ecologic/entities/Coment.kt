package com.proyect.ecologic.entities

class Coment() {
    lateinit var username: String
    lateinit var coment: String
    lateinit var date: String
    lateinit var idTour: String

    constructor(username: String, coment: String, date: String, idTour: String) : this() {
        this.username = username
        this.coment = coment
        this.date = date
        this.idTour = idTour

    }
}