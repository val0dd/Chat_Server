package me.valodd.chatserver.network.packet.boths;

import me.valodd.chatserver.client.Client;
import me.valodd.chatserver.client.ClientManager;
import me.valodd.chatserver.network.BufferConnection;
import me.valodd.chatserver.network.Packet;
import me.valodd.chatserver.network.packet.PACKETS;

public class PacketConnection extends Packet {
	private String username;
	private int nbClients;
	private String clients;

	public PacketConnection(Client owner) {
		super(owner);
	}

	@Override
	public void readPacket(BufferConnection bc) {
		username = bc.readString();
	}

	@Override
	public void writePacket(BufferConnection bc) {
		bc.writeString(username);
		bc.writeInt(nbClients);
		bc.writeString(clients);
	}

	@Override
	public void executePacket() {
		getOwner().setName(getUsername());
		System.out.println("New Client: " + getUsername());
		StringBuilder cs = new StringBuilder();
		for (Client c : ClientManager.getClients()) {
			cs.append(c.getName()).append(",");
		}
		getOwner().getNetworkClient().sendPacket(new PacketConnection(getOwner()).setUsername(getUsername())
				.setNbClients(ClientManager.getClients().size()).setClients(cs.toString()));
	}

	@Override
	public PACKETS getPacketID() {
		return PACKETS.PACKETCONNECTION;
	}

	public PacketConnection setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public PacketConnection setNbClients(int nbClients) {
		this.nbClients = nbClients;
		return this;
	}

	public PacketConnection setClients(String clients) {
		this.clients = clients;
		return this;
	}
}
