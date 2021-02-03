package j.e.c.com.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notifications {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("tid")
    @Expose
    private String tid;
    @SerializedName("sid")
    @Expose
    private String sid;
    @SerializedName("jid")
    @Expose
    private String jid;
    @SerializedName("link")
    @Expose
    private String link;

    /**
     * No args constructor for use in serialization
     *
     */
    public Notifications() {
    }

    /**
     *
     * @param jid
     * @param link
     * @param id
     * @param tid
     * @param sid
     */
    public Notifications(String id, String tid, String sid, String jid, String link) {
        super();
        this.id = id;
        this.tid = tid;
        this.sid = sid;
        this.jid = jid;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}