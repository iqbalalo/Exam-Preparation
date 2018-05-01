package examprep.DAOs;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import examprep.entities.Exam;
import examprep.entities.Subject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExamDAO {

    String tableName;
    DynamoDBService dbs;

    public ExamDAO() {
        dbs = new DynamoDBService();
        tableName = "Exam";
    }

    public void createOrUpdate(Exam exam) throws Exception {

        Item item = new Item();

        List<Map<String, String>> subjects = new ArrayList();
        for (Subject s : exam.getSubjects()) {
            Map<String, String> m = new HashMap();
            m.put("id", s.getId());
            m.put("name", s.getName());
            m.put("total_mcq", s.getTotalMcq() + "");
            subjects.add(m);
        }

        item.withPrimaryKey("id", exam.getId())
                .withString("name", exam.getName() + "")
                .withString("access", exam.getAccess() + "")
                .withList("subjects", subjects)
                .withString("sl", exam.getSl());

        Table table = dbs.getDynamoDB().getTable(tableName);
        table.putItem(item);
    }

    public void delete(String id) {
        Table table = dbs.dynamoDB.getTable(tableName);
        table.deleteItem("id", id);
    }

    public List<Exam> findAll() throws Exception {

        List<Exam> exams = new ArrayList();
        Map<String, AttributeValue> lastKeyEvaluated = null;

        do {
            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(tableName)
                    .withExclusiveStartKey(lastKeyEvaluated);

            ScanResult result = dbs.getDynamoDBClient().scan(scanRequest);

            for (Map<String, AttributeValue> item : result.getItems()) {

                Exam e = new Exam();
                e.setId(item.get("id").getS());
                e.setName(item.get("name").getS());

                List<AttributeValue> lav = item.get("subjects").getL();
                List<Subject> subs = new ArrayList();
                for (AttributeValue av : lav) {
                    Subject s = new Subject(av.getM().get("id").getS(), av.getM().get("name").getS(), Integer.parseInt(av.getM().get("total_mcq").getS()));
                    subs.add(s);
                }
                e.setSubjects(subs);
                e.setAccess(item.get("access").getS());
                e.setSl(item.get("sl").getS());

                exams.add(e);
            }

            lastKeyEvaluated = result.getLastEvaluatedKey();
        }
        while (lastKeyEvaluated != null);

        return exams;
    }

    public Exam findById(String id) throws Exception {

        Table table = dbs.dynamoDB.getTable(tableName);

        Item item = table.getItem("id", id);

        if (item == null) {
            return null;
        }

        Exam e = new Exam();
        e.setId(item.getString("id"));
        e.setName(item.getString("name"));

        List<Map<String, String>> lav = item.getList("subjects");
        List<Subject> subs = new ArrayList();

        for (Map<String, String> m : lav) {
            Subject s = new Subject(m.get("id"), m.get("name"), Integer.parseInt(m.get("total_mcq")));
            subs.add(s);
        }
        e.setSubjects(subs);
        e.setAccess(item.getString("access"));
        e.setSl(item.getString("sl"));

        return e;
    }

    public List<Subject> findAllSubject() throws Exception {

        List<Subject> subs = new ArrayList();
        Map<String, AttributeValue> lastKeyEvaluated = null;

        do {
            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(tableName)
                    .withExclusiveStartKey(lastKeyEvaluated);

            ScanResult result = dbs.getDynamoDBClient().scan(scanRequest);

            for (Map<String, AttributeValue> item : result.getItems()) {

                List<AttributeValue> lav = item.get("subjects").getL();
                for (AttributeValue av : lav) {
                    Subject s = new Subject(av.getM().get("id").getS(), av.getM().get("name").getS(), Integer.parseInt(av.getM().get("total_mcq").getS()));
                    subs.add(s);
                }
            }

            lastKeyEvaluated = result.getLastEvaluatedKey();
        }
        while (lastKeyEvaluated != null);

        //Remove duplicate items
        subs = subs.stream().distinct().collect(Collectors.toList());

        return subs;
    }

    public List<Subject> findSubjectsByExam(String id) throws Exception {
        List<Subject> subs = new ArrayList();
        Table table = dbs.dynamoDB.getTable(tableName);

        Item item = table.getItem("id", id);

        if (item == null) {
            return null;
        }

        List<AttributeValue> lav = item.getList("subjects");

        for (AttributeValue av : lav) {
            Subject s = new Subject(av.getM().get("id").getS(), av.getM().get("name").getS(), Integer.parseInt(av.getM().get("total_mcq").getS()));
            subs.add(s);
        }
        //Remove duplicate items
        subs = subs.stream().distinct().collect(Collectors.toList());

        return subs;
    }

    public Subject findSubjectByExam(String exam, String subjectId) throws Exception {
        List<Subject> subjects = findById(exam).getSubjects();
        Subject sub = new Subject();
        for (Subject s : subjects) {
            if (s.getId().equals(subjectId)) {
                sub = s;
                break;
            }
        }
        return sub;
    }

    public void addMcqCount(String exam, String subject) throws Exception {
        Exam e = findById(exam);
        for (Subject s : e.getSubjects()) {
            if (s.getId().equals(subject)) {
                s.setTotalMcq(s.getTotalMcq() + 1);
                break;
            }
        }
        createOrUpdate(e);
    }

    public void subtractMcqCount(String exam, String subject) throws Exception {
        Exam e = findById(exam);
        for (Subject s : e.getSubjects()) {
            if (s.getId().equals(subject)) {
                s.setTotalMcq(s.getTotalMcq() - 1);
                break;
            }
        }
        createOrUpdate(e);
    }

    public void updateMcqCount(String exam, String subject) throws Exception {
        Exam e = findById(exam);
        for (Subject s : e.getSubjects()) {
            if (s.getId().equals(subject)) {
                int count = MCQDAO.numberOfRecords(e.getId(), s.getId());
                s.setTotalMcq(count);
                break;
            }
        }
        createOrUpdate(e);
    }

    public void updateExamWiseMCQCount() throws Exception {
        List<Exam> exams = findAll();
        for (Exam e : exams) {
            for (Subject s : e.getSubjects()) {
                int count = MCQDAO.numberOfRecords(e.getId(), s.getId());
                s.setTotalMcq(count);
            }
            createOrUpdate(e);
        }
    }

}
