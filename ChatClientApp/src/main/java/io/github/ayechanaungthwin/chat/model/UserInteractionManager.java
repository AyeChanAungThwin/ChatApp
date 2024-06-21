package io.github.ayechanaungthwin.chat.model;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ayechanaungthwin.chat.controller.ClientController;

public class UserInteractionManager {

	private volatile boolean flag;
    private final ScheduledExecutorService scheduler;
    private long lastInteractionTime;
    
    private String socketName;
    private Socket socket;
    
    public UserInteractionManager(String socketName, Socket socket) {
    	this.socketName = socketName;
    	this.socket = socket;
    	
    	this.flag = false;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.lastInteractionTime = System.currentTimeMillis();

        // Schedule a task to update the flag every 1500ms
        scheduler.scheduleAtFixedRate(this::updateFlag, 0, 1500, TimeUnit.MILLISECONDS);
    }
    
    private static boolean interact = false;
    
    private void pushToSocketOnInteraction(String message) {
    	if (interact!=flag) {
        	interact = flag;
        	
        	try {
        		if (!socket.isConnected()) return;
        		//Object to json conversion.
        		ObjectMapper mapper = new ObjectMapper();
            	PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
    			String jsonString = mapper.writeValueAsString(new Dto(socketName, message));
    			
    			//Encrypt String before sending.
    			String encryptedString = StringEncryptionUtils.encrypt(jsonString, ClientController.SECRET_KEY);
    			out.println(encryptedString);
        	}
        	catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
    
    private void updateFlag() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastInteractionTime >= 1500) {
            flag = false;
            pushToSocketOnInteraction("idle");
        } else {
            flag = true;
            pushToSocketOnInteraction("typing");
        }
    }

    public void interact() {
        // Update the last interaction time and set the flag to true
        lastInteractionTime = System.currentTimeMillis();
        flag = true;
    }

    public boolean isFlag() {
        return flag;
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
