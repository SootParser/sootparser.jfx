package thiagodnf.sootparser.jfx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage stage) throws Exception {

		LOGGER.info("Starting SootParser JFX");

		String fxmlFile = "/fxml/home.fxml";
		LOGGER.debug("Loading FXML for main view from: {}", fxmlFile);
		FXMLLoader loader = new FXMLLoader();
		Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

		LOGGER.debug("Showing JFX scene");
		Scene scene = new Scene(rootNode);
		scene.getStylesheets().add("/styles/styles.css");

		stage.setTitle("SootParser JFX");
		stage.setScene(scene);
		stage.show();
	}
}
