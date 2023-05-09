package com.amazonaws.samples.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class SourceTable {

    private String id;
    private String attribute_a;

    public SourceTable () {

    }
    public SourceTable(String id){
        setId(id);
    }

    // Partition Keys
    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}
