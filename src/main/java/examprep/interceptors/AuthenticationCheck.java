package examprep.interceptors;

import examprep.entities.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AuthenticationCheck implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object o) throws Exception {
        String reqUri = req.getRequestURI();

        User loggedUser = (User) req.getSession().getAttribute("loggedUser");

        if (reqUri.contains("/signin") || reqUri.contains("/signup")
                || reqUri.contains("/resources")
                || reqUri.contains("/json")
                || reqUri.contains("/registration/new")
                || reqUri.contains("/payment")) {

            if (loggedUser != null && reqUri.contains("/signin")) {
                res.sendRedirect(req.getContextPath() + "/home");
                return false;
            }
            return true;
        }
        else {
            if (loggedUser == null) {
                res.sendRedirect(req.getContextPath() + "/signin");
                return false;
            }
            else if (loggedUser.getType().equals("admin")) {
                return true;
            }
            else if (loggedUser.getType().equals("user")) {
                if (reqUri.contains("delete") || reqUri.contains("registration") || reqUri.contains("user") || reqUri.contains("ReCountMcq")) {
                    res.sendRedirect(req.getContextPath() + "/unauthorised_access");
                    return false;
                }
            }
            else if (loggedUser.getType().equals("examinee")) {
                if (reqUri.contains("register_exam")) {
                    return true;
                }

                if (reqUri.contains("/home") || reqUri.contains("delete") || reqUri.contains("user") || reqUri.contains("registration") || reqUri.contains("ReCountMcq")) {
                    res.sendRedirect(req.getContextPath() + "/examinee_home");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object o, ModelAndView mav) throws Exception {
        //System.out.println("Post-handle");
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object o, Exception excptn) throws Exception {
        //System.out.println("Handler Complete");
    }

}
