package io.github.ayechanaungthwin.chat.cor;

import java.awt.image.BufferedImage;

import io.github.ayechanaungthwin.chat.controller.MainController;
import io.github.ayechanaungthwin.chat.model.Dto;
import io.github.ayechanaungthwin.chat.model.Key;
import io.github.ayechanaungthwin.chat.ui.JfxDynamicUiChangerUtils;
import io.github.ayechanaungthwin.chat.utils.ImageJsonUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class ProfileImageHandler extends BaseHandler {
	
	@Override
	public void handleRequest(ScrollPane scrollPane, VBox vBox, Dto dto) {
		// TODO Auto-generated method stub
		try {
			if (dto.getKey()==Key.PROFILE_IMAGE) {
				BufferedImage bufferedImage = ImageJsonUtils.getBufferedImage(dto.getMessage());
				Image image = ImageJsonUtils.getImage(bufferedImage);						
				MainController.responseImage = image;
				
				JfxDynamicUiChangerUtils.addJoinedProfileImageToVBox(vBox, dto);
				
				JfxDynamicUiChangerUtils.autoScrollDown(scrollPane, vBox);
				return;
			}
			throw new Exception();
		}
		catch (Exception ex) {
			super.successor.handleRequest(scrollPane, vBox, dto);
		}
	}
}
