package com.amazonaws.samples;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;

public class S3MultipartWithSDKOnly {


    
	public static AWSCredentials authenticateAWS() {
		AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/home/local/XX/XX/.aws/credentials), and is in valid format.",
                    e);
        }
        return credentials;
	}
	
	
	public static void main(String[] args) {
		AWSCredentials credentials = authenticateAWS();
		String bucketName="<BucketName>";
	    long partSize=5*1024*1024;
	    
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-west-2")
                .build();
        File file=new File("/home/local/XX/XX/Downloads/ubuntu-16.04.3-desktop-amd64.iso");
        String key="Downloads/ubuntu-16.04.3-desktop-amd64.iso";
        long contentLength= file.length();
        long bytePosition = 0;
        List<PartETag> partETags = new ArrayList<PartETag>();
        
        List<CopyPartResult> copyResponses = new ArrayList<CopyPartResult>();
        InitiateMultipartUploadRequest initiateRequest = new InitiateMultipartUploadRequest(bucketName, key);
        InitiateMultipartUploadResult initResult = s3.initiateMultipartUpload(initiateRequest);
        
        try {
        	for(int i =0;bytePosition<partSize; i++) {
        		UploadPartRequest uploadRequest = new UploadPartRequest()
        				.withBucketName(bucketName)
        				.withKey(key)
        				.withUploadId(initResult.getUploadId()).withPartNumber(i)
        				.withFile(file)
        				.withPartSize(partSize);
        		
        	}
        }
        
        
        
        
        

	}

}
