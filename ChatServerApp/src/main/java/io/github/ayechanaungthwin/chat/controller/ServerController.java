package io.github.ayechanaungthwin.chat.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ayechanaungthwin.chat.cor.EnterKeyHandler;
import io.github.ayechanaungthwin.chat.cor.Handler;
import io.github.ayechanaungthwin.chat.cor.ImagePngJpegHandler;
import io.github.ayechanaungthwin.chat.cor.ProcessIdleTypingHandler;
import io.github.ayechanaungthwin.chat.cor.ProcessTypingHandler;
import io.github.ayechanaungthwin.chat.cor.ProfileImageHandler;
import io.github.ayechanaungthwin.chat.model.Dto;
import io.github.ayechanaungthwin.chat.model.Server;
import io.github.ayechanaungthwin.chat.model.UserInteractionManager;
import io.github.ayechanaungthwin.chat.ui.JfxDynamicUiChangerUtils;
import io.github.ayechanaungthwin.chat.utils.StringEncryptionUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ServerController implements Initializable {
	
	private final ObjectMapper mapper = new ObjectMapper();
	public static final String SECRET_KEY = "4Y3CH4N4UN67HW1N";
	
	@FXML
    private Button btn;
	
	@FXML
	private ScrollPane scrollPane;
	
	@FXML
    private VBox vBox;
	
	@FXML
	private TextField textInput;
	
	@FXML
    private ImageView profileImageView;
	
	private Server server;
	private Socket soc;
	
	public static Image responseImage = null;
	
	public ServerController() {
		new Thread(() -> {
			try {
				server = new Server(7777); 
				Thread.sleep(300); 
				/* Since this thread runs concurrently 
				 * with the construction of the constructor, 
				 * if status is called before the status field 
				 * has been initialized, a NullPointerException 
				 * may occur. So you can wait with the sleep thread
				 * until the initialization is done. 
				 * This is also related to the performance of your CPU, 
				 * so consider increasing the sleep time
				 * when necessary.*/
				//JfxDynamicUiChangerUtils.setStatus(status, "Waiting for client...");
				
				soc = server.getServerSocket().accept(); 
				server.setSocket(soc);
				
				if (soc.isConnected()) {
					//JfxDynamicUiChangerUtils.setStatus(status, "Client has joined to the chat...");
					JfxDynamicUiChangerUtils.pushImageToSocketOnConnected(soc, SECRET_KEY, server.getSocketName());
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
					if (text==null) continue;
					String decryptedData = StringEncryptionUtils.decrypt(text, SECRET_KEY);
					
					//JSON to object
					Dto dto = mapper.readValue(decryptedData, Dto.class);
					
//					if (dto.getKey()==Key.PROFILE_IMAGE) {
//						BufferedImage bufferedImage = ImageJsonUtils.getBufferedImage(dto.getMessage());
//						Image image = ImageJsonUtils.getImage(bufferedImage);
//						profileImageView.setImage(image);
//						continue;
//					}
					//Using COR to check Keys
					Handler hdl0 = new ProfileImageHandler();
					Handler hdl1 = new ImagePngJpegHandler();
					Handler hdl2 = new EnterKeyHandler();
					Handler hdl3 = new ProcessTypingHandler();
					Handler hdl4 = new ProcessIdleTypingHandler();
					
					hdl0.setSuccessor(hdl1);
					hdl1.setSuccessor(hdl2);
					hdl2.setSuccessor(hdl3);
					hdl3.setSuccessor(hdl4);
					
					hdl0.handleRequest(vBox, dto);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				//JfxDynamicUiChangerUtils.setStatus(status, "Cannot connect to server!"); 
				server.close();
			}
		}).start();
	}
	
	private Stage primaryStage;
	
	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@FXML
	void onSendBtnPressed() {
		String text = textInput.getText().toString().trim();
		if (text.length()==0) return;
		
		JfxDynamicUiChangerUtils.pushTextToSocket(soc, SECRET_KEY, text);
		JfxDynamicUiChangerUtils.addLabelToVBox(vBox, text, false);
        
        textInput.setText(""); //Reset input
	}
	
	private UserInteractionManager userInteractionManager = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		if (textInput==null) return;

		textInput.setOnKeyPressed( event -> {
			if( event.getCode() == KeyCode.ENTER ) {
			    onSendBtnPressed();
			    return;
			}
			
			//Typing Thread
			if (userInteractionManager==null) {
				userInteractionManager = new UserInteractionManager(server.getSocketName(), server.getSocket());
			}	
			userInteractionManager.interact();	
		});
		
		//JavaFX auto-scroll down scrollpane
		scrollPane.vvalueProperty().bind(vBox.heightProperty());
	}
	
	@FXML
    void onAttachFiles() {
		// Create a FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
    			new ExtensionFilter("PNG IMAGE", "*.png"),
    			new ExtensionFilter("JPG IMAGE", "*.jpg"),
    			new ExtensionFilter("JPEG IMAGE", "*.jpeg")
    		);
        fileChooser.setTitle("Open Image File");

        // Show the FileChooser in a modal window
        Stage fileChooserStage = new Stage();
        fileChooserStage.initModality(Modality.WINDOW_MODAL);
        fileChooserStage.initOwner(primaryStage);
        
        // Show the FileChooser and wait for user action
		File selectedFile = fileChooser.showOpenDialog(fileChooserStage);
		if (selectedFile==null) return;
		
		//System.out.println(selectedFile.getAbsolutePath());
		JfxDynamicUiChangerUtils.pushImageToSocketWithFileChooser(soc, SECRET_KEY, selectedFile.getAbsolutePath());
		JfxDynamicUiChangerUtils.addImageToVBox(vBox, selectedFile.getAbsolutePath());
    }
}