package org.bridge.entry;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bridgegeorge on 15/11/14.
 */
public class NoteEntry implements Parcelable{
    private int id;
    private String content;
    private String pubDate;

    public NoteEntry(int id, String content, String pubDate) {
        this.id = id;
        this.content = content;
        this.pubDate = pubDate;
    }

    protected NoteEntry(Parcel in) {
        id = in.readInt();
        content = in.readString();
        pubDate = in.readString();
    }

    public static final Creator<NoteEntry> CREATOR = new Creator<NoteEntry>() {
        @Override
        public NoteEntry createFromParcel(Parcel in) {
            return new NoteEntry(in);
        }

        @Override
        public NoteEntry[] newArray(int size) {
            return new NoteEntry[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeString(pubDate);
    }
}
