package thiagodnf.sootparser.builder.generator;

import java.util.ArrayList;
import java.util.List;

import soot.SootClass;
import soot.jimple.toolkits.callgraph.CallGraph;

public class RealizationGenerator extends AbstractGenerator {
	
	protected String generate(SootClass cls) {
		StringBuilder builder = new StringBuilder();

		builder.append("Realization(");
		builder.append(cls.getName());
		builder.append(");");

		return builder.toString();
	}
	@Override
	public List<String> generate(CallGraph cg, SootClass cls, List<SootClass> classes) {
		
		List<String> lines = new ArrayList<String>();

		for (SootClass inter : cls.getInterfaces()) {
			
			// The target should be a method in the system class
			if (!contains(classes, inter.getName())) {
				continue;
			}
						
			lines.add(generate(inter));
		}

		return lines;
	}

}
