package thiagodnf.sootparser.jfx.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;
import thiagodnf.sootparser.jfx.util.OSUtil;

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

		LOGGER.info("\t Name: " + mainClass.getName());
		LOGGER.info("\t Short Name: " + mainClass.getShortName());

		List<SootMethod> ep = Scene.v().getEntryPoints();

		System.out.println(ep);

		ep.add(Scene.v().getMainMethod());
		Scene.v().setEntryPoints(ep);
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
