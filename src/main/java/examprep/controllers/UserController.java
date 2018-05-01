package examprep.controllers;

import examprep.entities.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    static final Logger logger = Logger.getLogger(UserController.class.getName());

    @ModelAttribute
    public void objectEveryTime(Model m) {
        m.addAttribute("menu", "User");
    }

    @RequestMapping(value = {"", "/list"}, method = {RequestMethod.GET})
    public ModelAndView getList() {
        logger.info("start");

        ModelAndView mv = new ModelAndView("/user/list");
        mv.addObject("title", "User List");

        try {
            List<User> users = userDao.findAll();
            logger.log(Level.INFO, "User list size is {0}", users.size());

            if (users.isEmpty()) {
                mv.addObject("message", "No records were found!");
                return mv;
            }

            mv.addObject("users", users);
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

    @RequestMapping(value = {"/new"}, method = RequestMethod.GET)
    public ModelAndView newUser() {
        ModelAndView mv = new ModelAndView("/user/new");
        mv.addObject("title", "New User");
        return mv;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {

        logger.info("start");

        ModelAndView mv = new ModelAndView("redirect:/user/list");

        if (user.getId() == null || user.getId().trim().isEmpty()
                || user.getName() == null || user.getName().trim().isEmpty()
                || user.getPassword() == null || user.getPassword().trim().isEmpty()) {

            logger.info("parameter missing. wrong request");

            redirectAttributes.addFlashAttribute("message", "Inputt paramaters are not correct!");
            logger.info("finish");
            return mv;
        }

        try {

            User u = userDao.findById(user.getId());

            if (u == null) {
                logger.info("no user found!");
                logger.info("create user");

                userDao.create(user);
                logger.info("new user created");

                redirectAttributes.addFlashAttribute("message", "User has been created");
                logger.info("finish");
                return mv;
            }
            redirectAttributes.addFlashAttribute("message", "Duplicate user found! User was not saved!");
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

    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        logger.info("start");
        ModelAndView mv = new ModelAndView("redirect:/user/list");
        try {
            userDao.delete(id);

            logger.log(Level.INFO, "deleted: {0}", id);

            redirectAttributes.addFlashAttribute("message", "User has been deleted");
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
