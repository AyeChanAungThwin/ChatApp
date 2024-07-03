package io.github.ayechanaungthwin.chat.cor;

import io.github.ayechanaungthwin.chat.model.Dto;
import io.github.ayechanaungthwin.chat.model.Key;
import io.github.ayechanaungthwin.chat.ui.JfxDynamicUiChangerUtils;
import javafx.scene.layout.VBox;

public class ProcessTypingHandler extends BaseHandler {
	
	@Override
	public void handleRequest(VBox vBox, Dto dto) {
		// TODO Auto-generated method stub
		try {
			if (dto.getKey()!=Key.PROCESS_TYPING) 
				throw new Exception();
			
			JfxDynamicUiChangerUtils.showTypingGif(vBox, dto.getMessage());
		}
		catch (Exception ex) {
			super.successor.handleRequest(vBox, dto);
		}
	}
}
