package j.e.c.com.Models;




import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class School {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("agentReference")
    @Expose
    private String agentReference;
    @SerializedName("schoolLocation")
    @Expose
    private String schoolLocation;
    @SerializedName("ApplicantName")
    @Expose
    private String applicantName;
    @SerializedName("postionInSchool")
    @Expose
    private String postionInSchool;
    @SerializedName("ContactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("weChatId")
    @Expose
    private String weChatId;
    @SerializedName("licenecePicture")
    @Expose
    private String licenecePicture;
    @SerializedName("licenseLiveImage")
    @Expose
    private String licenseLiveImage;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("jobTitle")
    @Expose
    private String jobTitle;
    @SerializedName("workTime")
    @Expose
    private String workTime;
    @SerializedName("kidsAge")
    @Expose
    private String kidsAge;
    @SerializedName("KPMUT")
    @Expose
    private String kPMUT;
    @SerializedName("ArrivalDate")
    @Expose
    private String arrivalDate;
    @SerializedName("Demand")
    @Expose
    private String demand;
    @SerializedName("numberOfTeaches")
    @Expose
    private String numberOfTeaches;
    @SerializedName("requimentsscholl")
    @Expose
    private String requimentsscholl;
    @SerializedName("salary")
    @Expose
    private String salary;
    @SerializedName("housing")
    @Expose
    private String housing;
    @SerializedName("advantage")
    @Expose
    private String advantage;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("stid")
    @Expose
    private String stid;
    /**
     * No args constructor for use in serialization
     *
     */
    public School() {
    }

    /**
     *
     * @param city
     * @param housing
     * @param advantage
     * @param jobTitle
     * @param licenseLiveImage
     * @param schoolLocation
     * @param applicantName
     * @param kidsAge
     * @param salary
     * @param workTime
     * @param demand
     * @param kPMUT
     * @param arrivalDate
     * @param agentReference
     * @param contactNumber
     * @param requimentsscholl
     * @param licenecePicture
     * @param id
     * @param postionInSchool
     * @param weChatId
     * @param numberOfTeaches
     * @param status
     */
    public School(String id,String agentReference, String schoolLocation, String applicantName, String postionInSchool, String contactNumber, String weChatId, String licenecePicture, String licenseLiveImage, String city, String jobTitle, String workTime, String kidsAge, String kPMUT, String arrivalDate, String demand, String numberOfTeaches, String requimentsscholl, String salary, String housing, String advantage, String status, String stid) {
        super();
        this.id = id;
        this.stid= stid;
        this.agentReference = agentReference;
        this.schoolLocation = schoolLocation;
        this.applicantName = applicantName;
        this.postionInSchool = postionInSchool;
        this.contactNumber = contactNumber;
        this.weChatId = weChatId;
        this.licenecePicture = licenecePicture;
        this.licenseLiveImage = licenseLiveImage;
        this.city = city;
        this.jobTitle = jobTitle;
        this.workTime = workTime;
        this.kidsAge = kidsAge;
        this.kPMUT = kPMUT;
        this.arrivalDate = arrivalDate;
        this.demand = demand;
        this.numberOfTeaches = numberOfTeaches;
        this.requimentsscholl = requimentsscholl;
        this.salary = salary;
        this.housing = housing;
        this.advantage = advantage;
        this.status = status;
    }

    public School(School school){
        this.id = school.getId();

        this.agentReference = school.getAgentReference();
        this.schoolLocation = school.getSchoolLocation();
        this.applicantName = school.getApplicantName();
        this.postionInSchool = school.getPostionInSchool();
        this.contactNumber = school.getContactNumber();
        this.weChatId = school.getWeChatId();
        this.licenecePicture = school.getLicenecePicture();
        this.licenseLiveImage = school.getLicenseLiveImage();
        this.city = school.getCity();
        this.jobTitle = school.getJobTitle();
        this.workTime = school.getWorkTime();
        this.kidsAge = school.getKidsAge();
        this.kPMUT = school.getKPMUT();
        this.arrivalDate = school.getArrivalDate();
        this.demand = school.getDemand();
        this.numberOfTeaches = school.getNumberOfTeaches();
        this.requimentsscholl = school.getRequimentsscholl();
        this.salary = school.getSalary();
        this.housing = school.getHousing();
        this.advantage = school.getAdvantage();
        this.status = school.getStatus();
        this.stid= school.getStid();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgentReference() {
        return agentReference;
    }

    public void setAgentReference(String agentReference) {
        this.agentReference = agentReference;
    }

    public String getSchoolLocation() {
        return schoolLocation;
    }

    public void setSchoolLocation(String schoolLocation) {
        this.schoolLocation = schoolLocation;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getPostionInSchool() {
        return postionInSchool;
    }

    public void setPostionInSchool(String postionInSchool) {
        this.postionInSchool = postionInSchool;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getWeChatId() {
        return weChatId;
    }

    public void setWeChatId(String weChatId) {
        this.weChatId = weChatId;
    }

    public String getLicenecePicture() {
        return licenecePicture;
    }

    public void setLicenecePicture(String licenecePicture) {
        this.licenecePicture = licenecePicture;
    }

    public String getLicenseLiveImage() {
        return licenseLiveImage;
    }

    public void setLicenseLiveImage(String licenseLiveImage) {
        this.licenseLiveImage = licenseLiveImage;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getKidsAge() {
        return kidsAge;
    }

    public void setKidsAge(String kidsAge) {
        this.kidsAge = kidsAge;
    }

    public String getKPMUT() {
        return kPMUT;
    }

    public void setKPMUT(String kPMUT) {
        this.kPMUT = kPMUT;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public String getNumberOfTeaches() {
        return numberOfTeaches;
    }

    public void setNumberOfTeaches(String numberOfTeaches) {
        this.numberOfTeaches = numberOfTeaches;
    }

    public String getRequimentsscholl() {
        return requimentsscholl;
    }

    public void setRequimentsscholl(String requimentsscholl) {
        this.requimentsscholl = requimentsscholl;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getHousing() {
        return housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }

    public String getAdvantage() {
        return advantage;
    }

    public void setAdvantage(String advantage) {
        this.advantage = advantage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStid() {
        return stid;
    }

    public void setStid(String stid) {
        this.stid = stid;
    }
}