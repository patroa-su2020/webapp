package com.patro.SpringBootProject.service;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.patro.SpringBootProject.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;

@Service
public class EmailService {
    @Value("${SNS_TOPIC_ARN}")
    private String topicArn;

    @Value("${amazonProperties.profile}")
    String profile;

    @Value("${amazonProperties.region}")
    String region;

    final SNSService message = new SNSService();



    public void emailPasswordResetLink(User user)
    {
        //AmazonSNS sns = AmazonSNSClientBuilder.standard().withCredentials(new InstanceProfileCredentialsProvider(true)).build();
//        String topicArn = sns.createTopic("email_request").getTopicArn();
        AWSCredentialsProviderChain awsCredentialsProviderChain = new AWSCredentialsProviderChain(
                new InstanceProfileCredentialsProvider(true),
                new ProfileCredentialsProvider(profile));
        AmazonSNS sns = AmazonSNSClientBuilder.standard().withCredentials(awsCredentialsProviderChain).withRegion(region).build();
        String msgId = message.publish(sns,topicArn, user.getUsername());
    }
}
