package io.github.ayechanaungthwin.chat.cor;

import io.github.ayechanaungthwin.chat.model.Dto;
import io.github.ayechanaungthwin.chat.model.Key;
import io.github.ayechanaungthwin.chat.ui.JfxDynamicUiChangerUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;

public class ProcessTypingHandler extends BaseHandler {
	
	private static MediaPlayer player = null;
	
	@Override
	public void handleRequest(ScrollPane scrollPane, VBox vBox, Dto dto) {
		// TODO Auto-generated method stub
		try {
			if (dto.getKey()==Key.PROCESS_TYPING) {
				JfxDynamicUiChangerUtils.addShowTypingGif(scrollPane, vBox, dto.getMessage());
				
				if (player==null) {
					player = JfxDynamicUiChangerUtils.getMediaPlayer("typing.mp3");
					player.play();
					player.setOnEndOfMedia(() -> {
						player.seek(player.getStartTime());
						player.play();
					});
				}
				return;
			}
			else {
				if (player!=null) {
					player.pause();
					player.setOnEndOfMedia(() -> {
						player.stop();
					});
					player.seek(player.getStopTime());
					player.stop();
					player = null;
				}
				throw new Exception();
			}
		}
		catch (Exception ex) {
			super.successor.handleRequest(scrollPane, vBox, dto);
		}
	}
}
