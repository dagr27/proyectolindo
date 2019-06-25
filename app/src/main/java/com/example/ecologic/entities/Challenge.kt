package com.example.ecologic.entities

class Challenge() {
    lateinit var title: String
    var love: Int = 0
    var sun: Int = 0
    var water: Int = 0
    lateinit var type: String

    constructor(title: String, love: Int, sun: Int, water: Int, type: String): this() {
        this.title = title
        this.love = love
        this.sun = sun
        this.water = water

    }
}