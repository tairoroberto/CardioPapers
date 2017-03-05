package br.com.trmasolucoes.cardiopapers.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tairo on 13/04/16.
 */
public class PostComment implements Parcelable {
    private long id;
    private String commentID;
    private String postID;
    private String author;
    private String authorEmail;
    private String date;
    private String content;
    private int parent;

    public PostComment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public PostComment(long id, String commentID, String postID, String author, String authorEmail, String date, String content, int parent) {
        this.id = id;
        this.commentID = commentID;
        this.postID = postID;
        this.author = author;
        this.authorEmail = authorEmail;
        this.date = date;
        this.content = content;
        this.parent = parent;
    }


    protected PostComment(Parcel in) {
        id = in.readLong();
        commentID = in.readString();
        postID = in.readString();
        author = in.readString();
        authorEmail = in.readString();
        date = in.readString();
        content = in.readString();
        parent = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(commentID);
        dest.writeString(postID);
        dest.writeString(author);
        dest.writeString(authorEmail);
        dest.writeString(date);
        dest.writeString(content);
        dest.writeInt(parent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostComment> CREATOR = new Creator<PostComment>() {
        @Override
        public PostComment createFromParcel(Parcel in) {
            return new PostComment(in);
        }

        @Override
        public PostComment[] newArray(int size) {
            return new PostComment[size];
        }
    };
}
