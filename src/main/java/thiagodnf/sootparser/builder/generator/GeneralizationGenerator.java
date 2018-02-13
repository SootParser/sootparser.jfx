package thiagodnf.sootparser.builder.generator;

import java.util.ArrayList;
import java.util.List;

import soot.SootClass;
import soot.jimple.toolkits.callgraph.CallGraph;

public class GeneralizationGenerator extends AbstractGenerator {
	
	protected String generate(SootClass cls) {
		StringBuilder builder = new StringBuilder();

		builder.append("Generalization(");
		builder.append(cls.getName());
		builder.append(");");

		return builder.toString();
	}
	@Override
	public List<String> generate(CallGraph cg, SootClass cls, List<SootClass> classes) {
		
		List<String> lines = new ArrayList<String>();

		if (!cls.isInterface()) {
			if (cls.hasSuperclass()) {
				if (classes.stream().anyMatch(x -> x.getName().equalsIgnoreCase(cls.getSuperclass().getName()))) {
					lines.add(generate(cls.getSuperclass()));
				}
			}
		}

		return lines;
	}

}
