package com.mr.study.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeoutException;

/**
 * @author zhanxp
 * @version 1.0 2019/6/17
 */
public class Send {
    private final static String QUEUE_NAME = "hello.august";

    public static void main(String[] argv)
            throws java.io.IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "占旭鹏测试2";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("send message: " + message);

        channel.close();
        connection.close();
    }
}
