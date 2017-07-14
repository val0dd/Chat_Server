package me.valodd.chatserver.network.packet.out;

import me.valodd.chatserver.client.Client;
import me.valodd.chatserver.network.BufferConnection;
import me.valodd.chatserver.network.Packet;
import me.valodd.chatserver.network.packet.PACKETS;

public class PacketUserLogout extends Packet {
	private Client client;

	public PacketUserLogout(Client owner) {
		super(owner);
	}

	@Override
	public void readPacket(BufferConnection bc) {
		// NOTHING TO DO HERE BECAUSE OUT PACKET
	}

	@Override
	public void writePacket(BufferConnection bc) {
		bc.writeString(client.getName());
	}

	@Override
	public void executePacket() {
		// NOTHING TO DO HERE BECAUSE OUT PACKET
	}

	@Override
	public PACKETS getPacketID() {
		return PACKETS.PACKETUSERLOGOUT;
	}

	public PacketUserLogout setClient(Client client) {
		this.client = client;
		return this;
	}
}
