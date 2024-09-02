import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.Charset;
import java.util.Random;

public class Producer {
    private final static String QUEUE_NAME = "Rabbit";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        messageCicle(factory);
    }

    private static void messageCicle(ConnectionFactory factory) throws Exception {
        while (true) {
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                String message = getMessage();
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println(" [x] Sent '" + message + "'");

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }

    private static String getMessage() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}