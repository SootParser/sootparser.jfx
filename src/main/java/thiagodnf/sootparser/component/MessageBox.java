package thiagodnf.sootparser.component;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MessageBox {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageBox.class);
	
	public static void message(Stage stage, AlertType type, String title, String message) {
		
		LOGGER.info(type + ": " + message);
		
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
	
	public static void exception(Stage stage, Throwable throwable) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Ooops...");
        alert.setHeaderText(null);
        alert.setContentText(throwable.getMessage());
        alert.initOwner(stage);
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Details:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
	}
}
