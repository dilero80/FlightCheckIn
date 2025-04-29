package com.dilero.jms.reservationsystem.listeners;

import com.dilero.jms.model.Passenger;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ReservationListener implements MessageListener {
    public void onMessage(Message message) {
        ObjectMessage objMsg = (ObjectMessage) message;
        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext("airuser","airpass")){
            InitialContext initialContext = new InitialContext();
            Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
            MapMessage replyMessage = jmsContext.createMapMessage();
            Passenger passenger = (Passenger) objMsg.getObject();

            if (passenger != null) {
                replyMessage.setBoolean("passenger", true);
            }
            else {
                replyMessage.setBoolean("passenger", false);
            }

            JMSProducer producer = jmsContext.createProducer();
            producer.send(replyQueue, replyMessage);

        } catch (JMSException e) {
            throw new RuntimeException(e);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

    }
}
