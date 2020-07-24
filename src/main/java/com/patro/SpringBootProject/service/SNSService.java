package com.patro.SpringBootProject.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.Map;

public class SNSService {

    private String message;
    private Map<String, MessageAttributeValue> messageAttributes;

    public String publish(final AmazonSNS snsClient, final String topicArn, String email) {
        final PublishRequest request = new PublishRequest(topicArn, email);
//                .withMessageAttributes(messageAttributes);
        final PublishResult result = snsClient.publish(request);
        return result.getMessageId();
    }
}
