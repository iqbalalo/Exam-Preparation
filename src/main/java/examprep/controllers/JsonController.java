package examprep.controllers;

import com.google.gson.Gson;
import com.nihonsoftwork.smskitti.util.DateUtil;
import examprep.entities.Exam;
import examprep.entities.HttpResponse;
import examprep.entities.MCQ;
import examprep.entities.Registration;
import examprep.entities.SMS;
import examprep.entities.User;
import examprep.utils.RandomString;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Scope("request")
public class JsonController extends BaseController {

    static final Logger logger = Logger.getLogger(JsonController.class.getName());

    List<MCQ> mcqList = new ArrayList<>();

    @RequestMapping(value = {"/exam/list/json"})
    public @ResponseBody
    HttpResponse examList(@RequestParam("key") String key) {
        logger.info("start");

        if (key == null) {
            logger.info("parameter is null");
            return null;
        }

        if ("*private*".equals(key)) {
            try {
                List<Exam> examList = examDao.findAll();
                return new HttpResponse(HttpStatus.OK, new Gson().toJson(examList));
            }
            catch (Exception ex) {
                return new HttpResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
            }
        }
        return new HttpResponse(HttpStatus.BAD_REQUEST, "Bad Request");

    }

    @RequestMapping(value = {"/exam/{examId}/json"})
    public @ResponseBody
    HttpResponse examById(@RequestParam("key") String key, @PathVariable("examId") String examId) {
        logger.info("start");

        if (key == null) {
            logger.info("parameter is null");
            return null;
        }

        if ("*private*".equals(key)) {
            try {
                Exam exam = examDao.findById(examId);
                return new HttpResponse(HttpStatus.OK, new Gson().toJson(exam));
            }
            catch (Exception ex) {
                return new HttpResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
            }
        }
        return new HttpResponse(HttpStatus.BAD_REQUEST, "Bad Request");

    }

    @RequestMapping(value = {"/mcq/list/json"})
    public @ResponseBody
    HttpResponse MCQsInJson(@RequestParam("key") String key,
                            @RequestParam("deviceId") String deviceId,
                            @RequestParam("subject") String subject) {
        logger.info("start");

        if (key == null || deviceId == null || subject == null) {
            logger.info("parameter is null");
            return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid Parameter!");
        }

        try {

            logger.info("check whether key is valid");
            Registration registration = regDao.findByAccessKey(key);

            if (registration == null) {
                logger.log(Level.INFO, "{0} is invalid!", key);

                return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid Registration!");
            }

            if (!registration.getDeviceId().equals(deviceId)) {

                logger.log(Level.INFO, "{0} is invalid!", deviceId);

                return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid Device Access!");
            }

            logger.log(Level.INFO, "{0} is valid!", key);

            //mcqList = mcqDao.findAllByExam(registration.getExam());
            mcqList = mcqDao.findAll(registration.getExam(), subject);

            logger.log(Level.INFO, "mcq list size is {0}", mcqList.size());

            return new HttpResponse(HttpStatus.OK, new Gson().toJson(mcqList));
        }
        catch (Exception e) {
            logger.severe(e.getMessage());
            logger.info("finish");

            return new HttpResponse(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }

    }

    @RequestMapping(value = {"/mcq/list/free/json"})
    public @ResponseBody
    HttpResponse FreeMCQsInJson(@RequestParam("key") String key,
                                @RequestParam("exam") String exam,
                                @RequestParam("subject") String subject) {
        logger.info("start");

        if (key == null || exam == null || subject == null) {
            logger.info("parameter is null");
            return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid Parameter!");
        }

        if (!key.equals("Free")) {
            logger.info("key is not free");
            return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid Parameter!");
        }

        try {

            //mcqList = mcqDao.findAllByExam(registration.getExam());
            mcqList = mcqDao.findAll(exam, subject);

            logger.log(Level.INFO, "mcq list size is {0}", mcqList.size());

            return new HttpResponse(HttpStatus.OK, new Gson().toJson(mcqList));
        }
        catch (Exception e) {
            logger.severe(e.getMessage());
            logger.info("finish");

            return new HttpResponse(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }

    }

    @RequestMapping(value = "/registration/new", method = RequestMethod.GET)
    public @ResponseBody
    HttpResponse registrationRequest(
            @RequestParam("txId") String transactionId,
            @RequestParam("deviceId") String deviceId,
            @RequestParam("phone") String phone,
            @RequestParam("exam") String exam) {

        logger.info("start");

        if (transactionId == null || phone == null || transactionId.trim().isEmpty()
                || deviceId == null || deviceId.trim().isEmpty()
                || exam == null || exam.trim().isEmpty()) {

            logger.info("parameter missing. wrong request");
            return new HttpResponse(HttpStatus.BAD_REQUEST, "Bad request!");
        }

        try {

            logger.info("check registration request");

            Registration registration = regDao.findById(transactionId);

            if (registration != null) {
                logger.info("valid transaction found!");

                //New registration
                if ((registration.getDeviceId() == null && registration.getPhone() == null) || (registration.getPhone().equals(phone) && registration.getDeviceId() == null)) {
                    logger.info("device is not registered yet");
                    logger.info("create registration");

                    registration.setPhone(phone);
                    registration.setDeviceId(deviceId);
                    registration.setExam(exam);
                    registration.setRegDate(DateUtil.currentDateString());
                    registration.setAccessKey(RandomString.getRandomString(10) + new Date().getTime() + RandomString.getRandomString(5));

                    regDao.create(registration);
                    logger.info("new registration created");

                    return new HttpResponse(HttpStatus.CREATED, new Gson().toJson(registration));
                }

                //Is user already registered
                if (registration.getDeviceId().equals(deviceId) && registration.getPhone().equals(phone)) {
                    logger.info("registration found");

                    logger.info("check request for exam");
                    //Check requested exam
                    if (registration.getExam().equals(exam)) {
                        logger.info("requested exam matched");
                        return new HttpResponse(HttpStatus.ACCEPTED, new Gson().toJson(registration));
                    }
                    else {
                        logger.info("requested exam not matched");
                        return new HttpResponse(HttpStatus.FORBIDDEN, "Already registered for another exam!");
                    }
                }
                else {
                    logger.info("some other device already registered!");
                    return new HttpResponse(HttpStatus.LOCKED, "Invalid Transaction ID!");
                }
            }
            else {
                logger.info("no valid transaction found!");
                return new HttpResponse(HttpStatus.NOT_FOUND, "Invalid Transaction ID!");
            }

        }
        catch (Exception e) {
            logger.log(Level.INFO, "error: {0}", e.getMessage());
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/signup_from_device", method = RequestMethod.POST)
    public @ResponseBody
    HttpResponse signUp(@RequestParam("user") String u) {
        try {
            logger.info("start");

            if (u == null) {
                logger.info("User param is null");
                return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid User Inforamtion");
            }

            User user = new Gson().fromJson(u, User.class);

            //Duplicate user check
            if (userDao.findById(user.getId()) != null) {
                logger.info("Duplicate signup");
                List<Exam> examList = examDao.findAll();
                return new HttpResponse(HttpStatus.OK, new Gson().toJson(examList));
            }

            userDao.create(user);
            logger.log(Level.INFO, "User created: {0}", user.getId());

            List<Exam> examList = examDao.findAll();
            //Return exam list on successful signup
            return new HttpResponse(HttpStatus.OK, new Gson().toJson(examList));

        }
        catch (Exception ex) {
            logger.log(Level.INFO, "Error: {0}", ex.getMessage());
            return new HttpResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }
    }

    @RequestMapping(value = "/payment/new", method = RequestMethod.POST)
    public @ResponseBody
    HttpResponse newPaymentSMS(@RequestParam("smsStr") String smsStr) {
        logger.info("start");

        if (smsStr == null || "".equals(smsStr)) {
            logger.info("sms param is empty");
            return new HttpResponse(HttpStatus.BAD_REQUEST, "No valid SMS");
        }

        SMS sms = new Gson().fromJson(smsStr, SMS.class);

        if (sms == null) {
            logger.info("sms is empty");
            return new HttpResponse(HttpStatus.BAD_REQUEST, "No SMS Found");
        }

        //bKash use "TrxID" and DBBL user "TnxId" into their sms format
        //bKash sender number is bKash and DBBL sender number is 16216
        if (sms.getMessage().contains("TrxID") || sms.getPhoneNumber().equals("bKash")) {
            logger.info("sms from bKash");
            String trxId = extractTrxId(sms.getMessage());

            if (trxId == null || "".equals(trxId)) {
                logger.info("trxId not found");
                return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid SMS");
            }
            else {
                trxId = "bKash-" + trxId;
                logger.log(Level.INFO, "trxId {0}", trxId);
            }

            String amount = extractTrxAmount(sms.getMessage());

            if (amount == null || "".equals(amount)) {
                logger.info("amount not found");
                return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid SMS");
            }
            else if (Double.parseDouble(amount) == 50.00) {
                logger.log(Level.INFO, "amount {0}", Double.parseDouble(amount));
                String smsTime = DateUtil.dateTimeToString(DateUtil.millisecondsToDate(Long.parseLong(sms.getSmsTime())));
                return newPayment(trxId, smsTime, amount);
            }
            else {
                logger.log(Level.INFO, "amount {0}", Double.parseDouble(amount));
                return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid Amount");
            }

        }
        else if (sms.getMessage().contains("TxnId") || sms.getPhoneNumber().equals("16216")) {

            logger.info("sms from DBBL");
            String trxId = extractTxnId(sms.getMessage());

            if (trxId == null || "".equals(trxId)) {
                logger.info("trxId not found");
                return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid SMS");
            }
            else {
                trxId = "DBBL-" + trxId;
                logger.log(Level.INFO, "trxId {0}", trxId);
            }

            String amount = extractTxnAmount(sms.getMessage());

            if (amount == null || "".equals(amount)) {
                logger.info("amount not found");
                return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid SMS");
            }
            else if (Double.parseDouble(amount) == 50.00) {
                logger.log(Level.INFO, "amount {0}", Double.parseDouble(amount));
                String smsTime = DateUtil.dateTimeToString(DateUtil.millisecondsToDate(Long.parseLong(sms.getSmsTime())));
                return newPayment(trxId, smsTime, amount);
            }
            else {
                logger.log(Level.INFO, "amount {0}", Double.parseDouble(amount));
                return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid Amount");
            }

        }

        logger.info("invalid SMS");
        return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid SMS");
    }

    private String extractTrxId(String s) {
        Matcher m = Pattern.compile("TrxID \\d+").matcher(s);
        String result = "";
        if (m.find()) {
            String trx = m.group();

            logger.info(trx);

            m = Pattern.compile("\\d+").matcher(trx);
            if (m.find()) {
                result = m.group();
                logger.info(result);
            }
        }
        return result;
    }

    private String extractTxnId(String s) {
        Matcher m = Pattern.compile("TxnId: \\d+;").matcher(s);
        String result = "";
        if (m.find()) {
            String trx = m.group();

            logger.info(trx);

            m = Pattern.compile("\\d+").matcher(trx);
            if (m.find()) {
                result = m.group();
                logger.info(result);
            }
        }
        return result;
    }

    private String extractTrxAmount(String s) {
        Matcher m = Pattern.compile("Tk 50.00 from").matcher(s);
        String result = "";
        if (m.find()) {
            String amount = m.group();

            logger.info(amount);
            m = Pattern.compile("\\d+\\.\\d+").matcher(amount);
            if (m.find()) {
                result = m.group();
                logger.info(result);
            }
        }
        return result;
    }

    private String extractTxnAmount(String s) {
        Matcher m = Pattern.compile("Tk50.00;").matcher(s);
        String result = "";
        if (m.find()) {
            String amount = m.group();

            logger.info(amount);
            m = Pattern.compile("\\d+\\.\\d+").matcher(amount);
            if (m.find()) {
                result = m.group();
                logger.info(result);
            }
        }
        return result;
    }

    private HttpResponse newPayment(String txId, String txDate, String txAmount) {

        try {
            logger.info("check existing tranasction request");

            Registration registration = regDao.findById(txId);

            if (registration == null) {
                logger.info("no transaction found!");
                logger.info("create transaction");

                registration = new Registration();
                registration.setId(txId);
                registration.setTxAmount(txAmount);
                registration.setTxDate(txDate);
                regDao.create(registration);
                logger.info("new transaction created");
                logger.info(registration.toString());

                return new HttpResponse(HttpStatus.CREATED, "TrxID " + txId + " created");
            }
            return new HttpResponse(HttpStatus.CONFLICT, "Wrong transaction information");

        }
        catch (Exception e) {
            logger.log(Level.INFO, "error: {0}", e.getMessage());
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
