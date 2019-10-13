package com.mr.study.rabbit.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author zhanxp
 * @version 1.0 2019/6/29
 */
public class ReceiveLogsDirect {
    private static final String EXCHANGE_NAME = "direct_logs";
    private static final String QUEUE_NAME = "info";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare(QUEUE_NAME, false, false, false, null).getQueue();
        System.out.println(queueName);

        //接受routingKey为warning和error的消息
        channel.queueBind(queueName, EXCHANGE_NAME, "warning");
        channel.queueBind(queueName, EXCHANGE_NAME, "error");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
