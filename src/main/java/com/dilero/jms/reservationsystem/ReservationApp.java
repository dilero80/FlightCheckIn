package com.dilero.jms.reservationsystem;

import com.dilero.jms.model.Passenger;
import com.dilero.jms.reservationsystem.listeners.ReservationListener;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;

public class ReservationApp {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");


        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext("airuser","airpass")) {

            JMSConsumer consumer  = jmsContext.createConsumer(requestQueue);
            consumer.setMessageListener(new ReservationListener());

            Thread.sleep(10000);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

