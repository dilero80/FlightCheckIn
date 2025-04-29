package com.dilero.jms.checkinapp;

import com.dilero.jms.model.Passenger;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;

public class CheckInApp {
    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
        Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext("airuser","airpass")) {

            JMSProducer producer = jmsContext.createProducer();

            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            Passenger passenger = new Passenger();
            passenger.setPassengerId(123);
            passenger.setFirstName("Bob");
            passenger.setLastName("Smith");
            passenger.setEmail("bob.smith@gmail.com");
            objectMessage.setObject(passenger);


            for (int i = 1; i <= 10; i++) {
                producer.send(requestQueue, objectMessage);
            }

            JMSConsumer consumer = jmsContext.createConsumer(replyQueue);
            MapMessage replyMessage = (MapMessage) consumer.receive(30000);
            System.out.println("CheckInApp received: " + replyMessage);




    }
}
}
