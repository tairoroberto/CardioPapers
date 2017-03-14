package br.com.trmasolucoes.cardiopapers.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post implements Parcelable {
    private long id;
    private int postId;
    private String author;
    private String date;
    private String title;
    private String content;
    private String content_filtered;
    private String status;
    private String guid;
    private int commmentCount;
    private String display_name;
    private String metaImage;
    private String tumbnail;
    private String tumbnailMedium;
    private String tumbnailLarge;
    private String userImage;
    private Date created_at;
    private ArrayList<PostComment> comments;
    private List<Category> categories;

    public Post() {
    }

    public Post(long id, int postId, String author, String date, String title, String content, String content_filtered, String status, String guid, int commmentCount, String display_name, String metaImage, String tumbnail, String tumbnailMedium, String tumbnailLarge, String userImage, Date created_at, ArrayList<PostComment> comments, List<Category> categories) {
        this.id = id;
        this.postId = postId;
        this.author = author;
        this.date = date;
        this.title = title;
        this.content = content;
        this.content_filtered = content_filtered;
        this.status = status;
        this.guid = guid;
        this.commmentCount = commmentCount;
        this.display_name = display_name;
        this.metaImage = metaImage;
        this.tumbnail = tumbnail;
        this.tumbnailMedium = tumbnailMedium;
        this.tumbnailLarge = tumbnailLarge;
        this.userImage = userImage;
        this.created_at = created_at;
        this.comments = comments;
        this.categories = categories;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_filtered() {
        return content_filtered;
    }

    public void setContent_filtered(String content_filtered) {
        this.content_filtered = content_filtered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getCommmentCount() {
        return commmentCount;
    }

    public void setCommmentCount(int commmentCount) {
        this.commmentCount = commmentCount;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getMetaImage() {
        return metaImage;
    }

    public void setMetaImage(String metaImage) {
        this.metaImage = metaImage;
    }

    public String getTumbnail() {
        return tumbnail;
    }

    public void setTumbnail(String tumbnail) {
        this.tumbnail = tumbnail;
    }

    public String getTumbnailMedium() {
        return tumbnailMedium;
    }

    public void setTumbnailMedium(String tumbnailMedium) {
        this.tumbnailMedium = tumbnailMedium;
    }

    public String getTumbnailLarge() {
        return tumbnailLarge;
    }

    public void setTumbnailLarge(String tumbnailLarge) {
        this.tumbnailLarge = tumbnailLarge;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public ArrayList<PostComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<PostComment> comments) {
        this.comments = comments;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    protected Post(Parcel in) {
        id = in.readLong();
        postId = in.readInt();
        author = in.readString();
        date = in.readString();
        title = in.readString();
        content = in.readString();
        content_filtered = in.readString();
        status = in.readString();
        guid = in.readString();
        commmentCount = in.readInt();
        display_name = in.readString();
        metaImage = in.readString();
        tumbnail = in.readString();
        tumbnailMedium = in.readString();
        tumbnailLarge = in.readString();
        userImage = in.readString();
        long tmpDate = in.readLong();
        created_at = tmpDate == -1 ? null : new Date(tmpDate);
        comments = new ArrayList<>();
        in.readTypedList(comments, PostComment.CREATOR);
        categories = new ArrayList<>();
        in.readTypedList(categories, Category.CREATOR);
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(postId);
        dest.writeString(author);
        dest.writeString(date);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(content_filtered);
        dest.writeString(status);
        dest.writeString(guid);
        dest.writeInt(commmentCount);
        dest.writeString(display_name);
        dest.writeString(metaImage);
        dest.writeString(tumbnail);
        dest.writeString(tumbnailMedium);
        dest.writeString(tumbnailLarge);
        dest.writeString(userImage);
        dest.writeLong(created_at != null ? created_at.getTime() : -1);
        dest.writeTypedList(comments);
        dest.writeTypedList(categories);
    }
}
