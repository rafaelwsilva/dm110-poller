package br.inatel.pos.mobile.dm110.poller.util;

public class NetworkIpGen {

	public static String[] generateIps(String networkIp, int cidr) {
		int rangeSize = 0;
		for (int i = 0; i < 32 - cidr; i++) {
			rangeSize |= 1 << i;
		}
		long networkAddress = fromIp(networkIp);
		String[] ips = new String[rangeSize - 1];
		for (int i = 1; i < rangeSize; i++) {
			ips[i - 1] = toIp(networkAddress + i);
		}
		return ips;
	}

	public static long fromIp(String ip) {
		String[] octs = ip.split("\\.");
		long octs1 = Long.parseLong(octs[0]);
		long octs2 = Long.parseLong(octs[1]);
		long octs3 = Long.parseLong(octs[2]);
		long octs4 = Long.parseLong(octs[3]);
		return (octs1 << 24) | (octs2 << 16) | (octs3 << 8) | octs4;
	}

	public static String toIp(long value) {
		return String.format("%s.%s.%s.%s", value >> 24, (value >> 16) & 255, (value >> 8) & 255, value & 255);
	}
}