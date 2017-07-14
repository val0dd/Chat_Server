package me.valodd.chatserver.network;

import java.io.IOException;
import java.net.Socket;

import me.valodd.chatserver.client.Client;
import me.valodd.chatserver.client.ClientManager;
import me.valodd.chatserver.network.packet.PACKETS;
import me.valodd.chatserver.network.packet.boths.PacketConnection;
import me.valodd.chatserver.network.packet.boths.PacketMessage;
import me.valodd.chatserver.network.packet.out.PacketUserLogout;

public class NetworkClient {
	private Client c;
	private Socket socket;
	private Thread threadPacketListener;
	private boolean end = false;

	public NetworkClient(Socket socket) {
		this.socket = socket;
		startInputListening();
	}

	public void sendPacket(Packet packet) {
		packet.write();
		_sendPacket(packet.getBufferConnection());
	}

	private void _sendPacket(BufferConnection bc) {
		try {
			int size = bc.getSizeBuff();
			socket.getOutputStream().write(size >> 24);
			socket.getOutputStream().write(size >> 16);
			socket.getOutputStream().write(size >> 8);
			socket.getOutputStream().write(size >> 0);
			socket.getOutputStream().write(bc.getAllBytes());
		} catch (IOException e) {
			stop();
		}
	}

	protected void onPacketReceive(BufferConnection bc) {
		if ("-VAL0DD-".equalsIgnoreCase(bc.readString())) {
			int packetID = bc.readInt();
			PACKETS packetsID = PACKETS.getByID(packetID);
			Packet packet = null;
			switch (packetsID) {
			case PACKETCONNECTION: // PacketConnection
				packet = new PacketConnection(c);
				break;
			case PACKETMESSAGE: // PacketMessage
				packet = new PacketMessage(c);
				break;
			case PACKETUSERLOGOUT: // PacketUserLogout
				packet = new PacketUserLogout(c);
				break;
			default:
				break;
			}
			if (packet != null) {
				packet.read(bc);
				packet.executePacket();
			}
		}
	}

	private void stop() {
		if (!end) {
			end = true;
			for (Client c : ClientManager.getClients())
				if (c != getClient())
					c.getNetworkClient().sendPacket(new PacketUserLogout(getClient()).setClient(getClient()));
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ClientManager.removeClient(getClient());
	}

	private void startInputListening() {
		threadPacketListener = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!end) {
					try {
						int ch1 = socket.getInputStream().read();
						if (ch1 == -1) {
							stop();
						} else {
							int ch2 = socket.getInputStream().read();
							int ch3 = socket.getInputStream().read();
							int ch4 = socket.getInputStream().read();
							int length = (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
							if (length > 0) {
								BufferConnection bc = new BufferConnection(length);
								byte[] message = new byte[length];
								socket.getInputStream().read(message, 0, length);
								bc.writeBytes(message);
								new Thread(new Runnable() {

									@Override
									public void run() {
										onPacketReceive(bc);
									}
								}).start();
							}
						}
					} catch (IOException ex) { // DISCONNECTED
						stop();
					}
				}
			}
		});
		threadPacketListener.start();
	}

	public void setClient(Client c) {
		this.c = c;
	}

	public Client getClient() {
		return c;
	}
}
