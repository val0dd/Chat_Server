package me.valodd.chatserver.network;

import me.valodd.chatserver.client.Client;

/**
 * This class will be extended by all Class Packet to send packet over the
 * Internet.<br />
 * <br />
 * Created: 06 juil. 2017 (16:22:31);<br />
 * Last edit: 06 juil. 2017 (16:22:31);<br />
 * 
 * @author 0ddlyoko<br />
 *         <br />
 */
public abstract class Packet implements IPacket {
	private BufferConnection bc;
	private Client owner;

	public Packet(Client owner) {
		this.owner = owner;
		bc = new BufferConnection(32);
	}

	public final Packet read(BufferConnection bc) {
		this.bc = bc;
		readPacket(bc);
		return this;
	}

	public final Packet write() {
		bc.writeString("-VAL0DD-");
		bc.writeInt(getPacketID().getID());
		writePacket(bc);
		return this;
	}

	public BufferConnection getBufferConnection() {
		return bc;
	}

	public Client getOwner() {
		return owner;
	}
}
