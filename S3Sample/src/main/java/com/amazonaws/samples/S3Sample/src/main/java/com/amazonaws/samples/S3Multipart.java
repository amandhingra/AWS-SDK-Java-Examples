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
		AWSCredentials credentials = S3Controller.authenticateAWS();
        AmazonS3 s3 = S3Controller.getClient(credentials, "eu-west-1");
        try {
        	
    		String multipartFile= "<path to file>";
			File fileToUpload=convertFromMultiPart(multipartFile);
    		String key = Instant.now().getEpochSecond() + "_" + fileToUpload.getName();
    		
    		s3.putObject(new PutObjectRequest("<bucketname>", key, fileToUpload));
    		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest("<bucketname>", key);
    		generatePresignedUrlRequest.setMethod(HttpMethod.GET);
    		generatePresignedUrlRequest.setExpiration(DateTime.now().plusYears(1).toDate());
    		
    		URL signedUrl = s3.generatePresignedUrl(generatePresignedUrlRequest);
    		
    		String signedUrlString = signedUrl.toString();
    		
    		
    	}catch(Exception e) {}
	}
	
	

}
