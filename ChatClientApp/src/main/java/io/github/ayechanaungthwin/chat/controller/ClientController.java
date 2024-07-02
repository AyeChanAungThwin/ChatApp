package io.github.ayechanaungthwin.chat.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ayechanaungthwin.chat.model.Client;
import io.github.ayechanaungthwin.chat.model.Dto;
import io.github.ayechanaungthwin.chat.model.FileUtils;
import io.github.ayechanaungthwin.chat.model.ImageJsonUtils;
import io.github.ayechanaungthwin.chat.model.Key;
import io.github.ayechanaungthwin.chat.model.StringEncryptionUtils;
import io.github.ayechanaungthwin.chat.model.UserInteractionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ClientController implements Initializable {

	private final ObjectMapper mapper = new ObjectMapper();
	public static final String SECRET_KEY = "4Y3CH4N4UN67HW1N";
	
	@FXML
    private Button btn;
	
	@FXML
	private ScrollPane scrollPane;
	
	@FXML
    private VBox vBox;
	
	@FXML
    private Label status;
	
	@FXML
	private TextField textInput;
	
	private Client client;
	private Socket soc;
	
	private Image responseImage = null;
	
	public ClientController() {
		new Thread(() -> {
			try {
				client = new Client(7777);
				soc = client.getSocket();
				
				if (soc.isConnected()) {
					setStatus("Client is connected to server!");
					sendImageThroughSocket();
				}
				
				while(soc.isConnected()) {
					BufferedReader reader = 
							new BufferedReader
							(
									new InputStreamReader(
											soc.getInputStream()
									)
							);
					String text = reader.readLine();
					String decryptedData = StringEncryptionUtils.decrypt(text, SECRET_KEY);
			
					Dto dto = mapper.readValue(decryptedData, Dto.class);
					if (dto.getKey()==Key.IMAGE_PROFILE) {
						if (responseImage==null) {
							BufferedImage bufferedImage = ImageJsonUtils.getBufferedImage(dto.getMessage());
							Image image = ImageJsonUtils.getImage(bufferedImage);						
							responseImage = image;
						}
					}
					else if (dto.getKey()==Key.IMAGE_PNG
							||dto.getKey()==Key.IMAGE_JPGE) {
						getImageFromSocketAndShow(dto);
					}
					else if (dto.getKey()==Key.ENTER_KEY) {
						text = dto.getMessage();
						addLabelToVBox(text, true); 
						removeHBoxById("typing-gif");
					}
					else if (dto.getKey()==Key.PROCESS_TYPING) {
						showTypingGif(dto.getMessage());
					}
					else {
						removeHBoxById("typing-gif");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				setStatus("Cannot connect to server!"); 
				client.close();
			}
		}).start();
	}
	
	private void getImageFromSocketAndShow(Dto dto) {
		Platform.runLater(() -> {
			BufferedImage bufferedImage = ImageJsonUtils.getBufferedImage(dto.getMessage());
			Image image = ImageJsonUtils.getImage(bufferedImage);						
			
			double width = image.getWidth();
			double height = image.getHeight();
			double percentage = 0;
			
			if (width>190) percentage = 190/width;
			
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(width*percentage);
			imageView.setFitHeight(height*percentage);
			
			HBox hBox=new HBox();
	        hBox.getChildren().addAll(imageView);
	        hBox.setAlignment(Pos.BASELINE_LEFT);
	        hBox.setPadding(new Insets(3, 3, 3, 3));
	        
			vBox.getChildren().add(hBox);
		});
	}
	
	private void sendImageThroughSocketFromFileChooser(String path) {
		try {
			PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
			
			//Object to json conversion.
			String json = ImageJsonUtils.getJson(path);
			Key extension=path.toLowerCase().contains(".png")?Key.IMAGE_PNG:Key.IMAGE_JPGE;
			
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
	
	private void sendImageThroughSocket() {
		try {
			PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
			
			//Object to json conversion.
			String json = ImageJsonUtils.getJson(FileUtils.getLogoPath());
			
			ObjectMapper mapper = new ObjectMapper();
        	String jsonString = mapper.writeValueAsString(new Dto(Key.IMAGE_PROFILE, json));
			
        	//Encrypt String before sending.
			String encryptedString = StringEncryptionUtils.encrypt(jsonString, SECRET_KEY);
			out.println(encryptedString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showTypingGif(String socketEndName) {
		Platform.runLater(() -> {
			Label label = new Label();
			label.setText(socketEndName);
			label.setFont(new Font(10));
			label.setMaxHeight(20);
			
			ImageView img = new ImageView();
			img.setFitWidth(25);
			img.setFitHeight(25);
			img.setImage(new Image(this.getClass().getResource("/images/typing.gif").toExternalForm()));
			
			HBox hBox=new HBox();
			hBox.setId("typing-gif");
	        hBox.getChildren().addAll(label, img);
	        hBox.setAlignment(Pos.BASELINE_LEFT);
	        hBox.setPadding(new Insets(0, 0, 0, 0));
	        
			vBox.getChildren().add(hBox);
		});
	}
	
	// Method to remove HBox by ID
    private void removeHBoxById(String id) {
        Platform.runLater(() -> {
        	vBox.getChildren().removeIf(node -> node instanceof HBox && id.equals(node.getId()));
        });
    }
	
	private void setStatus(String text) {
		Platform.runLater(() -> {
			status.setText(text);
		});
	}
	
	private void addLabelToVBox(String text, boolean isResponse) {
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
				imageView.setImage(responseImage);
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

	@FXML
	void onSendBtnPressed() {
		String text = textInput.getText().toString().trim();
		if (text.length()==0) return;
		
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
		
		addLabelToVBox(text, false);
        
        textInput.setText(""); //Reset input
	}
	
	private UserInteractionManager userInteractionManager = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		if (textInput==null) return;

		textInput.setOnKeyPressed( event -> {
			//System.out.println(textInput.getText().toString());
			if( event.getCode() == KeyCode.ENTER ) {
			    onSendBtnPressed();
			    return;
			}
			
			if (userInteractionManager==null) {
				userInteractionManager = new UserInteractionManager(client.getSocketName(), client.getSocket());
			}	
			userInteractionManager.interact();	
//			if (!TypingThread.typing) {
//				TypingThread tpT = new TypingThread();
//				tpT.setSocket(soc);
//				tpT.start();
//			}
		});
	}
	
	@FXML
    void onAttachFiles() {

    }
}