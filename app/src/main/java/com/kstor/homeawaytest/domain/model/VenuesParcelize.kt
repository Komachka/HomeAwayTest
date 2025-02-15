package com.kstor.homeawaytest.domain.model

import android.os.Parcel
import android.os.Parcelable

data class VenuesParcelize(
    var id: String? = null,
    var name: String? = null,
    var categories: VenuesCategoryParcelize? = null,
    var address: String? = null,
    var distance: Int,
    var lat: Double,
    var lng: Double,
    var isFavorite: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(VenuesCategoryParcelize::class.java.classLoader),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeParcelable(categories, flags)
        parcel.writeString(address)
        parcel.writeInt(distance)
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
        parcel.writeByte(if (isFavorite) 1 else 0)
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
