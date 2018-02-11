package thiagodnf.sootparser.component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MessageBox {
	
	public static void message(Stage stage, AlertType type, String title, String message) {
		
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.initOwner(stage);  
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}

	public static void warn(Stage stage, String message) {
		message(stage, AlertType.WARNING, "Warning", message);
	}
	
	public static void info(Stage stage, String message) {
		message(stage, AlertType.INFORMATION, "Information", message);
	}
	
	public static void error(Stage stage, String message) {
		message(stage, AlertType.ERROR, "Ooops...", message);
	}
}
