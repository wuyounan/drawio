package com.huigou.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class GetClientMacAddr {
	private String remoteAddress;
	private int port = 137;
	private byte[] buffer = new byte[1024];

	public GetClientMacAddr(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	private final DatagramPacket send(DatagramSocket ds, final byte[] bytes) throws IOException {
		DatagramPacket dp;
		if (remoteAddress.equals("127.0.0.1")) {
			dp = new DatagramPacket(bytes, bytes.length, InetAddress.getLocalHost(), port);
		} else {
			dp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(remoteAddress), port);
		}
		ds.send(dp);
		return dp;
	}

	private final DatagramPacket receive(DatagramSocket ds) throws Exception {
		DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
		ds.receive(dp);
		return dp;
	}

	private byte[] getQueryCmd() throws Exception {
		byte[] t_ns = new byte[50];
		t_ns[0] = 0x00;
		t_ns[1] = 0x00;
		t_ns[2] = 0x00;
		t_ns[3] = 0x10;
		t_ns[4] = 0x00;
		t_ns[5] = 0x01;
		t_ns[6] = 0x00;
		t_ns[7] = 0x00;
		t_ns[8] = 0x00;
		t_ns[9] = 0x00;
		t_ns[10] = 0x00;
		t_ns[11] = 0x00;
		t_ns[12] = 0x20;
		t_ns[13] = 0x43;
		t_ns[14] = 0x4B;

		for (int i = 15; i < 45; i++) {
			t_ns[i] = 0x41;
		}

		t_ns[45] = 0x00;
		t_ns[46] = 0x00;
		t_ns[47] = 0x21;
		t_ns[48] = 0x00;
		t_ns[49] = 0x01;
		return t_ns;
	}

	private final String getMacAddress(byte[] brevdata) throws Exception {
		int i = brevdata[56] * 18 + 56;
		String address;
		StringBuffer sb = new StringBuffer(17);

		for (int j = 1; j < 7; j++) {
			address = Integer.toHexString(0xFF & brevdata[i + j]);
			if (address.length() < 2) {
				sb.append(0);
			}
			sb.append(address.toUpperCase());
			if (j < 6)
				sb.append('-');
		}
		return sb.toString();
	}

	public final String getRemoteMacAddress() throws Exception {
		DatagramSocket ds = new DatagramSocket();
		try {
			byte[] cmd = getQueryCmd();
			send(ds, cmd);
			DatagramPacket dp = receive(ds);
			return getMacAddress(dp.getData());
		} finally {
			ds.close();
		}
	}
}
