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
     * 修改时间
     */
    private String modifyTime;
    /**
     * 印象笔记同步状态
     */
    private int syncState;
    /**
     * 印象笔记中笔记的guid标识码
     */
    private String everGuid;

    /**
     * 无参构造方法
     */
    public NoteBean() {

    }

    protected NoteBean(Parcel in) {
        id = in.readInt();
        content = in.readString();
        pubDate = in.readString();
        modifyTime = in.readString();
        syncState = in.readInt();
        everGuid = in.readString();
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
        dest.writeString(modifyTime);
        dest.writeInt(syncState);
        dest.writeString(everGuid);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEverGuid() {
        return everGuid;
    }

    public void setEverGuid(String everGuid) {
        this.everGuid = everGuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getSyncState() {
        return syncState;
    }

    public void setSyncState(int syncState) {
        this.syncState = syncState;
    }
}
