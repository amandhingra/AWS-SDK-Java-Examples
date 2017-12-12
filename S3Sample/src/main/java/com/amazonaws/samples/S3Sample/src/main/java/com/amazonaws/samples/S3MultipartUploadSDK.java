package com.amazonaws.samples;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

public class S3MultipartUploadSDK {
	static AWSCredentials credentials=S3Controller.authenticateAWS();
	static AmazonS3 s3=S3Controller.getClient(credentials, "eu-west-1");
	
	static String bucketName = "<bucketName>";
	public static void main(String arg[]) {
		System.out.println(bucketName);
		File file=new File("/Downloads/nmap-7.60.dmg");
		String key="Downloads/nmap-7.60.dmg";
		long contentLength=file.length();
		long partSize=5*1024*1024;
		List<PartETag> partETags = new ArrayList<PartETag>();
		//Step 1
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName,key);
	        InitiateMultipartUploadResult initResponse = s3.initiateMultipartUpload(initRequest);
	        
	        
		try {
            // Step 2: Upload parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Last part can be less than 5 MB. Adjust part size.
            	partSize = Math.min(partSize, (contentLength - filePosition));
            	
                // Create request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                    .withBucketName(bucketName).withKey(key)
                    .withUploadId(initResponse.getUploadId()).withPartNumber(i)
                    .withFileOffset(filePosition)
                    .withFile(file)
                    .withPartSize(partSize);

                // Upload part and add response to our list.
                partETags.add(s3.uploadPart(uploadRequest).getPartETag());

                filePosition += partSize;
            }
            CompleteMultipartUploadRequest compRequest = new 
                    CompleteMultipartUploadRequest(
                               bucketName, 
                               key, 
                               initResponse.getUploadId(), 
                               partETags);

            s3.completeMultipartUpload(compRequest);
		} catch (Exception e) {
            s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, key, initResponse.getUploadId()));
        }
	}
	

}
