package io.github.ayechanaungthwin.chat.model;

public enum Key {
	IMAGE_PROFILE,
	IMAGE_PNG,
	IMAGE_JPGE,
	ENTER_KEY,
	
	PROCESS_TYPING {
		@Override
	    public String toString() {
	        return " is typing";
	    }
	},
	
	PROCESS_NOT_TYPING {
		@Override
	    public String toString() {
	        return " is idle";
	    }
	}
}
