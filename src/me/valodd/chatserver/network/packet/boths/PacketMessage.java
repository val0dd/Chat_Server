package me.valodd.chatserver.network.packet.boths;

import me.valodd.chatserver.client.Client;
import me.valodd.chatserver.client.ClientManager;
import me.valodd.chatserver.network.BufferConnection;
import me.valodd.chatserver.network.Packet;
import me.valodd.chatserver.network.packet.PACKETS;

public class PacketMessage extends Packet {
	private String pseudo;
	private String message;

	public PacketMessage(Client client) {
		super(client);
	}

	@Override
	public void readPacket(BufferConnection bc) {
		this.message = bc.readString();
	}

	@Override
	public void writePacket(BufferConnection bc) {
		bc.writeString(pseudo);
		bc.writeString(message);
	}

	@Override
	public void executePacket() {
		// TODO CHANGE THIS
		Packet p = new PacketMessage(getOwner()).setPseudo(getOwner().getName()).setMessage(message);
		for (Client c : ClientManager.getClients())
			c.getNetworkClient().sendPacket(p);
	}

	@Override
	public PACKETS getPacketID() {
		return PACKETS.PACKETMESSAGE;
	}

	public String getMessage() {
		return message;
	}

	public PacketMessage setMessage(String message) {
		this.message = message;
		return this;
	}

	public PacketMessage setPseudo(String pseudo) {
		this.pseudo = pseudo;
		return this;
	}
}
