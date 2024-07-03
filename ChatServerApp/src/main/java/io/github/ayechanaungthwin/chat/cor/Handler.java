package io.github.ayechanaungthwin.chat.cor;

import io.github.ayechanaungthwin.chat.model.Dto;
import javafx.scene.layout.VBox;

public interface Handler {

	void handleRequest(VBox vBox, Dto dto);
	void setSuccessor(Handler handler);
}
