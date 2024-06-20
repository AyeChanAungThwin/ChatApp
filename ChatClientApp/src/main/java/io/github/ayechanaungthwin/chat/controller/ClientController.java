package io.github.ayechanaungthwin.chat.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import io.github.ayechanaungthwin.chat.model.Client;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ClientController implements Initializable {

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
	
	public ClientController() throws IOException {
		new Thread(() -> {
			try {
				client = new Client(7777);
				soc = client.getSocket();
				setStatus("Client is connected to server!");
				
				while(soc.isConnected()) {
					BufferedReader reader = 
							new BufferedReader
							(
									new InputStreamReader(
											soc.getInputStream()
									)
							);
					String text = reader.readLine();
			
					if (text.contains("EnTeR834")) {
						text = text.replaceAll("@EnTeR834", "");
						addLabelToVBox(text, true); 
					}
					else {
						setStatus(text);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setStatus("Cannot connect to server!"); 
				client.close();
			}
		}).start();
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
		}
		
		addLabelToVBox(text, false);
        
        textInput.setText("");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		if (textInput==null) return;

		textInput.setOnKeyPressed( event -> {
			//System.out.println(textInput.getText().toString());
			if (!TypingThread.typing) {
				TypingThread tpT = new TypingThread();
				tpT.setSocket(soc);
				tpT.start();
			}
			
			if( event.getCode() == KeyCode.ENTER ) {
			    onSendBtnPressed();
			}
		});
	}
}
