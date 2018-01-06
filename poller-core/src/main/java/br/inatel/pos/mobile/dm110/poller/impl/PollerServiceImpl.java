package br.inatel.pos.mobile.dm110.poller.impl;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

import br.inatel.pos.mobile.dm110.poller.api.PollerService;
import br.inatel.pos.mobile.dm110.poller.interfaces.PollerRemote;
import br.inatel.pos.mobile.dm110.poller.to.IPTO;

@RequestScoped
public class PollerServiceImpl implements PollerService {
	
	@EJB(lookup="java:app/poller-ejb-1.0.0-SNAPSHOT/PollerBean!br.inatel.pos.mobile.dm110.poller.interfaces.PollerRemote")
	
	private PollerRemote pollerRemote;

	@Override
	public void startPoller(String ip, String mask) {
		pollerRemote.startPoller(ip, mask);
	}

	@Override
	public IPTO checkStatus(String ip) {
		return checkStatus(ip);
	}

}
