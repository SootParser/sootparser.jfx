package thiagodnf.sootparser.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import soot.Scene;
import soot.SootClass;
import soot.jimple.toolkits.callgraph.CallGraph;
import thiagodnf.sootparser.builder.generator.AbstractGenerator;
import thiagodnf.sootparser.builder.generator.AttributeGenerator;
import thiagodnf.sootparser.builder.generator.ClassGenerator;
import thiagodnf.sootparser.builder.generator.GeneralizationGenerator;
import thiagodnf.sootparser.builder.generator.MethodGenerator;
import thiagodnf.sootparser.builder.generator.ParameterGenerator;
import thiagodnf.sootparser.builder.generator.RealizationGenerator;
import thiagodnf.sootparser.builder.generator.RelationGenerator;

public class Builder {
	
	/** 
	 * It represents the edges in a call graph
	 */
	protected CallGraph cg;
	
	/**
	 * This array saves the found class in the call graph that are
	 * the same in the system under analysis. This avoid to process
	 * class like Object
	 */
	protected List<SootClass> classes;
	
	/**
	 * The generators responsible for generating the content for the file
	 */
	protected List<AbstractGenerator> generators;
	
	public Builder(CallGraph cg, List<String> classes) {
		
		this.cg = cg;
		this.classes = new ArrayList<>();
		this.generators = new ArrayList<>();
		
		this.addGenerator(new ClassGenerator());
		this.addGenerator(new AttributeGenerator());
		this.addGenerator(new ParameterGenerator());
		this.addGenerator(new MethodGenerator());
		this.addGenerator(new GeneralizationGenerator());
		this.addGenerator(new RealizationGenerator());
		this.addGenerator(new RelationGenerator());
		
		// We will process only the class of the system under analysis.
		// So, we need to filter these one
		for (SootClass c : Scene.v().getClasses()) {
			if (classes.contains(c.toString())) {
				if(Pattern.compile("((.+)\\$)+(\\d+)").matcher(c.getName()).find()) {
					continue;
				}
				
				this.classes.add(c);
			}
		}
	}
	
	public void addGenerator(AbstractGenerator generator) {
		this.generators.add(generator);
	}
	
	public String parse() {
		
		List<String> rows = new ArrayList<>();

		for (int i = 0; i < classes.size(); i++) {

			SootClass cls = classes.get(i);

			rows.add("StartClass " + (i + 1));

			for (AbstractGenerator generator : getGenerators()) {
				rows.addAll(generator.generate(cg, cls, classes));
			}

			rows.add("EndClass " + (i + 1));
		}
		
		return StringUtils.join(rows, "\n");
	}

	public List<AbstractGenerator> getGenerators() {
		return generators;
	}
	
}
