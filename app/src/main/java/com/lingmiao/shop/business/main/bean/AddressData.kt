package com.lingmiao.shop.business.main.bean

import android.os.Parcel
import android.os.Parcelable
import com.amap.api.maps.model.LatLng


/**
Create Date : 2021/4/211:14 PM
Auther      : Fox
Desc        :
 **/
class AddressData() : Parcelable {
    var latLng: LatLng? = null
    var province: String? = null
    var city: String? = null
    var district: String? = null
    var address: String? = null

    constructor(parcel: Parcel) : this() {
        latLng = parcel.readParcelable(LatLng::class.java.classLoader)
        province = parcel.readString()
        city = parcel.readString()
        district = parcel.readString()
        address = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(latLng, flags)
        parcel.writeString(province)
        parcel.writeString(city)
        parcel.writeString(district)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddressData> {
        override fun createFromParcel(parcel: Parcel): AddressData {
            return AddressData(parcel)
        }

        override fun newArray(size: Int): Array<AddressData?> {
            return arrayOfNulls(size)
        }
    }
}