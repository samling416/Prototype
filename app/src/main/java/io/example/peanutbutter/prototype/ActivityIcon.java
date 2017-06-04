package io.example.peanutbutter.prototype;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Samuel on 4/06/2017.
 */

public class ActivityIcon implements Parcelable {
    private String mName;
    private int mPhoto;

    ActivityIcon(String iconname, int locicon) {
        mName = iconname;
        mPhoto = locicon;
    }

    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.
    private ActivityIcon(Parcel in) {
        mPhoto = in.readInt();
        mName = in.readString();
    }

    public static final Parcelable.Creator<ActivityIcon> CREATOR = new Parcelable.Creator<ActivityIcon>() {
        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public ActivityIcon createFromParcel(Parcel in) {
            return new ActivityIcon(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public ActivityIcon[] newArray(int size) {
            return new ActivityIcon[size];
        }
    };

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getPhoto() {
        return mPhoto;
    }

    public void setPhoto(int photo) {
        mPhoto = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mPhoto);
    }


}
