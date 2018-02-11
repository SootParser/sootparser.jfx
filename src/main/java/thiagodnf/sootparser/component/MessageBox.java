package thiagodnf.sootparser.component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MessageBox {

	public static void warn(String message) {
		
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}

	public static void info(String message) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}
	
	public static void error(String message) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Ooops...");
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}
}
