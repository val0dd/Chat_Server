package me.valodd.chatserver.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import me.valodd.chatserver.network.packet.PACKETS;
import me.valodd.chatserver.network.packet.boths.PacketConnection;

public class NetworkClient {
	private Socket socket;
	private Thread threadPacketListener;
	private boolean end = false;

	public NetworkClient(Socket socket) {
		this.socket = socket;
		startInputListening();
	}

	public void sendPacket(Packet packet) {
		packet.write();
		_sendPacket(new BufferConnection(packet.getBufferConnection().getSizeBuff() + 4)
				.writeInt(packet.getBufferConnection().getSizeBuff()).writeBytes(packet.getBufferConnection()));
	}

	private void _sendPacket(BufferConnection bc) {
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.write(bc.getAllBytes());
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
				PacketConnection packet = new PacketConnection();
				packet.read(bc);
				System.out.println("New Client: " + packet.getUsername());
				PacketConnection pc = new PacketConnection();
				pc.setUsername("0ddlyoko");
				pc.setNbClients(1);
				pc.setClients("0ddlyoko");
				sendPacket(pc);
				break;
			default:
				break;
			}
		}
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
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		threadPacketListener.start();
	}
}
