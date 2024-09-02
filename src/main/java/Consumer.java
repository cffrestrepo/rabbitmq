import com.rabbitmq.client.*;

public class Consumer {
    private final static String QUEUE_NAME = "Rabbit";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        readMessageCicle(factory);
    }

    private static void readMessageCicle(ConnectionFactory factory) throws Exception {
        while (true) {
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                };

                channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
                });

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }
}