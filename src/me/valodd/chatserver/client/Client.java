package me.valodd.chatserver.client;

import me.valodd.chatserver.network.NetworkClient;

public class Client {
	private NetworkClient nc;
	private String name;

	public Client(NetworkClient nc) {
		this.nc = nc;
		nc.setClient(this);
	}

	public NetworkClient getNetworkClient() {
		return nc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
