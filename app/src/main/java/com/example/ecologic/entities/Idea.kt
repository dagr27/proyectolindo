package com.example.ecologic.entities

import android.os.Parcel
import android.os.Parcelable

data class Idea(
    val title: String = "N/A",
    val description: String = "N/A",
    val image: String = "N/A",
    val date: String = "N/A",
    val username: String = "N/A"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeString(date)
        parcel.writeString(username)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Idea> {
        override fun createFromParcel(parcel: Parcel): Idea {
            return Idea(parcel)
        }

        override fun newArray(size: Int): Array<Idea?> {
            return arrayOfNulls(size)
        }
    }
}