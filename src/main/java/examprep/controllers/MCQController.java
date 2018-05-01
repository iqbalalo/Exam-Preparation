package examprep.controllers;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.nihonsoftwork.smskitti.util.DateUtil;
import examprep.entities.Answer;
import examprep.entities.Exam;
import examprep.entities.MCQ;
import examprep.entities.Score;
import examprep.entities.Subject;
import examprep.entities.User;
import examprep.utils.ExcelFileProcessor;
import examprep.utils.Utility;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mcq")
@Scope("session")
public class MCQController extends BaseController {

    static final Logger logger = Logger.getLogger(MCQController.class.getName());

    List<MCQ> mcqs = new ArrayList<>();
    MCQ backupMCQ;

    String userExamSelection;
    String userSubjectSelection;

    @ModelAttribute
    public void objectEveryTime(Model m) {
        try {

            if (exams.isEmpty()) {
                exams = examDao.findAll();
                subjects = examDao.findAllSubject();
            }

            m.addAttribute("menu", "MCQ");
            m.addAttribute("exams", exams);
            m.addAttribute("subjects", subjects);
        }
        catch (Exception ex) {
            m.addAttribute("message", ex.getMessage());
        }
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("/mcq/list");
        mv.addObject("title", "MCQ List");

        return mv;
    }

    @RequestMapping(value = {"/list"}, method = {RequestMethod.GET})
    public ModelAndView getMCQs(@RequestParam(required = false) String exam, @RequestParam(required = false) String subject) {
        logger.info("start");

        ModelAndView mv = new ModelAndView("/mcq/list");
        mv.addObject("title", "MCQ List");
        if (exam != null && subject != null) {
            mv.addObject("exam", exam);
            mv.addObject("subject", subject);

            userExamSelection = exam;
            userSubjectSelection = subject;
        }

        try {
            mcqs = mcqDao.findAll(exam, subject);
            Collections.sort(mcqs);

            logger.log(Level.INFO, "mcq list size is {0}", mcqs.size());

            if (mcqs.isEmpty()) {
                mv.addObject("message", "No records were found!");
                return mv;
            }

            mv.addObject("mcqs", mcqs);
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
    public ModelAndView searchMCQ(@RequestParam("keyword") String keyword) {
        logger.info("start");

        ModelAndView mv = new ModelAndView("/mcq/list");
        mv.addObject("title", "MCQ List");

        try {
            List<MCQ> filteredMCQs = mcqDao.search(mcqs, keyword.toLowerCase());

            logger.log(Level.INFO, "filtered list size is {0}", filteredMCQs.size());

            if (filteredMCQs.isEmpty()) {
                mv.addObject("message", "No records were found!");
                return mv;
            }

            mv.addObject("mcqs", filteredMCQs);
            mv.addObject("reset", true);
            mv.addObject("exam", userExamSelection);
            mv.addObject("subject", userSubjectSelection);
            mv.addObject("resetToExam", userExamSelection);
            mv.addObject("resetToSubject", userSubjectSelection);
            return mv;
        }
        catch (Exception e) {
            logger.info(e.getMessage());
            mv.addObject("Error! " + e.getMessage());
            logger.info("finish");
            return mv;
        }
    }

    @RequestMapping(value = {"/new"}, method = RequestMethod.GET)
    public ModelAndView newMCQForm() {
        ModelAndView mv = new ModelAndView("/mcq/new");
        mv.addObject("title", "New MCQ");
        mv.addObject("exam", userExamSelection);
        mv.addObject("subject", userSubjectSelection);
        return mv;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
    public ModelAndView save(
            @RequestParam("question") String question,
            @RequestParam("answer_options[]") String[] answerOptions,
            @RequestParam("correct_answer") int correctAnswer,
            @RequestParam("exam") String exam,
            @RequestParam("subject") String subject,
            @RequestParam("history") String history,
            @RequestParam("note") String note,
            RedirectAttributes redirectAttributes) {

        logger.info("start");

        if (question.trim().isEmpty() || answerOptions.length == 0
                || "0".equals(exam) || "0".equals(subject)) {

            throw new RuntimeException("Invalid input parameters!");
        }

        logger.info("validation complete");

        try {
            MCQ q = new MCQ();
            q.setId(new Date().getTime() + "");
            q.setQuestion(question);
            q.setAnswerOptions(answerOptions);
            q.setCorrectAnswer(answerOptions[correctAnswer]);
            q.setExam(exam);
            q.setSubject(subject);
            q.setHistory(history);
            q.setNote(note);

            mcqDao.create(q);
            //update mcq count table for particular exam and subject;
            examDao.addMcqCount(exam, subject);

            logger.info("data saved successfully and update count");

            ModelAndView mv = new ModelAndView("redirect:/mcq/list");
            redirectAttributes.addFlashAttribute("message", "MCQ has been saved");
            redirectAttributes.addAttribute("exam", q.getExam());
            redirectAttributes.addAttribute("subject", q.getSubject());
            logger.info("finish");
            return mv;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "error: {0}", e.getMessage());
            ModelAndView mv = new ModelAndView("redirect:/mcq/new");
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            logger.info("finish");
            return mv;
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, params = "saveAndNewForm")
    public ModelAndView saveAndNew(
            @RequestParam("question") String question,
            @RequestParam("answer_options[]") String[] answerOptions,
            @RequestParam("correct_answer") int correctAnswer,
            @RequestParam("exam") String exam,
            @RequestParam("subject") String subject,
            @RequestParam("history") String history,
            @RequestParam("note") String note,
            RedirectAttributes redirectAttributes) {

        logger.info("start");

        if (question.trim().isEmpty() || answerOptions.length == 0
                || "0".equals(exam) || "0".equals(subject)) {

            throw new RuntimeException("Invalid input parameters!");
        }

        logger.info("validation complete");

        try {
            MCQ q = new MCQ();
            q.setId(new Date().getTime() + "");
            q.setQuestion(question);
            q.setAnswerOptions(answerOptions);
            q.setCorrectAnswer(answerOptions[correctAnswer]);
            q.setExam(exam);
            q.setSubject(subject);
            q.setHistory(history);
            q.setNote(note);

            mcqDao.create(q);
            //update mcq count table for particular exam and subject;
            examDao.addMcqCount(exam, subject);
            logger.info("data saved successfully and update count");

            ModelAndView mv = new ModelAndView("redirect:/mcq/new");
            logger.info("finish");
            return mv;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "error: {0}", e.getMessage());
            ModelAndView mv = new ModelAndView("redirect:/mcq/new");
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            logger.info("finish");
            return mv;
        }
    }

    @RequestMapping(value = {"/edit/{id}"}, method = RequestMethod.GET)
    public ModelAndView editMCQForm(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("/mcq/edit");

        try {
            MCQ q = mcqDao.findById(userExamSelection, id);
            backupMCQ = q;
            if (q != null) {
                mv.addObject("mcq", q);
            }

            mv.addObject("title", "Edit MCQ");
            mv.addObject("exam", userExamSelection);
            mv.addObject("subject", userSubjectSelection);
            return mv;
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, "error: {0}", ex.getMessage());
            mv = new ModelAndView("redirect:/mcq/list");
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            logger.info("finish");
            return mv;
        }

    }

    @RequestMapping(value = {"/saveas/{id}"}, method = RequestMethod.GET)
    public ModelAndView copyMCQForm(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("/mcq/saveas");

        try {
            MCQ q = mcqDao.findById(userExamSelection, id);
            if (q != null) {
                mv.addObject("mcq", q);
            }

            mv.addObject("title", "Save As");
            mv.addObject("exam", userExamSelection);
            mv.addObject("subject", userSubjectSelection);
            return mv;
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, "error: {0}", ex.getMessage());
            mv = new ModelAndView("redirect:/mcq/saveas/" + id);
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            logger.info("finish");
            return mv;
        }

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam("id") String id,
            @RequestParam("question") String question,
            @RequestParam("answer_options[]") String[] answerOptions,
            @RequestParam("correct_answer") int correctAnswer,
            @RequestParam("exam") String exam,
            @RequestParam("subject") String subject,
            @RequestParam("history") String history,
            @RequestParam("note") String note,
            RedirectAttributes redirectAttributes) {

        logger.info("start");

        if (question.trim().isEmpty() || answerOptions.length == 0
                || "0".equals(exam) || "0".equals(subject)) {

            throw new RuntimeException("Invalid input parameters!");
        }

        logger.info("validation complete");

        try {
            //First delete the old MCQ
            mcqDao.delete(backupMCQ.getExam(), backupMCQ.getId());
            logger.info("old MCQ deleted");
            examDao.subtractMcqCount(backupMCQ.getExam(), backupMCQ.getSubject());
            logger.info("update MCQ count");

            MCQ q = new MCQ();
            q.setId(id);
            q.setQuestion(question);
            q.setAnswerOptions(answerOptions);
            q.setCorrectAnswer(answerOptions[correctAnswer]);
            q.setExam(exam);
            q.setSubject(subject);
            q.setHistory(history);
            q.setNote(note);

            mcqDao.update(q);
            logger.info("data saved successfully");

            //Add MCQ count to new subject
            examDao.addMcqCount(exam, subject);

            ModelAndView mv = new ModelAndView("redirect:/mcq/list");
            redirectAttributes.addFlashAttribute("message", "MCQ has been saved");
            redirectAttributes.addAttribute("exam", q.getExam());
            redirectAttributes.addAttribute("subject", q.getSubject());
            logger.info("finish");
            return mv;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "error: {0}", e.getMessage());
            ModelAndView mv = new ModelAndView("redirect:/mcq/edit/" + id);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            logger.info("finish");
            return mv;
        }
    }

    @RequestMapping(value = {"upload_excel_file"}, method = RequestMethod.GET)
    public ModelAndView uploadXLSXPage() {
        ModelAndView mv = new ModelAndView("/mcq/upload_excel");
        mv.addObject("menu", "UploadExcel");
        mv.addObject("title", "Excel File Upload Form");

        return mv;
    }

    @RequestMapping(value = {"excel_file_submit"}, method = RequestMethod.POST)
    public ModelAndView excelFileSubmit(@RequestParam("excelFile") MultipartFile excelFile) {

        logger.info("start");
        ModelAndView mv = new ModelAndView("/mcq/upload_excel");
        mv.addObject("menu", "UploadExcel");
        mv.addObject("title", "Excel File Upload Form");

        ExcelFileProcessor efp = new ExcelFileProcessor();

        try {

            if (excelFile == null || excelFile.getSize() == 0) {
                logger.info("file size is incorrect");
                mv.addObject("message", "File size is not correct!");
            }
            else {
                logger.info("excel file will process now");

                String fileExt = excelFile.getOriginalFilename().substring(excelFile.getOriginalFilename().indexOf("."));

                File tempFile = File.createTempFile("temp", fileExt);
                excelFile.transferTo(tempFile);

                logger.info("temp excel file created");

                List<ArrayList<String>> rows = efp.read(tempFile);
                logger.log(Level.INFO, "excel read rows size: {0}", rows.size());

                List<MCQ> mcqList = new ArrayList();

                long currTime = new Date().getTime();
                for (int i = 0; i < rows.size(); i++) {
                    logger.log(Level.INFO, "mcq object with id {0} in row {1}", new Object[]{currTime, i});

                    MCQ q = new MCQ();

                    q.setId((currTime + i) + "");
                    q.setQuestion(rows.get(i).get(0));
                    String[] s = {rows.get(i).get(1), rows.get(i).get(2), rows.get(i).get(3), rows.get(i).get(4)};
                    q.setAnswerOptions(s);
                    q.setCorrectAnswer(rows.get(i).get(5));
                    q.setExam(rows.get(i).get(6));
                    q.setSubject(rows.get(i).get(7));
                    mcqList.add(q);
                }

                logger.log(Level.INFO, "mcq list size {0}", mcqList.size());
                String duplicates = "";

                for (int i = 0; i < mcqList.size(); i++) {
                    String[] tmp = mcqList.get(i).getAnswerOptions();
                    if (mcqList.get(i).getCorrectAnswer().isEmpty() || tmp[0].isEmpty() || tmp[1].isEmpty() || tmp[2].isEmpty() || tmp[3].isEmpty()) {
                        continue;
                    }
                    if (Utility.checkDuplicated(mcqList.get(i).getAnswerOptions()) == false) {
                        mcqDao.create(mcqList.get(i));
                        logger.log(Level.INFO, "new mcq created: {0}", mcqList.get(i).getId());
                        //update MCQ count
                        examDao.addMcqCount(mcqList.get(i).getExam(), mcqList.get(i).getSubject());
                    }
                    else {
                        duplicates += mcqList.get(i).getQuestion() + "; ";
                        logger.log(Level.INFO, "duplicate value in mcq. skipped!", i);
                    }
                }
                if ("".equals(duplicates)) {
                    mv.addObject("message", "Uploaded successfully");
                }
                else {
                    mv.addObject("message", "Uploaded successfully except - " + duplicates);
                }

                logger.info("excel file upload success");
                logger.info("finish");
            }

        }
        catch (IOException e) {
            logger.info(e.getMessage());
            logger.info("finish");
            mv.addObject("message", "Error! " + e.getMessage());
        }
        catch (Exception ex) {
            logger.info(ex.getMessage());
            logger.info("finish");
            mv.addObject("message", "Error! " + ex.getMessage());
        }

        return mv;
    }

    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {

        logger.info("start");
        ModelAndView mv = new ModelAndView("redirect:/mcq/list");

        try {
            mcqDao.delete(userExamSelection, id);
            //update mcq count table for particular exam and subject;
            examDao.subtractMcqCount(userExamSelection, userSubjectSelection);

            logger.log(Level.INFO, "deleted: {0}", id);

            redirectAttributes.addFlashAttribute("message", "MCQ has been deleted");
            redirectAttributes.addAttribute("exam", userExamSelection);
            redirectAttributes.addAttribute("subject", userSubjectSelection);

            logger.info("finish");
            return mv;
        }
        catch (Exception ex) {
            logger.log(Level.INFO, "error: {0}", ex.getMessage());
            redirectAttributes.addAttribute("exam", userExamSelection);
            redirectAttributes.addAttribute("subject", userSubjectSelection);
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            logger.info("finish");
            return mv;
        }

    }

    @RequestMapping(value = {"/modeltest/{exam}/{subject}"}, method = RequestMethod.GET)
    public ModelAndView modelTest(@PathVariable("exam") String exam, @PathVariable("subject") String subject) {
        try {
            logger.info("start");
            ModelAndView mv = new ModelAndView("/mcq/modeltest");
            Exam e = examDao.findById(exam);
            Subject s = examDao.findSubjectByExam(exam, subject);
            List<MCQ> mcqs = mcqDao.findAll(exam, subject);
            List<MCQ> selectedMcqs = new ArrayList();

            for (int i = 0; i < 100; i++) {
                int randQNo = Utility.randInt(0, mcqs.size() - 1);
                MCQ mcq = mcqs.get(randQNo);
                mcq.setCorrectAnswer("");
                selectedMcqs.add(mcq);
                mcqs.remove(randQNo);
            }

            mv.addObject("exam", e);
            mv.addObject("subject", s);
            mv.addObject("mcqs", selectedMcqs);
            logger.info("finish");
            return mv;
        }
        catch (Exception ex) {
            logger.info("error");
            ModelAndView mv = new ModelAndView("examinee_home");
            mv.addObject("message", ex.getMessage());
            return mv;

        }
    }

    @RequestMapping(value = {"/modeltest/question"}, method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView modelTestQuestion(@RequestParam("exam") String exam, @RequestParam("id") String id, @RequestParam("qSl") String qSl) {
        ModelAndView mv = new ModelAndView("/mcq/modeltest_mcq");
        try {
            logger.info("start");
            if (exam.isEmpty() || id.isEmpty()) {
                throw new Exception("Parameter is invalid");
            }

            MCQ q = mcqDao.findById(exam, id);
            logger.log(Level.INFO, "MCQ: {0}", q.toString());
            q.setCorrectAnswer("");
            mv.addObject("mcq", q);
            mv.addObject("qSl", qSl);
            logger.info("finish");
            return mv;
        }
        catch (Exception ex) {
            logger.info("error");
            logger.info(ex.getMessage());
            mv.addObject("message", ex.getMessage());
            return mv;
        }
    }

    @RequestMapping(value = {"/modeltest/finish"}, method = RequestMethod.POST)
    public @ResponseBody
    String modelTestFinish(HttpSession httpSession,
                           @RequestParam("exam") String exam,
                           @RequestParam("subject") String subject,
                           @RequestParam("answers") String answers,
                           @RequestParam("totalMark") String totalMark,
                           @RequestParam("timeTaken") String timeTaken) {
        try {
            logger.info("start");
            if (exam.isEmpty() || answers.isEmpty()) {
                throw new Exception("Parameter is invalid");
            }

            List<Answer> answerList = new Gson().fromJson(answers, new TypeToken<List<Answer>>() {
            }.getType());

            int point = 0;
            for (Answer a : answerList) {
                MCQ m = mcqDao.findById(exam, a.getqId());
                if (m.getCorrectAnswer().equals(a.getAnswer())) {
                    point++;
                }
            }
            User loggedUser = (User) httpSession.getAttribute("loggedUser");

            Score score = new Score();
            score.setUserId(loggedUser.getId());
            score.setExamDateTime(DateUtil.currentDateTimeString());
            score.setObtainedMark(point + "");
            score.setTotalMark(totalMark);
            score.setExam(examDao.findById(exam).getName());
            score.setSubject(examDao.findSubjectByExam(exam, subject).getName());
            score.setTimeTaken(timeTaken);
            scoreDao.createAndUpdate(score);
            logger.info("score stored");
            logger.info("finish");
            return "success";
        }
        catch (Exception ex) {
            logger.log(Level.INFO, "error: {0}", ex.getMessage());
            return ex.getMessage();
        }
    }

}
