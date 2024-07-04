package io.github.ayechanaungthwin.chat.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ayechanaungthwin.chat.controller.MainController;
import io.github.ayechanaungthwin.chat.model.Dto;
import io.github.ayechanaungthwin.chat.model.Key;
import io.github.ayechanaungthwin.chat.utils.FileUtils;
import io.github.ayechanaungthwin.chat.utils.ImageJsonUtils;
import io.github.ayechanaungthwin.chat.utils.StringEncryptionUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class JfxDynamicUiChangerUtils {
	
	private static Color color1 = Color.DARKCYAN;
	private static Color color2 = Color.FORESTGREEN;
	
	//Writer
	public static void pushImageToSocketOnConnected(Socket soc, String SECRET_KEY, String description) {
		try {
			PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
			
			//Object to JSON conversion.
			String json = ImageJsonUtils.getJson(FileUtils.getLogoPath());
			
			ObjectMapper mapper = new ObjectMapper();
			Dto dto = new Dto(Key.PROFILE_IMAGE, json);
			dto.setExtraData(description);
        	String jsonString = mapper.writeValueAsString(dto);
			
        	//Encrypt String before sending.
			String encryptedString = StringEncryptionUtils.encrypt(jsonString, SECRET_KEY);
			out.println(encryptedString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pushImageToSocketWithFileChooser(Socket soc, String SECRET_KEY, String path) {
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
	
	public static void pushTextToSocket(Socket soc, String SECRET_KEY, String text) {
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
	public static void addShowTypingGif(VBox vBox, String socketEndName) {
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
			img.setFitWidth(80);
			img.setFitHeight(80);
			img.setImage(new Image(file.toURI().toString()));
			
			HBox hBox=new HBox();
			hBox.setId("typing-gif");
			hBox.getChildren().addAll(/* label, */img);
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
    
    private static void addJsonImageToVBox(VBox vBox, String jsonImage, boolean isResponse) {
    	Pos pos = isResponse?Pos.BASELINE_LEFT:Pos.BASELINE_RIGHT;
    	Color color = isResponse?color1:color2;
    	
    	Platform.runLater(() -> {
			BufferedImage bufferedImage = ImageJsonUtils.getBufferedImage(jsonImage);
			Image image = ImageJsonUtils.getImage(bufferedImage);						
			
			double width = image.getWidth();
			double height = image.getHeight();
			double percentage = 0;
			
			//Set every images the same width/height for display
			if (width>180) percentage = 180/width;
			
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(width*percentage);
			imageView.setFitHeight(height*percentage);
			imageView.setEffect(new DropShadow(5, Color.BLACK));
			
			StackPane stackPane = new StackPane();
			stackPane.getChildren().add(imageView);
			stackPane.setBackground(new Background(new BackgroundFill(color, new CornerRadii(5), new Insets(5, 5, 5, 5))));
			stackPane.setStyle("-fx-padding: 10;");
			
			HBox hBox=new HBox();
	        hBox.getChildren().addAll(stackPane);
	        hBox.setAlignment(pos);
	        hBox.setPadding(new Insets(5, 5, 0, 5));
	        HBox.setMargin(stackPane, new Insets(2, 10, 2, 2));
	        
			vBox.getChildren().add(hBox);
		});
    }
    
    public static void addImageToVBox(VBox vBox, String path) {
    	String jsonImage = ImageJsonUtils.getJson(path);
		addJsonImageToVBox(vBox, jsonImage, false);
	}
	
	public static void popImageFromSocketAndAddToVBox(VBox vBox, Dto dto) {
		String jsonImage = dto.getMessage();
		addJsonImageToVBox(vBox, jsonImage, true);
	}
	
	public static void addLabelToVBox(VBox vBox, String text, boolean isResponse) {
		Color color = isResponse?color1:color2;
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
				imageView.setImage(MainController.responseImage);
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
	        HBox.setMargin(label, new Insets(2, 20, 2, 2));
	        
			vBox.getChildren().add(hBox);
		});  
	}
	
	public static void addJoinedProfileImageToVBox(VBox vBox, Dto dto) {
		Platform.runLater(() -> {
			ImageView imageView = new ImageView();
			imageView.setImage(MainController.responseImage);
			imageView.setFitWidth(50);
			imageView.setFitHeight(50);
			
			HBox hBox=new HBox();
			hBox.getChildren().addAll(imageView);
	        hBox.setAlignment(Pos.CENTER);
	        hBox.setPadding(new Insets(0, 0, 0, 0));
	        HBox.setMargin(imageView, new Insets(2, 2, 2, 2));
	        
	        Label label = new Label();
			label.setWrapText(true);
        	label.setPadding(new Insets(5, 5, 5, 5));
        	label.setFont(Font.font("System", FontWeight.BOLD, 12));
        	label.setTextFill(Color.WHITE);
        	label.setBackground(new Background(new BackgroundFill(Color.DARKCYAN, new CornerRadii(10), Insets.EMPTY)));
			label.setText("✔ "+dto.getExtraData()+" has joined to chat!"); //✔🗹
	        
	        VBox vB = new VBox();
	        vB.getChildren().addAll(hBox, label);
	        vB.setAlignment(Pos.CENTER);
	        
			vBox.getChildren().add(vB);
		});  
	}
	
	public static void setStatus(Label label, String text) {
		Label status = label;
		Platform.runLater(() -> {
			status.setText(text);
		});
	}
	
	public static void autoScrollDown(ScrollPane scrollPane, VBox vBox) {
		scrollPane.setVvalue(vBox.getHeight());
	}
}