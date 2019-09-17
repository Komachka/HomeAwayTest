package com.kstor.homeawaytest.view

import android.os.Parcel
import android.os.Parcelable

data class VenuesParcelize(
    var id: String? = null,
    var name: String? = null,
    var categories: List<VenuesCategoryParcelize>? = null,
    var address: String? = null,
    var distance: Int,
    var lat: Double,
    var lng: Double,
    var isFavorite:Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(VenuesCategoryParcelize),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readBoolean()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeTypedList(categories)
        parcel.writeString(address)
        parcel.writeInt(distance)
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
        parcel.writeBoolean(isFavorite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VenuesParcelize> {
        override fun createFromParcel(parcel: Parcel): VenuesParcelize {
            return VenuesParcelize(parcel)
        }

        override fun newArray(size: Int): Array<VenuesParcelize?> {
            return arrayOfNulls(size)
        }
    }
}

data class VenuesCategoryParcelize(
    var id: String?,
    var name: String?,
    val iconPath: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(iconPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VenuesCategoryParcelize> {
        override fun createFromParcel(parcel: Parcel): VenuesCategoryParcelize {
            return VenuesCategoryParcelize(parcel)
        }

        override fun newArray(size: Int): Array<VenuesCategoryParcelize?> {
            return arrayOfNulls(size)
        }
    }
}
