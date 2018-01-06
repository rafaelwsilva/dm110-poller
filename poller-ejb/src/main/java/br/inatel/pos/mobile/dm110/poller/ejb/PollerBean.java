package br.inatel.pos.mobile.dm110.poller.ejb;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import br.inatel.pos.mobile.dm110.poller.dao.PollerDAO;
import br.inatel.pos.mobile.dm110.poller.entities.IP;
import br.inatel.pos.mobile.dm110.poller.interfaces.PollerLocal;
import br.inatel.pos.mobile.dm110.poller.interfaces.PollerRemote;
import br.inatel.pos.mobile.dm110.poller.to.IPTO;
import br.inatel.pos.mobile.dm110.poller.util.NetworkIpGen;

@Stateless
@Remote(PollerRemote.class)
@Local(PollerLocal.class)
public class PollerBean implements PollerLocal, PollerRemote {

	@EJB
	private PollerDAO dao;

	@EJB
	private PollerMessageSender messageSender;

	@Override
	public void startPoller(String address, String mask) {

		ArrayList<IPTO> ips = new ArrayList<>();

		String[] ipsGenerated = NetworkIpGen.generateIps(address, Integer.parseInt(mask));

		for (int i = 1; i < ipsGenerated.length; i++) {
			IPTO ipto = new IPTO();
			ipto.setAddress(ipsGenerated[i]);
			ips.add(ipto);
			if ((i + 1) % 10 == 0) {
				messageSender.sendMessage(ips);
				ips = new ArrayList<>();
			}
		}
		if (ips.size() > 0) {
			messageSender.sendMessage(ips);
		}
	}

	@Override
	public IPTO checkStatus(String address) {
		IP ip = dao.pollerByIp(address);
		IPTO ipto = new IPTO();
		ipto.setAddress(ip.getAddress());
		ipto.setStatus(ip.getStatus());
		return ipto;
	}

	@Override
	public void insertIP(String address, String status) {
		IP ip = new IP();
		ip.setAddress(address);
		ip.setStatus(status);
		dao.insert(ip);
	}

}
