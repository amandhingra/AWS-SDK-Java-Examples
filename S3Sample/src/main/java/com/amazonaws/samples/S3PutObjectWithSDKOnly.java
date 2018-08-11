package com.amazonaws.samples;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.joda.time.DateTime;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.UploadObjectRequest;

public class S3PutObjectWithSDKOnly {

	public static String bucketName = "<BucketName>";
	public static String key = "keys.txt";
    public static File file=new File("/home/local/XX/XX/Downloads/keys.txt");

	
	
	
	
	public static void main(String[] arg) throws IOException{
		AWSCredentials credentials = S3Controller.authenticateAWS();
		
		AmazonS3 s3 = S3Controller.getClient(credentials);
		try {
			s3.putObject(new PutObjectRequest(bucketName, key, file));
			System.out.println("PUT Object Completed");
		}
		catch(AmazonServiceException e) {
			System.out.println("Error Msg: "+ e.getMessage());
			System.out.println("ResponseCode: "+e.getStatusCode());
			System.out.println("AWS Error Code: "+ e.getErrorCode());
			System.out.println("ErrorType: "+e.getErrorType());
			System.out.println("RequestID: "+ e.getRequestId());
			
		}
		catch(AmazonClientException ace) {
			System.out.println("Error Message: "+ace.getMessage());
		}

		
	}
}
