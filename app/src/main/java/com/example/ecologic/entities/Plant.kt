package com.example.ecologic.entities

class Plant() {
    lateinit var name: String
    var level: Int = 0
    var water: Int = 0
    var sun: Int = 0
    var love: Int = 0

    constructor(name: String, level: Int, water: Int, sun: Int, love: Int): this() {
        this.name = name
        this.level = level
        this.water = water
        this.sun = sun
        this.love = love
    }
}