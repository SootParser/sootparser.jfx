package thiagodnf.sootparser.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import soot.jimple.toolkits.callgraph.CallGraph;
import thiagodnf.sootparser.builder.Builder;
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
	
	public void restorePropertiesButton() {
		binaryClassesTextBox.setText(PreferencesUtil.restore("binary-classes"));
		toolsTextBox.setText(PreferencesUtil.restore("tools"));
		mainClassTextBox.setText(PreferencesUtil.restore("main-class"));
	}
	
	public void openPropertiesButton() {

		Stage stage = (Stage) parent.getScene().getWindow();
		
		final FileChooser fileChooser = new FileChooser();
		
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		
		File file = fileChooser.showOpenDialog(stage);
		
		if(file != null) {
			
			try {
				Properties prop = PreferencesUtil.load(file);
			
				binaryClassesTextBox.setText(prop.getProperty("binary-classes"));
				toolsTextBox.setText(prop.getProperty("tools"));
				mainClassTextBox.setText(prop.getProperty("main-class"));
			} catch (IOException ex) {
				MessageBox.exception(stage, ex);
				return;
			}
		}
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
		
		String binaryClasses = binaryClassesTextBox.getText();
		String toolsPath = toolsTextBox.getText();
		String mainClass = mainClassTextBox.getText();
		
		LOGGER.info("Saving the preferences");
		PreferencesUtil.save("binary-classes", binaryClasses);
		PreferencesUtil.save("tools", toolsPath);
		PreferencesUtil.save("main-class", mainClass);
		
		List<String> classes;
		List<String> tools;
		List<String> classpaths;
		
		try {
			classes = FileUtil.getClasses(binaryClasses);
			tools = FileUtil.getFiles(toolsPath);
			classpaths = sootService.getClasspaths(binaryClasses, tools);
		} catch (IOException ex) {
			MessageBox.exception(stage, ex);
			return;
		}
		
		Task<CallGraph> task = new Task<CallGraph>() {

			@Override
			protected CallGraph call() throws Exception {

				sootService.defineAllowPhantomRefs(allowPhantomRefsCheckbox.isSelected());
				sootService.defineVerbose(verboseCheckbox.isSelected());
				sootService.defineWholeProgram(wholeProgramCheckbox.isSelected());
				
				
				
				
				sootService.defineTheClassPath(classpaths);
				sootService.defineTheMainClass(mainClass);
				
				return sootService.buildCallGraph();
			}
		};
		
		task.setOnFailed(evt -> {
			MessageBox.exception(stage, task.getException());
		});
		
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				
				Builder builder = new Builder(task.getValue(), classes);
				
				System.out.println(builder.parse());
				
				MessageBox.info(stage, "Done");
			}
		});
		
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		executorService.submit(task);
		
		executorService.shutdown();
	}
}
