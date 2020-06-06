package com.akexorcist.progressnotification.data

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Akexorcist on 8/27/2017 AD.
 */

class ApplyPremiumAccountResult(var isActive: Boolean = false) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isActive) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApplyPremiumAccountResult> {
        override fun createFromParcel(parcel: Parcel): ApplyPremiumAccountResult {
            return ApplyPremiumAccountResult(parcel)
        }

        override fun newArray(size: Int): Array<ApplyPremiumAccountResult?> {
            return arrayOfNulls(size)
        }
    }
}