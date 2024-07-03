package io.github.ayechanaungthwin.chat.model;

public enum Key {
	PROFILE_IMAGE,
	PNG_IMAGE,
	JPEG_IMAGE,
	ENTER_KEY,
	
	PROCESS_TYPING {
		@Override
	    public String toString() {
	        return " is typing";
	    }
	},
	
	PROCESS_IDLE_TYPING {
		@Override
	    public String toString() {
	        return " is idle";
	    }
	}
}
