package com.github.diegopacheco.sandbox.java.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class PubSubApp {
	public static void main(String[] args) throws Throwable {
			
			// publish
		
			String EXCHANGE_NAME = "logs";
		
		    ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("10.99.4.17");
			factory.setPort(5672);
			factory.setTopologyRecoveryEnabled(true);
			factory.setUsername("admin");
			factory.setPassword("admin");
	        Connection connection = factory.newConnection();
	        Channel channel = connection.createChannel();

	        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

	        String message = "This is a simple test";

	        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
	        System.out.println(" [x] Sent '" + message + "'");

	        channel.close();
	        connection.close();
	        
	        // subscribe
	        
	        factory = new ConnectionFactory();
	        factory.setHost("10.99.4.17");
			factory.setPort(5672);
			factory.setTopologyRecoveryEnabled(true);
			factory.setUsername("admin");
			factory.setPassword("admin");
	        connection = factory.newConnection();
	        channel = connection.createChannel();

	        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	        String queueName = channel.queueDeclare().getQueue();
	        channel.queueBind(queueName, EXCHANGE_NAME, "");

	        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	        Consumer consumer = new DefaultConsumer(channel) {
	          @Override
	          public void handleDelivery(String consumerTag, Envelope envelope,
	                                     AMQP.BasicProperties properties, byte[] body) throws IOException {
	            String message = new String(body, "UTF-8");
	            System.out.println(" [x] Received '" + message + "'");
	          }
	        };
	        channel.basicConsume(queueName, true, consumer);
		
	}
}
