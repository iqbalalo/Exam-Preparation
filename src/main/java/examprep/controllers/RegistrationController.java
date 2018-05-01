package examprep.controllers;

import com.nihonsoftwork.smskitti.util.DateUtil;
import examprep.entities.Registration;
import examprep.entities.User;
import examprep.utils.RandomString;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/registration")
public class RegistrationController extends BaseController {

    static final Logger logger = Logger.getLogger(RegistrationController.class.getName());

    List<Registration> regs;
    Registration backupRegistration;

    @ModelAttribute
    public void objectEveryTime(Model m) {
        m.addAttribute("menu", "Registration");
    }

    @RequestMapping(value = {"", "/list"}, method = {RequestMethod.GET})
    public ModelAndView getList() {
        logger.info("start");

        ModelAndView mv = new ModelAndView("/registration/list");
        mv.addObject("title", "Registration List");

        try {
            regs = regDao.findAll();
            Collections.sort(regs);
            logger.log(Level.INFO, "registration list size is {0}", regs.size());

            if (regs.isEmpty()) {
                mv.addObject("message", "No records were found!");
                return mv;
            }

            mv.addObject("regs", regs);
            logger.info("finish");
            return mv;
        }
        catch (Exception e) {
            logger.severe(e.getMessage());
            mv.addObject("Error! " + e.getMessage());
            logger.info("finish");
            return mv;
        }

    }

    @RequestMapping(value = {"/search"}, method = {RequestMethod.POST})
    public ModelAndView searchRegistration(@RequestParam("keyword") String keyword) {
        logger.info("start");

        ModelAndView mv = new ModelAndView("/registration/list");
        mv.addObject("title", "Registration List");

        try {
            logger.log(Level.INFO, "regs list size: {0}", regs.size());
            logger.log(Level.INFO, "keyword: {0}", keyword);

            List<Registration> filteredRegs = regDao.search(regs, keyword.toLowerCase());

            logger.log(Level.INFO, "filtered list size is {0}", filteredRegs.size());

            if (filteredRegs.isEmpty()) {
                mv.addObject("message", "No records were found!");
                return mv;
            }

            mv.addObject("regs", filteredRegs);
            mv.addObject("reset", true);
            logger.info("finish");
            return mv;
        }
        catch (Exception e) {
            logger.log(Level.INFO, "exception: {0}", e.getMessage());
            mv.addObject("Error! " + e.getMessage());
            logger.info("finish");
            return mv;
        }
    }

    @RequestMapping(value = {"/transaction/new"}, method = RequestMethod.GET)
    public ModelAndView newTransactionEntryForm() {
        ModelAndView mv = new ModelAndView("/registration/new_transaction");
        mv.addObject("title", "New Transaction");
        return mv;
    }

    @RequestMapping(value = "/transaction/save", method = RequestMethod.POST)
    public ModelAndView newPayment(
            @RequestParam("paymentBy") String paymentBy,
            @RequestParam("txId") String txId,
            @RequestParam("txDate") String txDate,
            @RequestParam("txAmount") String txAmount,
            RedirectAttributes redirectAttributes) {

        logger.info("start");

        ModelAndView mv = new ModelAndView("redirect:/registration/list");

        if (txId == null || txId.trim().isEmpty()
                || txDate == null || txDate.trim().isEmpty()
                || txAmount == null || txAmount.trim().isEmpty()) {

            logger.info("parameter missing. wrong request");

            redirectAttributes.addFlashAttribute("message", "Transaction paramaters are not correct!");
            logger.info("finish");
            return mv;
        }

        try {

            logger.info("check existing tranasction request");

            Registration registration = regDao.findById(paymentBy + "-" + txId);

            if (registration == null) {
                logger.info("no transaction found!");
                logger.info("create transaction");

                registration = new Registration();
                registration.setId(paymentBy + "-" + txId);
                registration.setTxAmount(txAmount);
                registration.setTxDate(txDate);
                regDao.create(registration);
                logger.info("new transaction created");
                logger.info(registration.toString());

                redirectAttributes.addFlashAttribute("message", "Transaction has been saved");
                logger.info("finish");
                return mv;
            }
            redirectAttributes.addFlashAttribute("message", "Duplicate transaction Id found! Transaction was not saved!");
            logger.info("finish");
            return mv;
        }
        catch (Exception e) {
            logger.log(Level.INFO, "error: {0}", e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            logger.info("finish");
            return mv;
        }
    }

    @RequestMapping(value = "/register_exam", method = RequestMethod.POST)
    public ModelAndView registerExam(HttpSession httpSession,
                                     @RequestParam("paymentBy") String paymentBy,
                                     @RequestParam("exam") String exam,
                                     @RequestParam("transactionId") String transactionId,
                                     RedirectAttributes redirectAttributes) {
        logger.info("start");
        ModelAndView mv = new ModelAndView("redirect:/examinee_home");

        try {
            if ("".equals(exam) || "".equals(transactionId.trim())) {
                logger.info("parameter is empty");
                throw new Exception("All the fields are required.");
            }

            User u = (User) httpSession.getAttribute("loggedUser");
            //u.getId returns the logged user phone number

            //Check transaction Id is valid and already registerd or not
            Registration r = regDao.findById(paymentBy + "-" + transactionId);
            if (r == null) {
                logger.info("Transaction ID is not valid");
                throw new Exception("Transaction ID is not valid!");
            }
            //If phone is not registered yet. That means it is valid to register
            else if (r.getPhone() == null) {
                logger.info("Valid for new registration");

                //Update registration information and update examineee profile
                r.setExam(exam);
                r.setPhone(u.getId());  //Get phone number and set phone number to registration table
                r.setRegDate(DateUtil.currentDateString());
                r.setAccessKey(RandomString.getRandomString(10) + new Date().getTime() + RandomString.getRandomString(5));
                regDao.create(r);
            }
            else if (r.getPhone().equals(u.getId())) {
                logger.info("already register");
                throw new Exception("Already registered! No need to register again");
            }
            else {
                logger.info("Invalid transaction");
                throw new Exception("Invalid Transaction ID");
            }

            logger.info("finish");
            return mv;
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            logger.info("finish");
            return mv;
        }
    }

    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        logger.info("start");
        ModelAndView mv = new ModelAndView("redirect:/registration/list");
        try {
            regDao.delete(id);

            logger.log(Level.INFO, "deleted: {0}", id);

            redirectAttributes.addFlashAttribute("message", "Registration has been deleted");
            logger.info("finish");
            return mv;
        }
        catch (Exception ex) {
            logger.log(Level.INFO, "error: {0}", ex.getMessage());
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            logger.info("finish");
            return mv;
        }
    }

}
