package j.e.c.com.Models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Teacher {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("unique_id")
    @Expose
    private String uniqueId;
    @SerializedName("agent")
    @Expose
    private String agent;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("wechatid")
    @Expose
    private String wechatid;
    @SerializedName("pic")
    @Expose
    private String pic;
    @SerializedName("bpic")
    @Expose
    private String bpic;
    @SerializedName("cvpic")
    @Expose
    private String cvpic;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tid")
    @Expose
    private String tid;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("salary")
    @Expose
    private String salary;
    @SerializedName("education")
    @Expose
    private String education;
    @SerializedName("graduation")
    @Expose
    private String graduation;
    @SerializedName("jobtitle")
    @Expose
    private String jobtitle;
    @SerializedName("visa")
    @Expose
    private String visa;
    @SerializedName("visatype")
    @Expose
    private String visatype;
    @SerializedName("QFWV")
    @Expose
    private String qFWV;
    @SerializedName("workplace")
    @Expose
    private String workplace;
    @SerializedName("fromchina")
    @Expose
    private String fromchina;
    @SerializedName("yearinchina")
    @Expose
    private String yearinchina;
    @SerializedName("workexperince")
    @Expose
    private String workexperince;
    @SerializedName("joindate")
    @Expose
    private String joindate;
    @SerializedName("currentworkplace")
    @Expose
    private String currentworkplace;

    /**
     * No args constructor for use in serialization
     *
     */
    public Teacher() {
    }

    public Teacher(Teacher teacher){
        this.id = teacher.id;
        this.uniqueId = teacher.uniqueId;
        this.agent = teacher.agent;
        this.phone = teacher.phone;
        this.wechatid = teacher.wechatid;
        this.pic = teacher.pic;
        this.bpic = teacher.bpic;
        this.cvpic = teacher.cvpic;
        this.status = teacher.status;
        this.tid = teacher.tid;
        this.video = teacher.video;
        this.name = teacher.name;
        this.age = teacher.age;
        this.nationality = teacher.nationality;
        this.country = teacher.country;
        this.gender = teacher.gender;
        this.salary = teacher.salary;
        this.education = teacher.education;
        this.graduation = teacher.graduation;
        this.jobtitle = teacher.jobtitle;
        this.visa = teacher.visa;
        this.visatype = teacher.visatype;
        this.qFWV = teacher.qFWV;
        this.workplace = teacher.workplace;
        this.fromchina = teacher.fromchina;
        this.yearinchina = teacher.yearinchina;
        this.workexperince = teacher.workexperince;
        this.joindate = teacher.joindate;
        this.currentworkplace = teacher.currentworkplace;
    }

    /**
     *
     * @param country
     * @param agent
     * @param education
     * @param gender
     * @param bpic
     * @param wehchatid
     * @param pic
     * @param video
     * @param salary
     * @param tid
     * @param graduation
     * @param id
     * @param cvpic
     * @param qFWV
     * @param joindate
     * @param jobtitle
     * @param currentworkplace
     * @param fromchina
     * @param workexperince
     * @param nationality
     * @param phone
     * @param visa
     * @param name
     * @param visatype
     * @param yearinchina
     * @param workplace
     * @param uniqueId
     * @param age
     * @param status
     */
    public Teacher(String id, String uniqueId, String agent, String phone, String wehchatid, String pic, String bpic, String cvpic, String status, String tid, String video, String name, String age, String nationality, String country, String gender, String salary, String education, String graduation, String jobtitle, String visa, String visatype, String qFWV, String workplace, String fromchina, String yearinchina, String workexperince, String joindate, String currentworkplace) {
        super();
        this.id = id;
        this.uniqueId = uniqueId;
        this.agent = agent;
        this.phone = phone;
        this.wechatid = wehchatid;
        this.pic = pic;
        this.bpic = bpic;
        this.cvpic = cvpic;
        this.status = status;
        this.tid = tid;
        this.video = video;
        this.name = name;
        this.age = age;
        this.nationality = nationality;
        this.country = country;
        this.gender = gender;
        this.salary = salary;
        this.education = education;
        this.graduation = graduation;
        this.jobtitle = jobtitle;
        this.visa = visa;
        this.visatype = visatype;
        this.qFWV = qFWV;
        this.workplace = workplace;
        this.fromchina = fromchina;
        this.yearinchina = yearinchina;
        this.workexperince = workexperince;
        this.joindate = joindate;
        this.currentworkplace = currentworkplace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechatid() {
        return wechatid;
    }

    public void setWechatid(String wechatid) {
        this.wechatid = wechatid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getBpic() {
        return bpic;
    }

    public void setBpic(String bpic) {
        this.bpic = bpic;
    }

    public String getCvpic() {
        return cvpic;
    }

    public void setCvpic(String cvpic) {
        this.cvpic = cvpic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getGraduation() {
        return graduation;
    }

    public void setGraduation(String graduation) {
        this.graduation = graduation;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getVisa() {
        return visa;
    }

    public void setVisa(String visa) {
        this.visa = visa;
    }

    public String getVisatype() {
        return visatype;
    }

    public void setVisatype(String visatype) {
        this.visatype = visatype;
    }

    public String getQFWV() {
        return qFWV;
    }

    public void setQFWV(String qFWV) {
        this.qFWV = qFWV;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getFromchina() {
        return fromchina;
    }

    public void setFromchina(String fromchina) {
        this.fromchina = fromchina;
    }

    public String getYearinchina() {
        return yearinchina;
    }

    public void setYearinchina(String yearinchina) {
        this.yearinchina = yearinchina;
    }

    public String getWorkexperince() {
        return workexperince;
    }

    public void setWorkexperince(String workexperince) {
        this.workexperince = workexperince;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public String getCurrentworkplace() {
        return currentworkplace;
    }

    public void setCurrentworkplace(String currentworkplace) {
        this.currentworkplace = currentworkplace;
    }

}
