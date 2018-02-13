package thiagodnf.sootparser.builder.generator;

import java.util.Arrays;
import java.util.List;

import soot.SootClass;
import soot.jimple.toolkits.callgraph.CallGraph;

public class ClassGenerator extends AbstractGenerator {

	public String isInterface(SootClass cls) {
		return cls.isInterface() ? "Y" : "N";
	}

	public String isAbstract(SootClass cls) {
		return cls.isAbstract() ? "Y" : "N";
	}

	public String getVisibility(SootClass cls) {
		if (cls.isPrivate()) {
			return "Private";
		} else if (cls.isPublic()) {
			return "Public";
		} else if (cls.isProtected()) {
			return "Protected";
		} else {
			return "NA";
		}
	}
	
	@Override
	public List<String> generate(CallGraph cg, SootClass cls, List<SootClass> classes) {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("Class(");
		builder.append(cls.getName());
		builder.append(",");
		builder.append(isInterface(cls));
		builder.append(",");
		builder.append(isAbstract(cls));
		builder.append(",");
		builder.append(getVisibility(cls));
		builder.append(");");
		
		return Arrays.asList(builder.toString());
	}

}
