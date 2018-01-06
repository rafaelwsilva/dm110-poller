package br.inatel.pos.mobile.dm110.poller.ejb;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import br.inatel.pos.mobile.dm110.poller.to.IPTO;

@Stateless
public class PollerMessageSender {
	
	@Resource(lookup = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	@Resource(lookup = "java:/jms/queue/PollerQueue")
	private Queue queue;
	
	public void sendMessage(ArrayList<IPTO> ips) {
		try (
				Connection connection = connectionFactory.createConnection();
				Session session = connection.createSession();
				MessageProducer producer = session.createProducer(queue);
		) {
			ObjectMessage message = session.createObjectMessage(ips);
			producer.send(message);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

}
