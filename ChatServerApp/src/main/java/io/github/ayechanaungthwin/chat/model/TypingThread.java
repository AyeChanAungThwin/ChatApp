package io.github.ayechanaungthwin.chat.model;

import java.io.PrintWriter;
import java.net.Socket;

public class TypingThread extends Thread {
	
	public static boolean typing;
	private Socket socket;
	
	public TypingThread() {
		typing = false;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			typing = true;
			try {
				PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
				out.println("Server is typing!!!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//observer.setTypingStatus("typing");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			typing = false;
			try {
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println("Waiting for server reply...");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//observer.setTypingStatus("");
		}
	}
}
