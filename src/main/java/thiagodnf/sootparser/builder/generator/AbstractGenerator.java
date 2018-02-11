package thiagodnf.sootparser.builder.generator;

import java.util.List;

import soot.SootClass;
import soot.jimple.toolkits.callgraph.CallGraph;

public abstract class AbstractGenerator {

	public abstract List<String> generate(CallGraph cg, SootClass cls);

}
