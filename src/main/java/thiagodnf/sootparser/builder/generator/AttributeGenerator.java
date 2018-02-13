package thiagodnf.sootparser.builder.generator;

import java.util.ArrayList;
import java.util.List;

import soot.SootClass;
import soot.SootField;
import soot.jimple.toolkits.callgraph.CallGraph;

public class AttributeGenerator extends AbstractGenerator {

	public String isStatic(SootField field) {
		return field.isStatic() ? "Y" : "N";
	}

	public String isFinal(SootField field) {
		return field.isFinal() ? "Y" : "N";
	}
	
	public String getVisibility(SootField field) {
		if (field.isPrivate()) {
			return "Private";
		} else if (field.isPublic()) {
			return "Public";
		} else if (field.isProtected()) {
			return "Protected";
		} else {
			return "Public";
		}
	}
	
	protected String generate(SootField field) {

		StringBuilder builder = new StringBuilder();

		builder.append("Attribute(");
		builder.append(field.getName());
		builder.append(",");
		builder.append(field.getType());
		builder.append(",");
		builder.append(getVisibility(field));
		builder.append(",");
		builder.append(isStatic(field));
		builder.append(",");
		builder.append(isFinal(field));
		builder.append(");");

		return builder.toString();
	}
	
	@Override
	public List<String> generate(CallGraph cg, SootClass cls, List<SootClass> classes) {
		
		List<String> attributes = new ArrayList<>();
		
		for (SootField field : cls.getFields()) {
			
			if(field.getName().startsWith("this$")) {
				continue;
			}
			
			attributes.add(generate(field));
		}
		
		return attributes;
	}

}
