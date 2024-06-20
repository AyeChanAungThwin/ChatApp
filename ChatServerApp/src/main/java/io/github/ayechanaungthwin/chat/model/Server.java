package io.github.ayechanaungthwin.chat.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static int ServerPort = 5020;
	
	private ServerSocket svrSoc = null;
	private Socket soc = null;
	
	public Server(int port) throws IOException {
		svrSoc = new ServerSocket(port);
	}
	
	public ServerSocket getServerSocket() {
		return svrSoc;
	}
	
	public void close() throws IOException {
		soc.close();
		svrSoc.close();
	}
}
