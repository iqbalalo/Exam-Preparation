package examprep.DAOs;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import examprep.entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDAO {

    String tableName;
    DynamoDBService dbs;

    public UserDAO() {
        dbs = new DynamoDBService();
        tableName = "User";

    }

    public void create(User user) throws Exception {

        Item item = new Item();

        item.withPrimaryKey("id", user.getId() + "")
                .withString("name", user.getName() + "")
                .withString("type", user.getType() + "")
                .withString("password", user.getPassword() + "");

        Table table = dbs.getDynamoDB().getTable(tableName);
        table.putItem(item);
    }

    public User findById(String id) throws Exception {
        Table table = dbs.dynamoDB.getTable(tableName);

        Item item = table.getItem("id", id);

        if (item == null) {
            return null;
        }

        User u = new User();
        u.setId(item.getString("id"));
        u.setName(item.getString("name"));
        u.setType(item.getString("type"));
        u.setPassword(item.getString("password"));
        return u;
    }

    public List<User> findAll() throws Exception {
        List<User> users = new ArrayList();
        Map<String, AttributeValue> lastKeyEvaluated = null;

        do {
            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(tableName)
                    .withExclusiveStartKey(lastKeyEvaluated);

            ScanResult result = dbs.getDynamoDBClient().scan(scanRequest);

            for (Map<String, AttributeValue> item : result.getItems()) {

                User u = new User();
                u.setId(item.get("id").getS());
                u.setName(item.get("name").getS());
                u.setType(item.get("type").getS());
                u.setPassword(item.get("password").getS());

                users.add(u);
            }

            lastKeyEvaluated = result.getLastEvaluatedKey();
        }
        while (lastKeyEvaluated != null);

        return users;
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
        return t.describe().getItemCount();
    }

    public void delete(String id) {
        Table table = dbs.dynamoDB.getTable(tableName);
        table.deleteItem("id", id);
    }
}
