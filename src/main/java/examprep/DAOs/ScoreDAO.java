package examprep.DAOs;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import examprep.entities.Score;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreDAO {

    static String tableName = "Score";
    static DynamoDBService dbs = new DynamoDBService();

    public void createAndUpdate(Score score) throws Exception {

        Item item = new Item();

        item.withPrimaryKey("userId", score.getUserId())
                .withString("examDateTime", score.getExamDateTime() + "")
                .withString("exam", score.getExam())
                .withString("subject", score.getSubject() + "")
                .withString("obtainedMark", score.getObtainedMark() + "")
                .withString("totalMark", score.getTotalMark() + "")
                .withString("timeTaken", score.getTimeTaken() + "");

        Table table = dbs.getDynamoDB().getTable(tableName);
        table.putItem(item);
    }

    public void delete(String userId) {
        Table table = dbs.dynamoDB.getTable(tableName);
        table.deleteItem("userId", userId);
    }

    public List<Score> findAll(String userId) throws Exception {
        List<Score> scores = new ArrayList();
        Map<String, AttributeValue> lastKeyEvaluated = null;

        do {
            QueryRequest queryRequest = new QueryRequest()
                    .withTableName(tableName)
                    .addKeyConditionsEntry("userId", new Condition().withAttributeValueList(new AttributeValue(userId)).withComparisonOperator(ComparisonOperator.EQ))
                    .withExclusiveStartKey(lastKeyEvaluated);

            QueryResult result = dbs.getDynamoDBClient().query(queryRequest);

            for (Map<String, AttributeValue> item : result.getItems()) {

                Score s = new Score();
                s.setUserId(item.get("userId").getS());
                s.setExamDateTime(item.get("examDateTime").getS());
                s.setExam(item.get("exam").getS());
                s.setSubject(item.get("subject").getS());
                s.setTotalMark(item.get("totalMark").getS());
                s.setObtainedMark(item.get("obtainedMark").getS());
                s.setTimeTaken(item.get("timeTaken").getS());

                scores.add(s);
            }

            lastKeyEvaluated = result.getLastEvaluatedKey();
        }
        while (lastKeyEvaluated != null);

        return scores;
    }
}
