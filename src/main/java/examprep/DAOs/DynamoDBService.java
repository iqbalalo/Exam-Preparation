package examprep.DAOs;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class DynamoDBService {

    AmazonDynamoDBClient dynamoDBClient;
    DynamoDB dynamoDB;

    String awsAccessKey;
    String awsSecretKey;

    public DynamoDBService() {
        awsAccessKey = "AKIAJTEQK623SWYAMJ2Q";
        awsSecretKey = "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";

        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        dynamoDBClient = new AmazonDynamoDBClient(credentials);

        Region region = Region.getRegion(Regions.US_WEST_2);
        dynamoDBClient.setRegion(region);
        dynamoDB = new DynamoDB(dynamoDBClient);
    }

    public DynamoDB getDynamoDB() {
        return dynamoDB;
    }

    public AmazonDynamoDBClient getDynamoDBClient() {
        return dynamoDBClient;
    }

}
