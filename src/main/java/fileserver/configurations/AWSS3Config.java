package fileserver.configurations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import fileserver.helpers.SSLManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSS3Config {

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public AmazonS3 getAmazonS3Cient() throws Exception {
        SSLManager.clearSslProperties();
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .build();
    }
}