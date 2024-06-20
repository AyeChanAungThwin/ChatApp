package io.github.ayechanaungthwin.chat.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import io.github.ayechanaungthwin.chat.model.Server;
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

public class ServerController implements Initializable {

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
	
	private Server server;
	private Socket soc;
	
	public ServerController() throws IOException {
		new Thread(() -> {
			try {
				server = new Server(7777);
				Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		            	status.setText("Waiting for client...");
		            }
		        });  
				soc = server.getServerSocket().accept();
				Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		            	status.setText("Client is connected...");
		            }
		        });
				
				while(soc.isConnected()) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
							soc.getInputStream()));
					String data = reader.readLine();
			
					Platform.runLater(new Runnable() {
			            @Override
			            public void run() {
			            	Label label = new Label();
			            	label.setPadding(new Insets(5, 5, 5, 5));
			            	label.setBackground(new Background(new BackgroundFill(Color.CYAN, new CornerRadii(10), Insets.EMPTY)));
							label.setText(data);
							HBox hBox=new HBox();
					        hBox.getChildren().add(label);
					        hBox.setAlignment(Pos.BASELINE_LEFT);
					        hBox.setPadding(new Insets(0, 0, 0, 10));
							vBox.getChildren().add(hBox);
			            }
			        });  
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					server.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}).start();
	}

	@FXML
	void onSendBtnPressed() {
		String text = textInput.getText().toString().trim();
		if (text.length()==0) return;
		
		try {
			PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
			out.println(text);
		}
		catch (Exception e) {
			e.printStackTrace();
			Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	            	//status.setTextFill(Color.color(255, 0, 0));
	            	status.setText("Not connected to client yet!");
	            }
	        });
		}
		
		Label label = new Label();
    	label.setPadding(new Insets(5, 5, 5, 5));
    	label.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), Insets.EMPTY)));
		label.setText(text);
		HBox hBox=new HBox();
        hBox.getChildren().add(label);
        hBox.setAlignment(Pos.BASELINE_RIGHT);
        hBox.setPadding(new Insets(0, 0, 0, 0));
        vBox.getChildren().add(hBox);
        
        textInput.setText("");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		if (textInput==null) return;
		textInput.setOnKeyPressed( event -> {
			if( event.getCode() == KeyCode.ENTER ) {
			    onSendBtnPressed();
			}
		});
	}
}
