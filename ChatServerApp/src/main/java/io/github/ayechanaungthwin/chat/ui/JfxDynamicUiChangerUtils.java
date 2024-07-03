package io.github.ayechanaungthwin.chat.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ayechanaungthwin.chat.controller.ServerController;
import io.github.ayechanaungthwin.chat.model.Dto;
import io.github.ayechanaungthwin.chat.model.Key;
import io.github.ayechanaungthwin.chat.utils.FileUtils;
import io.github.ayechanaungthwin.chat.utils.ImageJsonUtils;
import io.github.ayechanaungthwin.chat.utils.StringEncryptionUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class JfxDynamicUiChangerUtils {
	
	//Writer
	public static void sendProfileImageThroughSocketOnConnected(Socket soc, String SECRET_KEY) {
		try {
			PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
			
			//Object to JSON conversion.
			String json = ImageJsonUtils.getJson(FileUtils.getLogoPath());
			
			ObjectMapper mapper = new ObjectMapper();
        	String jsonString = mapper.writeValueAsString(new Dto(Key.PROFILE_IMAGE, json));
			
        	//Encrypt String before sending.
			String encryptedString = StringEncryptionUtils.encrypt(jsonString, SECRET_KEY);
			out.println(encryptedString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendImageThroughSocketFromFileChooser(Socket soc, String SECRET_KEY, String path) {
		try {
			PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
			
			//Object to json conversion.
			String json = ImageJsonUtils.getJson(path);
			Key extension=path.toLowerCase().contains(".png")?Key.PNG_IMAGE:Key.JPEG_IMAGE;
			
			ObjectMapper mapper = new ObjectMapper();
        	String jsonString = mapper.writeValueAsString(new Dto(extension, json));
			
			//Encrypt String before sending.
			String encryptedString = StringEncryptionUtils.encrypt(jsonString, SECRET_KEY);
			out.println(encryptedString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendTextThroughSocket(Socket soc, String SECRET_KEY, String text) {
		try {
			PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
			
			//Object to json conversion.
        	ObjectMapper mapper = new ObjectMapper();
        	String jsonString = mapper.writeValueAsString(new Dto(Key.ENTER_KEY, text));
			
			//Encrypt String before sending.
			String encryptedString = StringEncryptionUtils.encrypt(jsonString, SECRET_KEY);
			out.println(encryptedString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Reader
	public static void showTypingGif(VBox vBox, String socketEndName) {
		Platform.runLater(() -> {
			Label label = new Label();
			label.setText(socketEndName);
			label.setFont(new Font(10));
			label.setMaxHeight(20);
			
			StringBuilder sb = new StringBuilder();
	    	sb.append(System.getProperty("user.dir"));
	    	sb.append(File.separator);
	    	sb.append("src");
	    	sb.append(File.separator);
	    	sb.append("main");
	    	sb.append(File.separator);
	    	sb.append("resources");
	    	sb.append(File.separator);
	    	sb.append("images");
	    	sb.append(File.separator);
	    	sb.append("typing.gif");
	    	
	    	File file = new File(sb.toString());
	    	
			ImageView img = new ImageView();
			img.setFitWidth(25);
			img.setFitHeight(25);
			img.setImage(new Image(file.toURI().toString()));
			
			HBox hBox=new HBox();
			hBox.setId("typing-gif");
	        hBox.getChildren().addAll(label, img);
	        hBox.setAlignment(Pos.BASELINE_LEFT);
	        hBox.setPadding(new Insets(0, 0, 0, 0));
	        
			vBox.getChildren().add(hBox);
		});
	}
	
	// Method to remove HBox by ID
    public static void removeHBoxById(VBox vBox, String id) {
        Platform.runLater(() -> {
        	vBox.getChildren().removeIf(node -> node instanceof HBox && id.equals(node.getId()));
        });
    }
	
	public static void getImageFromSocketAndShow(VBox vBox, Dto dto) {
		Platform.runLater(() -> {
			BufferedImage bufferedImage = ImageJsonUtils.getBufferedImage(dto.getMessage());
			Image image = ImageJsonUtils.getImage(bufferedImage);						
			
			double width = image.getWidth();
			double height = image.getHeight();
			double percentage = 0;
			
			//Set every images the same width/height for display
			if (width>190) percentage = 190/width;
			
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(width*percentage);
			imageView.setFitHeight(height*percentage);
			
			HBox hBox=new HBox();
	        hBox.getChildren().addAll(imageView);
	        hBox.setAlignment(Pos.BASELINE_LEFT);
	        hBox.setPadding(new Insets(5, 5, 0, 5));
	        
			vBox.getChildren().add(hBox);
		});
	}
	
	public static void addLabelToVBox(VBox vBox, String text, boolean isResponse) {
		Color color = isResponse?Color.CYAN:Color.LIGHTGREEN;
		Pos pos = isResponse?Pos.BASELINE_LEFT:Pos.BASELINE_RIGHT;
		int padding = isResponse?10:0;
		
		Platform.runLater(() -> {
			Label label = new Label();
			label.setWrapText(true);
        	label.setPadding(new Insets(5, 5, 5, 5));
        	//label.setStyle("-fx-border-color: black;");
        	label.setBackground(new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY)));
			label.setText(text);
			
			HBox hBox=new HBox();
			if (isResponse) {
				ImageView imageView = new ImageView();
				imageView.setImage(ServerController.responseImage);
				imageView.setFitWidth(25);
				imageView.setFitHeight(25);
				hBox.getChildren().addAll(imageView, label);
				//HBox.setMargin(imageView, new Insets(2, 2, 2, 2));
			}
			else {
				hBox.getChildren().add(label);
			}
	        hBox.setAlignment(pos);
	        hBox.setPadding(new Insets(0, 0, 0, padding));
	        HBox.setMargin(label, new Insets(2, 2, 2, 2));
	        
			vBox.getChildren().add(hBox);
		});  
	}
	
	public static void setStatus(Label label, String text) {
		Label status = label;
		Platform.runLater(() -> {
			status.setText(text);
		});
	}
}
