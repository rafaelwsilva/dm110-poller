package br.inatel.pos.mobile.dm110.poller.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.inatel.pos.mobile.dm110.poller.entities.IP;

@Stateless
public class PollerDAO {
	
	@PersistenceContext(unitName = "poller")
	private EntityManager em;

	public void insert(IP ip) {
		em.persist(ip);
	}
	
	public void update(IP ip) {
		em.merge(ip);
	}
	
	public IP pollerByIp(String ip) {
		return em.find(IP.class, ip);
	}
	
	public List<IP> listAll(){
		return em.createQuery("from ip i", IP.class).getResultList();
	}
}
