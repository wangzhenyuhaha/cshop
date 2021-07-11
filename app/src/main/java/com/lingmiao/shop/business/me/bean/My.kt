package com.lingmiao.shop.business.me.bean
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class My(
    @SerializedName("clerk_id")
    var clerkId: Int?=null,
    @SerializedName("email")
    var email: String?=null,
    @SerializedName("face")
    var face: String?=null,
    @SerializedName("founder")
    var founder: Int?=null,
    @SerializedName("founder_text")
    var founderText: String?=null,
    @SerializedName("member_id")
    var memberId: Int?=null,
    @SerializedName("mobile")
    var mobile: String?=null,
    @SerializedName("role")
    var role: String?=null,
    @SerializedName("role_id")
    var roleId: Int?=null,
    @SerializedName("shop_id")
    var shopId: Int?=null,
    @SerializedName("shop_name")
    var shopName: String?=null,//店铺名称
    @SerializedName("uname")
    var uname: String?=null,//账号名称
    @SerializedName("user_state")
    var userState: Int?=null,
    @SerializedName("shop_logo")
    var shopLogo: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(clerkId)
        parcel.writeString(email)
        parcel.writeString(face)
        parcel.writeValue(founder)
        parcel.writeString(founderText)
        parcel.writeValue(memberId)
        parcel.writeString(mobile)
        parcel.writeString(role)
        parcel.writeValue(roleId)
        parcel.writeValue(shopId)
        parcel.writeString(shopName)
        parcel.writeString(uname)
        parcel.writeValue(userState)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<My> {
        override fun createFromParcel(parcel: Parcel): My {
            return My(parcel)
        }

        override fun newArray(size: Int): Array<My?> {
            return arrayOfNulls(size)
        }
    }

}