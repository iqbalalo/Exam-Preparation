package examprep.DAOs;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import examprep.entities.MCQ;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MCQDAO {

    static String tableName = "Mcq";
    static DynamoDBService dbs = new DynamoDBService();

    public void create(MCQ mcq) throws Exception {

        Item item = new Item();

        item.withPrimaryKey("id", mcq.getId())
                .withString("question", mcq.getQuestion() + "")
                .withStringSet("answer_options", mcq.getAnswerOptions())
                .withString("correct_answer", mcq.getCorrectAnswer() + "")
                .withString("exam", mcq.getExam() + "")
                .withString("subject", mcq.getSubject() + "")
                .withString("history", mcq.getHistory() + "")
                .withString("note", mcq.getNote() + "");

        if (mcq.getHistory() == null || mcq.getHistory().isEmpty()) {
            item.removeAttribute("history");
        }
        if (mcq.getNote() == null || mcq.getNote().isEmpty()) {
            item.removeAttribute("note");
        }

        Table table = dbs.getDynamoDB().getTable(tableName);
        table.putItem(item);
    }

    public void update(MCQ mcq) throws Exception {

        Item item = new Item();

        item.withPrimaryKey("id", mcq.getId())
                .withString("question", mcq.getQuestion() + "")
                .withStringSet("answer_options", mcq.getAnswerOptions())
                .withString("correct_answer", mcq.getCorrectAnswer() + "")
                .withString("exam", mcq.getExam() + "")
                .withString("subject", mcq.getSubject() + "")
                .withString("history", mcq.getHistory() + "")
                .withString("note", mcq.getNote() + "");

        if (mcq.getHistory() == null || mcq.getHistory().isEmpty()) {
            item.removeAttribute("history");
        }

        if (mcq.getNote() == null || mcq.getNote().isEmpty()) {
            item.removeAttribute("note");
        }

        Table table = dbs.getDynamoDB().getTable(tableName);
        table.putItem(item);
    }

    public void delete(String exam, String id) {
        Table table = dbs.dynamoDB.getTable(tableName);
        table.deleteItem("exam", exam, "id", id);
    }

    public MCQ findById(String exam, String id) throws Exception {
        Table table = dbs.dynamoDB.getTable(tableName);

        Item item = table.getItem("exam", exam, "id", id);

        MCQ q = new MCQ();
        q.setId(item.getString("id"));
        q.setQuestion(item.getString("question"));
        q.setAnswerOptions(item.getStringSet("answer_options").toArray(new String[0]));
        q.setCorrectAnswer(item.getString("correct_answer"));
        q.setExam(item.getString("exam"));
        q.setSubject(item.getString("subject"));
        q.setHistory(item.getString("history"));
        q.setNote(item.getString("note"));

        return q;
    }

    public List<MCQ> findAll(String exam, String subject) throws Exception {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":subject", new AttributeValue().withS(subject));

        List<MCQ> mcqs = new ArrayList();
        Map<String, AttributeValue> lastKeyEvaluated = null;

        do {
            QueryRequest queryRequest = new QueryRequest()
                    .withTableName(tableName)
                    .addKeyConditionsEntry("exam", new Condition().withAttributeValueList(new AttributeValue(exam)).withComparisonOperator(ComparisonOperator.EQ))
                    .withFilterExpression("subject = :subject")
                    .withExpressionAttributeValues(expressionAttributeValues)
                    .withExclusiveStartKey(lastKeyEvaluated);

            QueryResult result = dbs.getDynamoDBClient().query(queryRequest);

            for (Map<String, AttributeValue> item : result.getItems()) {

                MCQ q = new MCQ();
                q.setId(item.get("id").getS());
                q.setQuestion(item.get("question").getS());
                q.setAnswerOptions(item.get("answer_options").getSS().toArray(new String[0]));
                q.setCorrectAnswer(item.get("correct_answer").getS());
                q.setExam(item.get("exam").getS());
                q.setSubject(item.get("subject").getS());

                if (item.containsKey("history")) {
                    q.setHistory(item.get("history").getS());
                }

                if (item.containsKey("note")) {
                    q.setNote(item.get("note").getS());
                }

                mcqs.add(q);
            }

            lastKeyEvaluated = result.getLastEvaluatedKey();
        }
        while (lastKeyEvaluated != null);

        return mcqs;
    }

    public List<MCQ> findAllByExam(String exam) throws Exception {

        List<MCQ> mcqs = new ArrayList();
        Map<String, AttributeValue> lastKeyEvaluated = null;

        do {
            QueryRequest queryRequest = new QueryRequest()
                    .withTableName(tableName)
                    .addKeyConditionsEntry("exam", new Condition().withAttributeValueList(new AttributeValue(exam)).withComparisonOperator(ComparisonOperator.EQ))
                    .withExclusiveStartKey(lastKeyEvaluated);

            QueryResult result = dbs.getDynamoDBClient().query(queryRequest);

            for (Map<String, AttributeValue> item : result.getItems()) {

                MCQ q = new MCQ();
                q.setId(item.get("id").getS());
                q.setQuestion(item.get("question").getS());
                q.setAnswerOptions(item.get("answer_options").getSS().toArray(new String[0]));
                q.setCorrectAnswer(item.get("correct_answer").getS());
                q.setExam(item.get("exam").getS());
                q.setSubject(item.get("subject").getS());

                if (item.containsKey("history")) {
                    q.setHistory(item.get("history").getS());
                }

                if (item.containsKey("note")) {
                    q.setNote(item.get("note").getS());
                }

                mcqs.add(q);
            }

            lastKeyEvaluated = result.getLastEvaluatedKey();
        }
        while (lastKeyEvaluated != null);

        return mcqs;
    }

    public List<MCQ> search(List<MCQ> mcqs, String keyword) {
        List<MCQ> filteredMCQ = new ArrayList();
        for (MCQ q : mcqs) {

            if (q.getQuestion().toLowerCase().contains(keyword)
                    || Arrays.toString(q.getAnswerOptions()).toLowerCase().contains(keyword)
                    || (q.getHistory() != null && q.getHistory().toLowerCase().contains(keyword))) {

                filteredMCQ.add(q);
            }
        }
        return filteredMCQ;
    }

    public static int numberOfRecords(String exam) {
        Map<String, AttributeValue> lastKeyEvaluated = null;

        int recordCount = 0;
        do {
            QueryRequest queryRequest = new QueryRequest()
                    .withTableName(tableName)
                    .addKeyConditionsEntry("exam", new Condition().withAttributeValueList(new AttributeValue(exam)).withComparisonOperator(ComparisonOperator.EQ))
                    .withProjectionExpression("id")
                    .withExclusiveStartKey(lastKeyEvaluated);

            QueryResult result = dbs.getDynamoDBClient().query(queryRequest);

            recordCount += result.getCount();
            lastKeyEvaluated = result.getLastEvaluatedKey();
        }
        while (lastKeyEvaluated != null);

        return recordCount;
    }

    public static int numberOfRecords(String exam, String subject) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":subject", new AttributeValue().withS(subject));

        Map<String, AttributeValue> lastKeyEvaluated = null;

        int recordCount = 0;
        do {
            QueryRequest queryRequest = new QueryRequest()
                    .withTableName(tableName)
                    .addKeyConditionsEntry("exam", new Condition().withAttributeValueList(new AttributeValue(exam)).withComparisonOperator(ComparisonOperator.EQ))
                    .withProjectionExpression("id")
                    .withFilterExpression("subject = :subject")
                    .withExpressionAttributeValues(expressionAttributeValues)
                    .withExclusiveStartKey(lastKeyEvaluated);

            QueryResult result = dbs.getDynamoDBClient().query(queryRequest);

            recordCount += result.getCount();
            lastKeyEvaluated = result.getLastEvaluatedKey();
        }
        while (lastKeyEvaluated != null);

        return recordCount;
    }

    public int numberOfRecords() {
        Map<String, AttributeValue> lastKeyEvaluated = null;

        int recordCount = 0;
        do {
            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(tableName)
                    .withProjectionExpression("id")
                    .withExclusiveStartKey(lastKeyEvaluated);

            ScanResult result = dbs.getDynamoDBClient().scan(scanRequest);

            recordCount += result.getCount();
            lastKeyEvaluated = result.getLastEvaluatedKey();
        }
        while (lastKeyEvaluated != null);

        return recordCount;
    }

    public long numberOfRecordsApprox() {
        Table t = dbs.getDynamoDB().getTable(tableName);
        Long count = t.describe().getItemCount();
        return count;
    }

}
