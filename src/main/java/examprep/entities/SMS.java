package examprep.entities;

public class SMS {

    String smsTime;
    String phoneNumber;
    String message;
    String inoutStatus;

    public SMS() {
    }

    public String getSmsTime() {
        return smsTime;
    }

    public void setSmsTime(String smsTime) {
        this.smsTime = smsTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInoutStatus() {
        return inoutStatus;
    }

    public void setInoutStatus(String inoutStatus) {
        this.inoutStatus = inoutStatus;
    }
}
