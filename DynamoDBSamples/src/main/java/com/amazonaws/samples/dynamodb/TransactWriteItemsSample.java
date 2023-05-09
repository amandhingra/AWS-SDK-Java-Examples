package com.amazonaws.samples.dynamodb;



//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.model.TransactionCanceledException;


import java.net.URI;

public class TransactWriteItemsSample {
    private static final Logger logger = LogManager.getLogger(TransactWriteItemsSample.class);
    static Region region = Region.EU_WEST_1;

    static DynamoDbClient ddb = DynamoDbClient.builder()
            .region(region)
            .credentialsProvider(DefaultCredentialsProvider.builder().build())
            .build();

    static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build();

    static DynamoDbTable<SourceTable> table = enhancedClient.table("stream-iteratorage-test", TableSchema.fromBean(SourceTable.class));
    public static void main(String[] args) {
        TransactWriteItemsEnhancedRequest.Builder transactWriteItemsEnhancedRequest = TransactWriteItemsEnhancedRequest.builder();
        TransactWriteItemsEnhancedRequest tc = addItems(transactWriteItemsEnhancedRequest);

        for (int i = 0; i < tc.transactWriteItems().size(); i++) {
            logger.info(tc.transactWriteItems().get(i).toString());
        }
        try{
            enhancedClient.transactWriteItems(tc);
            System.out.println("foo");
        }
        catch (TransactionCanceledException e){
            System.out.println(e.cancellationReasons());
        }
    }
    public static TransactWriteItemsEnhancedRequest addItems(TransactWriteItemsEnhancedRequest.Builder tx){
        for(int i=0;i<100;i++){
            tx.addPutItem(table, new SourceTable(String.valueOf(i)));
        }
        return tx.build();
    }
}
