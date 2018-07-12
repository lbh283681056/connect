package com.base.connect.http.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BaseResultWrapper implements Parcelable {
    @SerializedName(value = "msg",alternate = {"Msg","Message"})
    public String msg;
    @SerializedName(value = "code",alternate = {"Code"})
    public Integer code;



    public BaseResultWrapper() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.msg);
        dest.writeValue(this.code);
    }

    protected BaseResultWrapper(Parcel in) {
        this.msg = in.readString();
        this.code = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<BaseResultWrapper> CREATOR = new Creator<BaseResultWrapper>() {
        @Override
        public BaseResultWrapper createFromParcel(Parcel source) {
            return new BaseResultWrapper(source);
        }

        @Override
        public BaseResultWrapper[] newArray(int size) {
            return new BaseResultWrapper[size];
        }
    };
}
