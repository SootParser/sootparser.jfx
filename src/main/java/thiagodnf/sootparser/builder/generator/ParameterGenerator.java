package thiagodnf.sootparser.builder.generator;

import java.util.ArrayList;
import java.util.List;

import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.jimple.toolkits.callgraph.CallGraph;

public class ParameterGenerator extends AbstractGenerator {
	
	protected String generate(SootMethod method, Type parameter) {

		StringBuilder builder = new StringBuilder();

		builder.append("Parameter(");
		builder.append(method.getNumber());
		builder.append(",");
		builder.append(parameter.toString());
		builder.append(");");

		return builder.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> generate(CallGraph cg, SootClass cls, List<SootClass> classes) {
		
		List<String> lines = new ArrayList<String>();

		for (SootMethod method : cls.getMethods()) {
			
			if (method.getName().contains("<clinit>")) {
				continue;
			}
			
			if(method.getName().startsWith("access$")) {
				continue;
			}
			
			List<Type> types = method.getParameterTypes();
			
			for(Type type : types) {
				lines.add(generate(method, type));
			}
		}

		return lines;
	}

}
