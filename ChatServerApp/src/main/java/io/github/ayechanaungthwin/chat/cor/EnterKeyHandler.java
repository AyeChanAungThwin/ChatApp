package io.github.ayechanaungthwin.chat.cor;

import io.github.ayechanaungthwin.chat.model.Dto;
import io.github.ayechanaungthwin.chat.model.Key;
import io.github.ayechanaungthwin.chat.ui.JfxDynamicUiChangerUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class EnterKeyHandler extends BaseHandler {

	@Override
	public void handleRequest(ScrollPane scrollPane, VBox vBox, Dto dto) {
		// TODO Auto-generated method stub
		try {
			if (dto.getKey()==Key.ENTER_KEY) {
				String text = dto.getMessage();
				JfxDynamicUiChangerUtils.addLabelToVBox(scrollPane, vBox, text, true); 
				JfxDynamicUiChangerUtils.removeHBoxById(scrollPane, vBox, "typing-gif");
				return;
			}
			throw new Exception();
		}
		catch (Exception ex) {
			this.successor.handleRequest(scrollPane, vBox, dto);
		}
	}

}
