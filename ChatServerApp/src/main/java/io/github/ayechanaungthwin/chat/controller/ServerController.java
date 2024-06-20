package io.github.ayechanaungthwin.chat.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ayechanaungthwin.chat.model.Dto;
import io.github.ayechanaungthwin.chat.model.Server;
import io.github.ayechanaungthwin.chat.model.TypingThread;
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

public class ServerController implements Initializable {
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@FXML
    private Button btn;
	
	@FXML
	private ScrollPane scrollPane;
	
	@FXML
    private VBox vBox;
	
	@FXML
    private ImageView imageView;
	
	@FXML
    private Label status;
	
	@FXML
	private TextField textInput;
	
	private Server server;
	private Socket soc;
	
	public ServerController() {
		//7imageView.setImage(new Image(this.getClass().getResource("/images/typing.gif").toExternalForm()));
		new Thread(() -> {
			try {
				server = new Server(7777);
				setStatus("Waiting for client...");
				  
				soc = server.getServerSocket().accept();
				server.setSocket(soc);
				setStatus("Client is connected...");
				
				while(soc.isConnected()) {
					BufferedReader reader = 
							new BufferedReader
							(
								new InputStreamReader
								(
									soc.getInputStream()
								)
							);
					String text = reader.readLine();
			
					if (text.contains("EnTeR834")) {
						text = text.replaceAll("@EnTeR834", "");
						addLabelToVBox(text, true); 
						removeHBoxById("typing-gif");
					}
					else {
						//setStatus(text);
						Dto dto = mapper.readValue(text, Dto.class);
						if (dto.getMessage().equals("typing")) {
							showTypingGif(dto.getName());
						}
						else {
							removeHBoxById("typing-gif");
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				server.close();
			}
		}).start();
	}
	
	public void showTypingGif(String socketEndName) {
		Platform.runLater(() -> {
			Label label = new Label();
			label.setText(socketEndName+" is typing : ");
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
	
	private void addLabelToVBox(String text, boolean isServerResponse) {
		Color color = isServerResponse?Color.CYAN:Color.LIGHTGREEN;
		Pos pos = isServerResponse?Pos.BASELINE_LEFT:Pos.BASELINE_RIGHT;
		int padding = isServerResponse?10:0;
		
		Platform.runLater(() -> {
			Label label = new Label();
			label.setWrapText(true);
        	label.setPadding(new Insets(5, 5, 5, 5));
        	label.setBackground(new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY)));
			label.setText(text);
			
			HBox hBox=new HBox();
	        hBox.getChildren().add(label);
	        hBox.setAlignment(pos);
	        hBox.setPadding(new Insets(0, 0, 0, padding));
	        
			vBox.getChildren().add(hBox);
		});  
	}

	@FXML
	void onSendBtnPressed() {
		String text = textInput.getText().toString().trim();
		if (text.length()==0) return;
		
		try {
			PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
			out.println(text+"@EnTeR834");
		}
		catch (Exception e) {
			e.printStackTrace();
			setStatus("Not connected to client yet!");
		}
		
		addLabelToVBox(text, false);
        
        textInput.setText(""); //Reset input
	}
	
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
			
			if (!TypingThread.typing) {
				TypingThread tpT = new TypingThread();
				tpT.setSocket(soc);
				tpT.start();
			}
		});
	}
}
