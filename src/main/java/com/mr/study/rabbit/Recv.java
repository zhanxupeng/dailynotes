package com.mr.study.rabbit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanxp
 * @version 1.0 2019/6/17
 */
public class Recv {
    private final static String QUEUE_NAME = "hello.august";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };


//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//
//            System.out.println(" [x] Received '" + message + "'");
//            try {
//                doWork(message);
//            } finally {
//                System.out.println(" [x] Done");
//                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//            }
//        };

        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
