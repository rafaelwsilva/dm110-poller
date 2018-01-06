package br.inatel.pos.mobile.dm110.poller.interfaces;

import br.inatel.pos.mobile.dm110.poller.to.IPTO;

public interface Poller {
	
	void startPoller(String ip, String mask);
	
	IPTO checkStatus(String ip);
	
	void insertIP(String ip, String status);
	
}
