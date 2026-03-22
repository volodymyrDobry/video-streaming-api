package com.viora.spi.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import static com.viora.spi.activemq.ActiveMQProperties.*;

@Slf4j
public class ActiveMQProducer {

    private final ConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper;

    public ActiveMQProducer() {
        this.connectionFactory =
                new ActiveMQConnectionFactory(ACTIVEMQ_USERNAME, ACTIVEMQ_PASSWORD, ACTIVEMQ_BROKER_URL);
        this.objectMapper = new ObjectMapper();
    }

    public void convertAndSend(String queueName, Object message) {
        TextMessage textMessage = convert(message);
        send(queueName, textMessage);
    }

    public void send(String queueName, TextMessage message) {
        execute(session -> {
            Destination destination = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(destination);
            producer.send(message);
        });
    }

    private void execute(ProducerOperation<Session> operation) {
        Connection connection = null;
        Session session = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            operation.accept(session);
        } catch (JMSException e) {
            log.error("Error during sending message", e);
        } finally {
            closeConnection(connection);
            closeSession(session);
        }
    }

    private void closeConnection(Connection connection) {

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (JMSException e) {
            log.error("Error during closing connection", e);
        }
    }

    private void closeSession(Session session) {
        if (session == null) return;
        try {
            session.close();
        } catch (JMSException e) {
            log.error("Error during closing session", e);
        }
    }

    private TextMessage convert(Object message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            TextMessage textMessage = new ActiveMQTextMessage();
            textMessage.setText(json);
            return textMessage;
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message to JSON", e);
            throw new RuntimeException(e);
        } catch (JMSException e) {
            log.error("Failed to create text message", e);
            throw new RuntimeException(e);
        }
    }

}
