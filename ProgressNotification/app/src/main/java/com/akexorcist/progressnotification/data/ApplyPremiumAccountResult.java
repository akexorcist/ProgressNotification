package com.akexorcist.progressnotification.data;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Akexorcist on 8/27/2017 AD.
 */

public class ApplyPremiumAccountResult implements Parcelable {
    private boolean isActive;

    public ApplyPremiumAccountResult(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isActive ? 1 : 0));
    }

    protected ApplyPremiumAccountResult(Parcel in) {
        isActive = in.readByte() != 0;
    }

    public static final Creator<ApplyPremiumAccountResult> CREATOR = new Creator<ApplyPremiumAccountResult>() {
        @Override
        public ApplyPremiumAccountResult createFromParcel(Parcel in) {
            return new ApplyPremiumAccountResult(in);
        }

        @Override
        public ApplyPremiumAccountResult[] newArray(int size) {
            return new ApplyPremiumAccountResult[size];
        }
    };
}
