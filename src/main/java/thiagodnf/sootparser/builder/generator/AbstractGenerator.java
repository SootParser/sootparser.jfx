package thiagodnf.sootparser.builder.generator;

import java.util.List;

import soot.SootClass;
import soot.jimple.toolkits.callgraph.CallGraph;

public abstract class AbstractGenerator {

	protected boolean contains(List<SootClass> classes, SootClass cls) {
		return contains(classes, cls.getName());
	}
	
	protected boolean contains(List<SootClass> classes, String className) {
		return classes
				.stream()
				.anyMatch(x -> x.getName().equalsIgnoreCase(className));
	}
	
	public abstract List<String> generate(CallGraph cg, SootClass cls, List<SootClass> classes);

}
