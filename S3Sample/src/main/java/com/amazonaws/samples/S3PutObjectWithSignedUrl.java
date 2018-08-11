package com.amazonaws.samples;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public class S3PutObjectWithSignedUrl {

	public static void main(String[] args) throws IOException {
		AWSCredentials credentials = S3Controller.authenticateAWS();
		AmazonS3 s3=S3Controller.getClient(credentials);
		String bucketName = "<BucketName>";
		String key = "keys.txt";
		Path path = Paths.get("/home/local/XX/XX/Downloads/keys.txt");
		
		byte[] file = Files.readAllBytes(path);
		
		try {
			java.util.Date expiration = new java.util.Date();
			long milliSeconds = expiration.getTime();
			expiration.setTime(milliSeconds);
			
			GeneratePresignedUrlRequest myReq = new GeneratePresignedUrlRequest(bucketName, key);
			myReq.setMethod(HttpMethod.PUT);
			myReq.setExpiration(new Date(System.currentTimeMillis()+ (60*60*1000)));
			myReq.setContentType("application/octet-stream");
			URL url = s3.generatePresignedUrl(myReq);
			System.out.println(url);
			
			HttpURLConnection con=(HttpURLConnection)url.openConnection();
			con.setRequestMethod("PUT");
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/octet-stream"); 
			OutputStream output= con.getOutputStream();
			InputStream input = new ByteArrayInputStream(file);
			byte[] buffer = new byte[4096];
		    int length;
		    while ((length = input.read(buffer)) > 0) {
		        output.write(buffer, 0, length);
		    }
		    output.flush();

		    System.out.println(con.getResponseCode());
			
			
		}
		catch(AmazonServiceException ase) {
			System.out.println("Error Details: "+ase.getErrorMessage()+'\n'
					+ase.getStatusCode()+'\n'
					+ase.getErrorCode()+'\n'
					+ase.getRequestId()
					+ase.getErrorType());
		}
		catch(AmazonClientException ace) {
			System.out.println("Error Details: "+ ace.getMessage()+'\n');
		}
		catch(Exception e ) {
			System.out.println(e.getMessage());
		}
		

	}

}
