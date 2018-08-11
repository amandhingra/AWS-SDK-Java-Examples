package com.amazonaws.samples;

import java.io.File;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;



import org.joda.time.DateTime;

import com.amazonaws.AmazonClientException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3Multipart {
	
	
	
	public static void main( String args[]) {
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
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-west-2")
                .build();
        try {
        	
    		String multipartFile= "/home/local/XX/XX/Downloads/ubuntu-16.04.3-desktop-amd64.iso";
			File fileToUpload=convertFromMultiPart(multipartFile);
    		String key = Instant.now().getEpochSecond() + "_" + fileToUpload.getName();
    		
    		s3.putObject(new PutObjectRequest("<BucketName>", key, fileToUpload));
    		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest("<BucketName>", key);
    		generatePresignedUrlRequest.setMethod(HttpMethod.GET);
    		generatePresignedUrlRequest.setExpiration(DateTime.now().plusYears(1).toDate());
    		
    		URL signedUrl = s3.generatePresignedUrl(generatePresignedUrlRequest);
    		
    		String signedUrlString = signedUrl.toString();
    		
    		
    	}catch(Exception e) {}
	}
	
	

}
