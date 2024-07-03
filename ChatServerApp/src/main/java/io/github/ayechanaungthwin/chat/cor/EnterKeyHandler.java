package io.github.ayechanaungthwin.chat.cor;

import io.github.ayechanaungthwin.chat.model.Dto;
import io.github.ayechanaungthwin.chat.model.Key;
import io.github.ayechanaungthwin.chat.ui.JfxDynamicUiChangerUtils;
import javafx.scene.layout.VBox;

public class EnterKeyHandler extends BaseHandler {

	@Override
	public void handleRequest(VBox vBox, Dto dto) {
		// TODO Auto-generated method stub
		try {
			if (dto.getKey()!=Key.ENTER_KEY) throw new Exception();
			
			String text = dto.getMessage();
			JfxDynamicUiChangerUtils.addLabelToVBox(vBox, text, true); 
			JfxDynamicUiChangerUtils.removeHBoxById(vBox, "typing-gif");
		}
		catch (Exception ex) {
			this.successor.handleRequest(vBox, dto);
		}
	}

}
