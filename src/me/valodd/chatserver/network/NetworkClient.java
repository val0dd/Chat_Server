package me.valodd.chatserver.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import me.valodd.chatserver.client.Client;
import me.valodd.chatserver.client.ClientManager;
import me.valodd.chatserver.network.packet.PACKETS;
import me.valodd.chatserver.network.packet.boths.PacketConnection;

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
			e.printStackTrace();
		}
	}

	protected void onPacketReceive(BufferConnection bc) {
		if ("-VAL0DD-".equalsIgnoreCase(bc.readString())) {
			int packetID = bc.readInt();
			PACKETS packetsID = PACKETS.getByID(packetID);
			switch (packetsID) {
			case PACKETCONNECTION: // PacketConnection
				PacketConnection packet = new PacketConnection(c);
				packet.read(bc);
				packet.executePacket();
				break;
			default:
				break;
			}
		}
	}

	private void stop() {
		end = true;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ClientManager.removeClient(getClient());
	}

	private void startInputListening() {
		threadPacketListener = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!end) {
					try {
						DataInputStream dis = new DataInputStream(socket.getInputStream());
						int length = dis.readInt();
						if (length > 0) {
							BufferConnection bc = new BufferConnection(length);
							byte[] message = new byte[length];
							dis.readFully(message, 0, message.length);
							bc.writeBytes(message);
							new Thread(new Runnable() {

								@Override
								public void run() {
									onPacketReceive(bc);
								}
							}).start();
						}
					} catch (IOException e) { // DISCONNECTED
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
