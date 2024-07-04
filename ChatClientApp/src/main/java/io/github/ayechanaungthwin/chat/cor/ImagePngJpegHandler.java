package io.github.ayechanaungthwin.chat.cor;

import io.github.ayechanaungthwin.chat.model.Dto;
import io.github.ayechanaungthwin.chat.model.Key;
import io.github.ayechanaungthwin.chat.ui.JfxDynamicUiChangerUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ImagePngJpegHandler extends BaseHandler {

	@Override
	public void handleRequest(ScrollPane scrollPane, VBox vBox, Dto dto) {
		// TODO Auto-generated method stub
		try {
			if (dto.getKey()==Key.PNG_IMAGE
					||dto.getKey()==Key.JPEG_IMAGE) {
				JfxDynamicUiChangerUtils.popImageFromSocketAndAddToVBox(vBox, dto);
				
				JfxDynamicUiChangerUtils.autoScrollDown(scrollPane, vBox);
				return;
			}
			throw new Exception();
		}
		catch (Exception ex) {
			this.successor.handleRequest(scrollPane, vBox, dto);
		}
	}

}
