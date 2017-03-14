package br.com.trmasolucoes.cardiopapers.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tairo on 13/03/17.
 */

public class Category implements Parcelable {
    long id;
    long postId;
    String name;

    public Category() {
    }

    public Category(long id, long postId, String name) {
        this.id = id;
        this.postId = postId;
        this.name = name;
    }

    protected Category(Parcel in) {
        id = in.readLong();
        postId = in.readLong();
        name = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(postId);
        dest.writeString(name);
    }
}
