package me.valodd.chatserver.client;

import java.util.ArrayList;
import java.util.List;

public class ClientManager {
	private static List<Client> clients = new ArrayList<>();

	public static void addClient(Client c) {
		clients.add(c);
	}

	public static void removeClient(Client c) {
		clients.remove(c);
	}

	public static List<Client> getClients() {
		return clients;
	}
}
