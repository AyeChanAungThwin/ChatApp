package io.github.ayechanaungthwin.chat.model;

import java.io.PrintWriter;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TypingThread extends Thread {
	
	private final ObjectMapper mapper = new ObjectMapper();
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
			
			PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
			String jsonString = mapper.writeValueAsString(new Dto(Key.PROCESS_TYPING, "Server is typing"));
			out.println(jsonString);
			
			Thread.sleep(1500);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			typing = false;
			
			try {
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				String jsonString = mapper.writeValueAsString(new Dto(Key.PROCESS_NOT_TYPING, "Server is idle"));
				out.println(jsonString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
