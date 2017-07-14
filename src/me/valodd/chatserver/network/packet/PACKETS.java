package me.valodd.chatserver.network.packet;

public enum PACKETS {
	PACKETCONNECTION(1),
	PACKETUSERLOGIN(2),
	PACKETMESSAGE(3),
	PACKETUSERLOGOUT(4);

	private int id;

	private PACKETS(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public static PACKETS getByID(int id) {
		for (PACKETS p : values())
			if (p.getID() == id)
				return p;
		return null;
	}
}
