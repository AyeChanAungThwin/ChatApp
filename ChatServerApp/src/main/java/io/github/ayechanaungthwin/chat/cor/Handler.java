package io.github.ayechanaungthwin.chat.cor;

import io.github.ayechanaungthwin.chat.model.Dto;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public interface Handler {

	void handleRequest(ScrollPane scrollPane, VBox vBox, Dto dto);
	void setSuccessor(Handler handler);
}
