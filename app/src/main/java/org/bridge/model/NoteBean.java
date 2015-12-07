package org.bridge.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 实现NoteBean的实体类，提供getter/setter方法，和序列化方法
 */
public class NoteBean implements Parcelable {
    /**
     * 主键id
     */
    private int id;
    /**
     * Note内容
     */
    private String content;
    /**
     * Note的发布日期
     */
    private String pubDate;

    /**
     * 无参构造方法
     */
    public NoteBean() {

    }

    /**
     * 带有参数的构造方法
     *
     * @param id
     * @param content
     * @param pubDate
     */
    public NoteBean(int id, String content, String pubDate) {
        this.id = id;
        this.content = content;
        this.pubDate = pubDate;
    }

    protected NoteBean(Parcel in) {
        id = in.readInt();
        content = in.readString();
        pubDate = in.readString();
    }

    public static final Creator<NoteBean> CREATOR = new Creator<NoteBean>() {
        @Override
        public NoteBean createFromParcel(Parcel in) {
            return new NoteBean(in);
        }

        @Override
        public NoteBean[] newArray(int size) {
            return new NoteBean[size];
        }
    };

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
}