package thiagodnf.sootparser.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;
import thiagodnf.sootparser.util.OSUtil;

public class SootService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SootService.class);
	
	public void defineTheClassPath(List<String> classpath) {

		LOGGER.info("Defining the Classpaths");

		String delimiter = ":";

		if (OSUtil.isWindows()) {
			delimiter = ";";
		}

		Scene.v().setSootClassPath(String.join(delimiter, classpath));

		classpath.stream().forEach(LOGGER::info);
	}
	
	public void defineTheMainClass(String cls) {

		LOGGER.info("Defining the Main Class");

		SootClass mainClass = Scene.v().loadClassAndSupport(cls);
		mainClass.setApplicationClass();
		Scene.v().setMainClass(mainClass);

		LOGGER.info("Name: " + mainClass.getName());
		LOGGER.info("Short Name: " + mainClass.getShortName());

		List<SootMethod> ep = Scene.v().getEntryPoints();

		LOGGER.info("Entering points:");
		ep.stream().map(m -> m.toString()).forEach(LOGGER::info);
		
		ep.add(Scene.v().getMainMethod());
		Scene.v().setEntryPoints(ep);
	}
	
	public List<String> getClasspaths(String jarFile, List<String> tools) {

		List<String> classpaths = new ArrayList<>();
		
		String userDir = System.getProperty("user.dir");

		String dir = userDir + File.separator + "lib" + File.separator + "java-1.7";
		
		classpaths.add(dir + File.separator + "javaws.jar");
		classpaths.add(dir + File.separator + "jce.jar");
		classpaths.add(dir + File.separator + "jsse.jar");
		classpaths.add(dir + File.separator + "rt.jar");

		classpaths.addAll(tools);
		classpaths.add(jarFile);

		return classpaths;
	}
	
	public CallGraph buildCallGraph() {

		LOGGER.info("Building the CallGraph");

		Scene.v().loadNecessaryClasses();

		CHATransformer.v().transform();

		return Scene.v().getCallGraph();
	}
	
	public void defineVerbose(boolean activated) {
		LOGGER.info("Parameter Verbose: " + activated);
		Options.v().setPhaseOption("cg", "verbose:" + activated);
	}
	
	public void defineWholeProgram(boolean activated) {
		LOGGER.info("Parameter Whole Program: " + activated);
		Options.v().set_whole_program(activated);
	}
	
	public void defineAllowPhantomRefs(boolean activated) {
		LOGGER.info("Parameter Allow Phantom Refs: " + activated);
		Options.v().set_allow_phantom_refs(activated);
	}
}
