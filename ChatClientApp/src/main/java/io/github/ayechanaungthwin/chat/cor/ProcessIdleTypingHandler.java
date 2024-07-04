package io.github.ayechanaungthwin.chat.cor;

import io.github.ayechanaungthwin.chat.model.Dto;
import io.github.ayechanaungthwin.chat.model.Key;
import io.github.ayechanaungthwin.chat.ui.JfxDynamicUiChangerUtils;
import javafx.scene.layout.VBox;

public class ProcessIdleTypingHandler extends BaseHandler {
	
	@Override
	public void handleRequest(VBox vBox, Dto dto) {
		// TODO Auto-generated method stub
		try {
			if (dto.getKey()==Key.PROCESS_IDLE_TYPING) {
				JfxDynamicUiChangerUtils.removeHBoxById(vBox, "typing-gif");
				return;
			}
			throw new Exception();
		}
		catch (Exception ex) {
			System.out.println("Hi");
			super.successor.handleRequest(vBox, dto);
		}
	}
}
