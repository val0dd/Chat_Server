package me.valodd.chatserver.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import me.valodd.chatserver.client.Client;

public class NetworkManager {
	private InetAddress inetAddress;
	private int port;
	private ServerSocket serverSocket;
	private Thread threadPacketListener;
	private boolean end = false;
	private Client client; // TODO JUST TO TEST

	public NetworkManager(InetAddress ia, int port) {
		this.inetAddress = ia;
		this.port = port;
		openServer();
	}

	public void openServer() {
		try {
			serverSocket = new ServerSocket(port, 0, inetAddress);
			startInputListening();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startInputListening() {
		threadPacketListener = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!end) {
					try {
						Socket s = serverSocket.accept();
						System.out.println("New Client:" + s.getInetAddress().getHostName());
						client = new Client(new NetworkClient(s));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		threadPacketListener.start();
	}
}
