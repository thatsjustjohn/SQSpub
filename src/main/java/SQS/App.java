/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package SQS;

// https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/standard-queues-getting-started-java.html

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;

import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        final String queueSubURL = "https://sqs.us-west-2.amazonaws.com/283346001770/";
        String[] queues = {"QueueA", "QueueB", "QueueC"};
        try {
            // Need at least 2 arguments and validate the first 2 are within bounds
            if(args.length < 2) throw new IllegalArgumentException();
            int sendOrReceive = Integer.parseInt(args[0]);
            if(sendOrReceive > 2 || sendOrReceive < 1) throw new IllegalArgumentException();
            int whichQueue = Integer.parseInt(args[1]);
            if(whichQueue > 2 || whichQueue < 0) throw new IllegalArgumentException();

            if(sendOrReceive == 1) {
                // Check string argument
                if(args.length != 3) throw new IllegalArgumentException();
                if(args[2] == null) throw new IllegalArgumentException();
                // Send a message.
                sendMessage(queueSubURL, queues[whichQueue], args[2]);
            }else if(sendOrReceive == 2){// Receive message
                receiveMessage(queueSubURL, queues[whichQueue]);
            }else throw new IllegalArgumentException();

        } catch (final AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means " +
                    "your request made it to Amazon SQS, but was " +
                    "rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (final AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means " +
                    "the client encountered a serious internal problem while " +
                    "trying to communicate with Amazon SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    public static void sendMessage(String queueSubURL, String queue, String message){
        final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        // Send a message.
        System.out.println("Sending a message to A Queue.\n");
        sqs.sendMessage(new SendMessageRequest(queueSubURL + queue, message));
    }

    public static String receiveMessage(String queueSubURL, String queue){
        final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        // Receive messages.
        System.out.println("Receiving messages from MyQueue.\n");
        final ReceiveMessageRequest receiveMessageRequest =
                new ReceiveMessageRequest(queueSubURL + queue);
        final List<Message> messages = sqs.receiveMessage(receiveMessageRequest)
                .getMessages();
        for (final Message message : messages) {
            System.out.println("Message");
            System.out.println("  MessageId:     "
                    + message.getMessageId());
            System.out.println("  ReceiptHandle: "
                    + message.getReceiptHandle());
            System.out.println("  MD5OfBody:     "
                    + message.getMD5OfBody());
            System.out.println("  Body:          "
                    + message.getBody());
            for (final Map.Entry<String, String> entry : message.getAttributes()
                    .entrySet()) {
                System.out.println("Attribute");
                System.out.println("  Name:  " + entry
                        .getKey());
                System.out.println("  Value: " + entry
                        .getValue());
            }
            return message.getBody();
        }
        return null;
    }
}
