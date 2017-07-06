package me.valodd.chatserver.client;

import me.valodd.chatserver.network.NetworkClient;

public class Client {
	private NetworkClient nc;

	public Client(NetworkClient nc) {
		this.nc = nc;
	}

	public NetworkClient getNetworkClient() {
		return nc;
	}
}
