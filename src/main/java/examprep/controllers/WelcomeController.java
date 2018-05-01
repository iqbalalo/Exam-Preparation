package examprep.controllers;

import examprep.entities.Exam;
import examprep.entities.Registration;
import examprep.entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("loggedUser")
public class WelcomeController extends BaseController {

    static final Logger logger = Logger.getLogger(WelcomeController.class.getName());

    long totalMCQ, totalReg;

    @RequestMapping(value = {"", "/", "home"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home");

        try {
            logger.info("start");

            totalReg = regDao.numberOfRecordsApprox();

            logger.log(Level.INFO, "total registration: {0}", totalReg);

            mv.addObject("menu", "Home");
            mv.addObject("totalMCQ", totalMCQ);
            mv.addObject("totalReg", totalReg);
            mv.addObject("exams", examDao.findAll());
            return mv;
        }
        catch (Exception ex) {
            mv.addObject("message", ex.getMessage());
            return mv;
        }
    }

    @RequestMapping(value = {"/ReCountMcq"}, method = RequestMethod.GET)
    public ModelAndView reCount(RedirectAttributes ra) {
        ModelAndView mv = new ModelAndView("redirect:/home");

        try {
            logger.info("start");

            examDao.updateExamWiseMCQCount();
            return mv;
        }
        catch (Exception ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return mv;
        }
    }

    @RequestMapping(value = {"/examinee_home"}, method = RequestMethod.GET)
    public ModelAndView examineeHome(HttpSession httpSession) {
        ModelAndView mv = new ModelAndView("examinee_home");
        try {
            logger.info("start");
            User u = (User) httpSession.getAttribute("loggedUser");
            logger.log(Level.INFO, "user: {0}", u.getName());

            List<Registration> regs = regDao.findByPhone(u.getId());
            logger.log(Level.INFO, "registered exam size: {0}", regs.size());

            List<Exam> registeredExams = new ArrayList();
            for (Registration r : regs) {
                Exam e = examDao.findById(r.getExam());
                registeredExams.add(e);
            }

            mv.addObject("menu", "Home");
            mv.addObject("registeredExams", registeredExams);
            mv.addObject("exams", examDao.findAll());
            mv.addObject("scores", scoreDao.findAll(u.getId()));
            return mv;
        }
        catch (Exception ex) {
            mv.addObject("message", ex.getMessage());
            return mv;
        }
    }

    @RequestMapping(value = "/unauthorised_access", method = RequestMethod.GET)
    public ModelAndView unauthorisedAccess() {
        logger.info("start");

        ModelAndView mv = new ModelAndView("unauthorised_access");

        logger.info("finish");
        return mv;
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView signIn() {
        logger.info("start");

        ModelAndView mv = new ModelAndView("signin");

        logger.info("finish");
        return mv;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signUp() {
        logger.info("start");

        ModelAndView mv = new ModelAndView("signup");
        logger.info("finish");
        return mv;
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ModelAndView submitSignIn(@RequestParam("id") String phone, @RequestParam("password") String password) {

        try {
            logger.info("start");

            logger.log(Level.INFO, "phone: {0}, password: {1}", new Object[]{phone, password});

            User u = userDao.findById(phone);
            if (u != null && u.getPassword().equals(password)) {
                logger.log(Level.INFO, "valid user: {0}", u);

                if (u.getType().equals("examinee")) {
                    ModelAndView mv = new ModelAndView("redirect:/examinee_home");
                    mv.addObject("loggedUser", u);
                    logger.info("finish");
                    return mv;
                }
                else {
                    ModelAndView mv = new ModelAndView("redirect:/home");
                    mv.addObject("loggedUser", u);
                    logger.info("finish");
                    return mv;
                }

            }
            else {
                logger.info("invalid user!");
                throw new Exception("Invalid user!");
            }
        }
        catch (Exception ex) {
            ModelAndView mv = new ModelAndView("redirect:/signin/failed");
            mv.addObject("message", ex.getMessage());
            logger.info("finish");
            return mv;
        }
    }

    @RequestMapping(value = "/signin/failed", method = RequestMethod.GET)
    public String signInFailed(@RequestParam("message") String msg, RedirectAttributes redirectAttributes) {
        logger.info("start");

        redirectAttributes.addFlashAttribute("message", msg);

        logger.info("finish");
        return "redirect:/signin";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView submitSignUp(@RequestParam("phone") String phone,
                                     @RequestParam("name") String name,
                                     @RequestParam("password") String password,
                                     RedirectAttributes redirectAttributes) {

        try {
            logger.info("start");

            if (phone.trim().isEmpty() || name.trim().isEmpty() || password.trim().isEmpty()) {
                throw new Exception("All the information is required!");
            }

            User u = new User();
            u.setId(phone.trim());
            u.setName(name.trim());
            u.setPassword(password.trim());
            u.setType("examinee");
            userDao.create(u);

            logger.log(Level.INFO, "new user created: {0}", u);
            ModelAndView mv = new ModelAndView("redirect:/signin");
            redirectAttributes.addFlashAttribute("message", "Congratulation! You have been registered successfully. Sign In now.");
            logger.info("finish");
            return mv;
        }
        catch (Exception ex) {
            ModelAndView mv = new ModelAndView("redirect:/signup");
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            logger.info("finish");
            return mv;
        }
    }

    @RequestMapping(value = "/signout", method = RequestMethod.GET)
    public ModelAndView signOut(SessionStatus status) {

        logger.info("start");

        status.setComplete();
        ModelAndView mv = new ModelAndView("redirect:/home");

        logger.info("finish");
        return mv;
    }
}
