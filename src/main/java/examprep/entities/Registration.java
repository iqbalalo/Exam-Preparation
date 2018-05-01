package examprep.entities;

import com.google.common.base.Objects;
import com.nihonsoftwork.smskitti.util.DateUtil;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Registration implements Comparable<Registration> {

    String id;                  //Transaction id
    String txDate;              //Tranasaction date
    String txAmount;            //Transaction amount
    String phone;               //Phone Number
    String deviceId;            //Moible Device Id
    String exam;                //Exam Id
    String regDate;             //Registration date
    String accessKey;           //Access Key

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTxDate() {
        return txDate;
    }

    public void setTxDate(String txDate) {
        this.txDate = txDate;
    }

    public String getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(String txAmount) {
        this.txAmount = txAmount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("phone", phone)
                .add("exam", exam).toString();
    }

    @Override
    public int compareTo(Registration o) {
        try {
            Long thisId = DateUtil.dateToMilliseconds(txDate);
            Long thatId = DateUtil.dateToMilliseconds(o.txDate);

            if (thisId.equals(thatId)) {
                return 0;
            }
            else if (thisId < thatId) {
                return 1;
            }
            else {
                return -1;
            }
        }
        catch (ParseException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

}
