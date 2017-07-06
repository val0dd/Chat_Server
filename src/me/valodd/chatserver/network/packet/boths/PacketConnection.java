package me.valodd.chatserver.network.packet.boths;

import me.valodd.chatserver.client.Client;
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
		PacketConnection pc = new PacketConnection(getOwner());
		pc.setUsername("0ddlyoko");
		pc.setNbClients(1);
		pc.setClients("0ddlyoko");
		getOwner().getNetworkClient().sendPacket(pc);
	}

	@Override
	public PACKETS getPacketID() {
		return PACKETS.PACKETCONNECTION;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setNbClients(int nbClients) {
		this.nbClients = nbClients;
	}

	public void setClients(String clients) {
		this.clients = clients;
	}
}
