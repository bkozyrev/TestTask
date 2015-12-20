package com.bkozyrev.testtask.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Staff implements Parcelable{

    private String mFirstName, mSecondName, mLastName, mGender, mImagePath;
    private int mAge, mId;

    public Staff(int mId, String mFirstName, String mSecondName, String mLastName, int mAge, String mGender, String mImagePath) {
        this.mId = mId;
        this.mFirstName = mFirstName;
        this.mSecondName = mSecondName;
        this.mLastName = mLastName;
        this.mAge = mAge;
        this.mGender = mGender;
        this.mImagePath = mImagePath;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getSecondName() {
        return mSecondName;
    }

    public void setSecondName(String mSecondName) {
        this.mSecondName = mSecondName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int mAge) {
        this.mAge = mAge;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String mGender) {
        this.mGender = mGender;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mId);
        parcel.writeString(mFirstName);
        parcel.writeString(mSecondName);
        parcel.writeString(mLastName);
        parcel.writeInt(mAge);
        parcel.writeString(mGender);
        parcel.writeString(mImagePath);
    }

    public Staff(Parcel in) {
        mId = in.readInt();
        mFirstName = in.readString();
        mSecondName = in.readString();
        mLastName = in.readString();
        mAge = in.readInt();
        mGender = in.readString();
        mImagePath = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Staff createFromParcel(Parcel in) {
            return new Staff(in);
        }

        @Override
        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
