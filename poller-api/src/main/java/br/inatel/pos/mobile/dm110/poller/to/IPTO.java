package br.inatel.pos.mobile.dm110.poller.to;

import java.io.Serializable;

public class IPTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7396480335897731160L;

	private String address;
	private String status;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
