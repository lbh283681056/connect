package com.base.connect.http.bean;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class BaseResultInfo extends BaseResultWrapper {


    @SerializedName(value = "sign",alternate = {"Sign"})
    public String sign;
    @SerializedName(value = "r",alternate = {"R"})
    public int r;
    @SerializedName(value = "data",alternate = {"Data"})
    public String data;

    public BaseResultInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.sign);
        dest.writeInt(this.r);
        dest.writeString(this.data);
    }

    protected BaseResultInfo(Parcel in) {
        super(in);
        this.sign = in.readString();
        this.r = in.readInt();
        this.data = in.readString();
    }

    public static final Creator<BaseResultInfo> CREATOR = new Creator<BaseResultInfo>() {
        @Override
        public BaseResultInfo createFromParcel(Parcel source) {
            return new BaseResultInfo(source);
        }

        @Override
        public BaseResultInfo[] newArray(int size) {
            return new BaseResultInfo[size];
        }
    };
}
