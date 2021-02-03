package j.e.c.com.Models;

public class ContractInfo {
    int id;
    String tid,sid,jid,payment_Image,contract_Image,Contract_Second_Image;

    public ContractInfo(int id, String tid, String sid, String jid, String payment_Image, String contract_Image, String contract_Second_Image) {
        this.id = id;
        this.tid = tid;
        this.sid = sid;
        this.jid = jid;
        this.payment_Image = payment_Image;
        this.contract_Image = contract_Image;
        Contract_Second_Image = contract_Second_Image;
    }

    public ContractInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPayment_Image() {
        return payment_Image;
    }

    public void setPayment_Image(String payment_Image) {
        this.payment_Image = payment_Image;
    }

    public String getContract_Image() {
        return contract_Image;
    }

    public void setContract_Image(String contract_Image) {
        this.contract_Image = contract_Image;
    }

    public String getContract_Second_Image() {
        return Contract_Second_Image;
    }

    public void setContract_Second_Image(String contract_Second_Image) {
        Contract_Second_Image = contract_Second_Image;
    }
}
