package me.valodd.chatserver.network.packet.boths;

import me.valodd.chatserver.client.Client;
import me.valodd.chatserver.client.ClientManager;
import me.valodd.chatserver.network.BufferConnection;
import me.valodd.chatserver.network.Packet;
import me.valodd.chatserver.network.packet.PACKETS;
import me.valodd.chatserver.network.packet.out.PacketUserLogin;

public class PacketConnection extends Packet {
	private String username;
	private String password;
	private int nbClients;
	private String clients;

	public PacketConnection(Client owner) {
		super(owner);
	}

	@Override
	public void readPacket(BufferConnection bc) {
		username = bc.readString();
		password = bc.readString();
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
		StringBuilder cs = new StringBuilder();
		for (Client c : ClientManager.getClients()) {
			if (c != getOwner())
				c.getNetworkClient().sendPacket(new PacketUserLogin(c).setClient(getOwner()));
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

	public String getPassword() {
		return password;
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
