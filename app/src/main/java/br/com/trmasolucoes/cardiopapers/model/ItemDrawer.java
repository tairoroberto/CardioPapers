package br.com.trmasolucoes.cardiopapers.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tairo on 04/03/17.
 */

public class ItemDrawer implements Parcelable {
    private String title;
    private int icon;
    private boolean showIcon;

    public ItemDrawer() {
    }

    public ItemDrawer(String title, int icon, boolean showIcon) {
        this.title = title;
        this.icon = icon;
        this.showIcon = showIcon;
    }

    protected ItemDrawer(Parcel in) {
        title = in.readString();
        icon = in.readInt();
        showIcon = in.readByte() != 0;
    }

    public static final Creator<ItemDrawer> CREATOR = new Creator<ItemDrawer>() {
        @Override
        public ItemDrawer createFromParcel(Parcel in) {
            return new ItemDrawer(in);
        }

        @Override
        public ItemDrawer[] newArray(int size) {
            return new ItemDrawer[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isShowIcon() {
        return showIcon;
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(icon);
        dest.writeByte((byte) (showIcon ? 1 : 0));
    }
}
