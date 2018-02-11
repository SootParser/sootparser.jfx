package thiagodnf.sootparser.jfx.controller;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import thiagodnf.sootparser.jfx.component.MessageBox;
import thiagodnf.sootparser.jfx.service.SootService;

public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@FXML
	private TextField binaryClassesTextBox;
	
	@FXML
	private TextField toolsTextBox;
	
	@FXML
	private TextField mainClassTextBox;
	
	@FXML
	private CheckBox verboseCheckbox;
	
	@FXML
	private CheckBox wholeProgramCheckbox;
	
	@FXML
	private CheckBox allowPhantomRefsCheckbox;
	
	@FXML
	private Pane parent;
	
	private SootService sootService = new SootService();

	public void openBinaryClasses() {

		LOGGER.info("Opening Directory Chooser for Binary Classes");

		File selectedDirectory = showDirectoryChooser();
		
		if (selectedDirectory != null) {
			binaryClassesTextBox.setText(selectedDirectory.getAbsolutePath());
		}
	}
	
	public void openTools() {

		LOGGER.info("Opening Directory Chooser for Tools");

		File selectedDirectory = showDirectoryChooser();
		
		if (selectedDirectory != null) {
			toolsTextBox.setText(selectedDirectory.getAbsolutePath());
		}
	}
	
	private File showDirectoryChooser() {
		
		Stage stage = (Stage) parent.getScene().getWindow();
		
		final DirectoryChooser fileChooser = new DirectoryChooser();
		
		return fileChooser.showDialog(stage);
	}
	
	public void close() {
		System.exit(0);
	}

	public void generate() {
		
		if (StringUtils.isEmpty(binaryClassesTextBox.getText())) {
			MessageBox.error("Binary Class is required");
			return;
		}
		
		if (StringUtils.isEmpty(mainClassTextBox.getText())) {
			MessageBox.error("Main Class is required");
			return;
		}
		
		sootService.defineAllowPhantomRefs(allowPhantomRefsCheckbox.isSelected());
		sootService.defineVerbose(verboseCheckbox.isSelected());
		sootService.defineWholeProgram(wholeProgramCheckbox.isSelected());
		
		
	}
}
