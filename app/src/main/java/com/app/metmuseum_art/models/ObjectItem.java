package com.app.metmuseum_art.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ObjectItem implements Parcelable {

    int objectID;
    String title;
    String primaryImage;
    String artistName;

    public ObjectItem() {
    }

    public ObjectItem(int objectID, String title, String primaryImage, String artistName) {
        this.objectID = objectID;
        this.title = title;
        this.primaryImage = primaryImage;
        this.artistName = artistName;
    }

    protected ObjectItem(Parcel in) {
        objectID = in.readInt();
        title = in.readString();
        primaryImage = in.readString();
        artistName = in.readString();
    }

    public static final Creator<ObjectItem> CREATOR = new Creator<ObjectItem>() {
        @Override
        public ObjectItem createFromParcel(Parcel in) {
            return new ObjectItem(in);
        }

        @Override
        public ObjectItem[] newArray(int size) {
            return new ObjectItem[size];
        }
    };

    public int getObjectID() {
        return objectID;
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(objectID);
        parcel.writeString(title);
        parcel.writeString(primaryImage);
        parcel.writeString(artistName);
    }
}
