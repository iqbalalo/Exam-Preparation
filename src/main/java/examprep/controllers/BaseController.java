package examprep.controllers;

import examprep.DAOs.ExamDAO;
import examprep.DAOs.MCQDAO;
import examprep.DAOs.RegistrationDAO;
import examprep.DAOs.ScoreDAO;
import examprep.DAOs.UserDAO;
import examprep.entities.Exam;
import examprep.entities.Subject;
import java.util.ArrayList;
import java.util.List;

public class BaseController {

    MCQDAO mcqDao;
    ExamDAO examDao;
    RegistrationDAO regDao;
    UserDAO userDao;
    ScoreDAO scoreDao;

    List<Exam> exams = new ArrayList<>();
    List<Subject> subjects = new ArrayList<>();

    public BaseController() {
        this.mcqDao = new MCQDAO();
        this.examDao = new ExamDAO();
        this.regDao = new RegistrationDAO();
        this.userDao = new UserDAO();
        this.scoreDao = new ScoreDAO();
    }
}
