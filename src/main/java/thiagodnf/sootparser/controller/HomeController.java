package thiagodnf.sootparser.controller;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import soot.jimple.toolkits.callgraph.CallGraph;
import thiagodnf.sootparser.component.MessageBox;
import thiagodnf.sootparser.service.SootService;
import thiagodnf.sootparser.util.FileUtil;
import thiagodnf.sootparser.util.PreferencesUtil;

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
	
	public void restoreButton() {
		binaryClassesTextBox.setText(PreferencesUtil.restore("binary-classes"));
		toolsTextBox.setText(PreferencesUtil.restore("tools"));
		mainClassTextBox.setText(PreferencesUtil.restore("main-class"));
	}
	
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
		
		Stage stage = (Stage) parent.getScene().getWindow();
		
		if (StringUtils.isEmpty(binaryClassesTextBox.getText())) {
			MessageBox.error(stage, "Binary Class is required");
			return;
		}
		
		if (StringUtils.isEmpty(mainClassTextBox.getText())) {
			MessageBox.error(stage, "Main Class is required");
			return;
		}
		
		LOGGER.info("Saving the preferences");
		PreferencesUtil.save("binary-classes", binaryClassesTextBox.getText());
		PreferencesUtil.save("tools", toolsTextBox.getText());
		PreferencesUtil.save("main-class", mainClassTextBox.getText());
		
		Task<CallGraph> task = new Task<CallGraph>() {

			@Override
			protected CallGraph call() throws Exception {

				sootService.defineAllowPhantomRefs(allowPhantomRefsCheckbox.isSelected());
				sootService.defineVerbose(verboseCheckbox.isSelected());
				sootService.defineWholeProgram(wholeProgramCheckbox.isSelected());
				
				List<String> tools = FileUtil.getFiles(toolsTextBox.getText());
				List<String> classpaths = sootService.getClasspaths(binaryClassesTextBox.getText(), tools);
				
				sootService.defineTheClassPath(classpaths);
				sootService.defineTheMainClass(mainClassTextBox.getText());
				
				return sootService.buildCallGraph();
			}
		};
		
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				MessageBox.info(stage, "Done");
				
				//System.out.println(task.getValue());
				
			}
		});
		
		new Thread(task).start();
	}
}
