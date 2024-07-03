package io.github.ayechanaungthwin.chat.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class Dto {

	private Key key;
	private String message;
	
	@Getter 
	@Setter
	private String description;
	
	public Dto(Key key, String message) {
		this.key = key;
		this.message = message;
	}
	
	
}
