package org.bridge.entry;

/**
 * Created by bridgegeorge on 15/11/14.
 */
public class NoteEntry {
    private int id;
    private String content;
    private String pubDate;

    public NoteEntry(int id, String content, String pubDate) {
        this.id = id;
        this.content = content;
        this.pubDate = pubDate;
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
