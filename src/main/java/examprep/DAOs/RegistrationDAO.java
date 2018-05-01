package examprep.DAOs;

import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import examprep.entities.Registration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RegistrationDAO {

    String tableName;
    DynamoDBService dbs;

    public RegistrationDAO() {
        dbs = new DynamoDBService();
        tableName = "Registration";
    }

    public void create(Registration reg) throws Exception {

        Item item = new Item();

        item.withPrimaryKey("id", reg.getId() + "")
                .withString("tx_date", reg.getTxDate() + "")
                .withString("tx_amount", reg.getTxAmount() + "")
                .withString("device_id", reg.getDeviceId() + "")
                .withString("exam", reg.getExam() + "")
                .withString("reg_date", reg.getRegDate() + "")
                .withString("phone", reg.getPhone() + "")
                .withString("access_key", reg.getAccessKey() + "");

        if (reg.getTxDate() == null || reg.getTxDate().isEmpty()) {
            item.removeAttribute("tx_date");
        }
        if (reg.getTxAmount() == null || reg.getTxAmount().isEmpty()) {
            item.removeAttribute("tx_amount");
        }
        if (reg.getDeviceId() == null || reg.getDeviceId().isEmpty()) {
            item.removeAttribute("device_id");
        }
        if (reg.getPhone() == null || reg.getPhone().isEmpty()) {
            item.removeAttribute("phone");
        }
        if (reg.getExam() == null || reg.getExam().isEmpty()) {
            item.removeAttribute("exam");
        }
        if (reg.getRegDate() == null || reg.getRegDate().isEmpty()) {
            item.removeAttribute("reg_date");
        }
        if (reg.getAccessKey() == null || reg.getAccessKey().isEmpty()) {
            item.removeAttribute("access_key");
        }

        Table table = dbs.getDynamoDB().getTable(tableName);
        table.putItem(item);
    }

    public Registration findById(String id) throws Exception {
        Table table = dbs.dynamoDB.getTable(tableName);

        Item item = table.getItem("id", id);

        if (item == null) {
            return null;
        }

        Registration r = new Registration();
        r.setId(item.getString("id"));
        r.setTxDate(item.getString("tx_date"));
        r.setTxAmount(item.getString("tx_amount"));
        r.setDeviceId(item.getString("device_id"));
        r.setExam(item.getString("exam"));
        r.setRegDate(item.getString("reg_date"));
        r.setPhone(item.getString("phone"));
        r.setAccessKey(item.getString("access_key"));
        return r;
    }

    public List<Registration> findByPhone(String phone) throws Exception {
        Table table = dbs.dynamoDB.getTable(tableName);
        Index index = table.getIndex("phone-id-index");

        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("phone = :phone")
                .withValueMap(new ValueMap()
                        .withString(":phone", phone));

        ItemCollection<QueryOutcome> items = index.query(spec);
        Iterator<Item> iter = items.iterator();
        List<Registration> regs = new ArrayList();
        while (iter.hasNext()) {
            Item item = iter.next();
            Registration r = new Registration();
            r.setId(item.getString("id"));
            r.setTxDate(item.getString("tx_date"));
            r.setTxAmount(item.getString("tx_amount"));
            r.setDeviceId(item.getString("device_id"));
            r.setExam(item.getString("exam"));
            r.setRegDate(item.getString("reg_date"));
            r.setPhone(item.getString("phone"));
            r.setAccessKey(item.getString("access_key"));
            regs.add(r);
        }
        return regs;
    }

    public Registration findByAccessKey(String key) throws Exception {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":key", new AttributeValue().withS(key));

        Map<String, AttributeValue> lastKeyEvaluated = null;

        Registration r = null;

        do {
            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(tableName)
                    .withFilterExpression("access_key = :key")
                    .withExpressionAttributeValues(expressionAttributeValues)
                    .withExclusiveStartKey(lastKeyEvaluated);

            ScanResult result = dbs.getDynamoDBClient().scan(scanRequest);

            if (result.getCount() == 1) {
                Map<String, AttributeValue> item = result.getItems().get(0);
                r = new Registration();
                r.setId(item.get("id").getS());
                r.setTxDate(item.get("tx_date").getS());
                r.setTxAmount(item.get("tx_amount").getS());
                r.setDeviceId(item.get("device_id").getS());
                r.setExam(item.get("exam").getS());
                r.setRegDate(item.get("reg_date").getS());
                r.setPhone(item.get("phone").getS());
                r.setAccessKey(item.get("access_key").getS());
            }

            lastKeyEvaluated = result.getLastEvaluatedKey();
        }
        while (lastKeyEvaluated != null);
        return r;
    }

    public List<Registration> findAll() throws Exception {

        List<Registration> regs = new ArrayList();
        Map<String, AttributeValue> lastKeyEvaluated = null;

        do {
            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(tableName)
                    .withExclusiveStartKey(lastKeyEvaluated);

            ScanResult result = dbs.getDynamoDBClient().scan(scanRequest);

            for (Map<String, AttributeValue> item : result.getItems()) {

                Registration r = new Registration();
                r.setId(item.get("id").getS());

                if (item.containsKey("tx_date")) {
                    r.setTxDate(item.get("tx_date").getS());
                }
                if (item.containsKey("tx_amount")) {
                    r.setTxAmount(item.get("tx_amount").getS());
                }
                if (item.containsKey("device_id")) {
                    r.setDeviceId(item.get("device_id").getS());
                }
                if (item.containsKey("exam")) {
                    r.setExam(item.get("exam").getS());
                }
                if (item.containsKey("reg_date")) {
                    r.setRegDate(item.get("reg_date").getS());
                }
                if (item.containsKey("phone")) {
                    r.setPhone(item.get("phone").getS());
                }
                if (item.containsKey("access_key")) {
                    r.setAccessKey(item.get("access_key").getS());
                }
                regs.add(r);
            }

            lastKeyEvaluated = result.getLastEvaluatedKey();
        }
        while (lastKeyEvaluated != null);

        return regs;
    }

    public List<Registration> search(List<Registration> regs, String keyword) {
        List<Registration> filteredList = new ArrayList();
        for (Registration r : regs) {

            if ((r.getDeviceId() != null && r.getDeviceId().toLowerCase().contains(keyword))
                    || r.getId().toLowerCase().contains(keyword)
                    || (r.getExam() != null && r.getExam().toLowerCase().contains(keyword))
                    || (r.getTxDate() != null && r.getTxDate().contains(keyword))
                    || (r.getPhone() != null && r.getPhone().contains(keyword))) {

                filteredList.add(r);
            }
        }
        return filteredList;
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
