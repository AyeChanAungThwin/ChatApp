package io.github.ayechanaungthwin.chat.model;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements SocketModel {

	private Socket soc;
	
	public Client(int port) throws UnknownHostException, IOException {
		soc = new Socket("localhost", port);
	}
	
	@Override
	public String getSocketName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public Socket getSocket() {
		return soc;
	}
	
	public void close() {
		try {
			soc.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
