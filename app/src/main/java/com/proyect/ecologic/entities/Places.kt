package com.proyect.ecologic.entities


class Places {
    lateinit var title:String
    lateinit var desc: String
    var rate: Double = 0.0
    constructor(title: String,desc:String,rate:Double) {
        this.title = title
        this.desc=desc
        this.rate=rate
    }
}