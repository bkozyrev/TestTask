package com.bkozyrev.testtask.model;

public class Staff {

    private String mFirstName, mSecondName, mLastName, mGender, mImagePath;
    private int mAge;

    public Staff(String mFirstName, String mSecondName, String mLastName, int mAge, String mGender, String mImagePath) {
        this.mFirstName = mFirstName;
        this.mSecondName = mSecondName;
        this.mLastName = mLastName;
        this.mAge = mAge;
        this.mGender = mGender;
        this.mImagePath = mImagePath;
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
}
