package j.e.c.com.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleInterview {

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
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("sender")
    @Expose
    private String sender;


    /**
     * No args constructor for use in serialization
     *
     */
    public ScheduleInterview() {
    }

    /**
     *
     * @param date
     * @param jid
     * @param month
     * @param year
     * @param id
     * @param time
     * @param day
     * @param tid
     * @param sid
     * @param status
     */
    public ScheduleInterview(String id, String tid, String sid, String jid, String time, String date, String day, String month, String year, String status,String sender) {
        super();
        this.id = id;
        this.tid = tid;
        this.sid = sid;
        this.jid = jid;
        this.time = time;
        this.date = date;
        this.day = day;
        this.month = month;
        this.year = year;
        this.status = status;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
