package cc.foxtail.new_version;

import android.graphics.BitmapFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Callable;



public class S3Connect implements Callable<Object> {
    //S3 연결을 위한 정보
    private final String BUCKET = "dodamimage";
    private final String ACCESS_KEY = "ooooooooooooooooo";
    private final String SECRET_KEY = "ooooooooooooooooo";

    //S3 image 통신용
    private int code;
    private AWSCredentials awsCredentials;
    private AmazonS3 dodamS3;
    private File imageData;
    private String imageName;

    S3Connect(int c, String link, int id) {
        code = c;
        imageData = new File(link);
        imageName = id+"_image.jpg";
    }

    S3Connect(int c, int id) {
        code = c;
        imageName = id+"_image.jpg";
    }

    @Override
    public Object call() throws Exception {
        awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        dodamS3 = new AmazonS3Client(awsCredentials);

        if (dodamS3 != null) {
            switch (code) {
                case 0:
                    try {
                        PutObjectRequest objectRequest = new PutObjectRequest(BUCKET, imageName, imageData);
                        dodamS3.putObject(objectRequest);
                        return true;
                    } catch (AmazonServiceException e) {
                        return false;
                    }
                case 1:
                    try {
                        GetObjectRequest objectRequest = new GetObjectRequest(BUCKET, imageName);
                        S3Object image = dodamS3.getObject(objectRequest);

                        InputStream imageData = image.getObjectContent();
                        return BitmapFactory.decodeStream(imageData);
                    } catch (AmazonServiceException e) {
                        return null;
                    }
            }
        }

        return null;
    }
}
