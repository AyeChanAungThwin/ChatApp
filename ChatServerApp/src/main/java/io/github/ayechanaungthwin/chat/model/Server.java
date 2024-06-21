package io.github.ayechanaungthwin.chat.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements SocketModel {
	
	private ServerSocket svrSoc = null;
	private Socket soc = null;
	
	public Server(int port) throws IOException {
		svrSoc = new ServerSocket(port);
	}
	
	public void setSocket(Socket soc) {
		this.soc = soc;
	}
	
	@Override
	public String getSocketName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public Socket getSocket() {
		return soc;
	}
	
	public ServerSocket getServerSocket() {
		return svrSoc;
	}
	
	public void close() {
		try {
			soc.close();
			svrSoc.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
