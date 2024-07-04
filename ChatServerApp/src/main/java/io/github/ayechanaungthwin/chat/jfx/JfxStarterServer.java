package io.github.ayechanaungthwin.chat.jfx;

import io.github.ayechanaungthwin.chat.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class JfxStarterServer extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub  
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Server.fxml"));
	    Parent root = (Parent) loader.load();
	    
	    MainController controller = (MainController) loader.getController();
	    controller.setStage(primaryStage);
	    
	    Scene scene = new Scene(root);
	    primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image(this.getClass().getResource("/images/Server.png").toExternalForm()));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void execute(String[] args) {
		launch(args);
	}
}
