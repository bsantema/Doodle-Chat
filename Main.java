package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	static Stage primStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("PaintTestJFX.fxml"));

						BorderPane rootElement = (BorderPane) loader.load();

						Scene scene = new Scene(rootElement, 600, 400);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

						primaryStage.setTitle("Paint Test");
						primaryStage.setScene(scene);
						// show the GUI
						primaryStage.show();
						FXController controller = loader.getController();
						controller.initializeEditor();
						
						
						Image logo = new Image("file:DoodleChatSquareLogo.png");
						primaryStage.getIcons().add(logo);
						primStage = primaryStage;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Stage getPrimaryStage()
	{
		return primStage;
	}
}
