package thiagodnf.sootparser.builder.generator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;

public class RelationGenerator extends AbstractGenerator {
	
	//Relation(GanttTXTOpen;load;setName,TaskImpl,String);
	
	public List<SootMethod> getTheCallIns(CallGraph cg, List<SootClass> classes, SootMethod method) {

		List<SootMethod> callIns = new ArrayList<SootMethod>();

		@SuppressWarnings("rawtypes")
		Iterator targets = new Targets(cg.edgesOutOf(method));

		while (targets.hasNext()) {
			
			SootMethod target = (SootMethod) targets.next();

			if (target.getName().contains("<clinit>")) {
				continue;
			}
			
			if(target.getName().startsWith("access$")) {
				continue;
			}
			
			// We want to avoid the call-in. So, the target and the method should be different
			if ( ! target.getDeclaringClass().equals(method.getDeclaringClass())) {
				continue;
			}

			// The target should be a method in the system class
			if (!contains(classes,target.getDeclaringClass().getName())) {
				continue;
			}

			// We should not include the same target
			if (callIns.contains(target)) {
				continue;
			}

			callIns.add(target);
		}

		return callIns;
	}
	
	public List<SootMethod> getTheCallOuts(CallGraph cg, List<SootClass> classes, SootMethod method) {

		List<SootMethod> callOuts = new ArrayList<SootMethod>();

		@SuppressWarnings("rawtypes")
		Iterator targets = new Targets(cg.edgesOutOf(method));

		while (targets.hasNext()) {
			
			SootMethod target = (SootMethod) targets.next();
			
			if (target.getName().contains("<clinit>")) {
				continue;
			}
			
			if(target.getName().startsWith("access$")) {
				continue;
			}

			// We want to avoid the call-in. So, the target and the method should be different
			if (target.getDeclaringClass().equals(method.getDeclaringClass())) {
				continue;
			}

			// The target should be a method in the system class
			if (!contains(classes, target.getDeclaringClass().getName())) {
				continue;
			}

			// We should not include the same target
			if (callOuts.contains(target)) {
				continue;
			}

			callOuts.add(target);
		}

		return callOuts;
	}
	
	public List<String> generate(SootMethod source, List<SootMethod> targets) {
		
		List<String> callOuts = new ArrayList<String>();

		for (SootMethod target : targets) {
			
			StringBuilder builder = new StringBuilder();

			builder.append("Relation(");
			builder.append(source.getDeclaringClass().getName());
			builder.append(",");
			builder.append(source.getNumber());
			builder.append(",");
			builder.append(source.getName());
			builder.append(",");
			builder.append(target.getNumber());
			builder.append(",");
			builder.append(target.getName());
			builder.append(",");
			builder.append(target.getDeclaringClass().getName());
			builder.append(");");

			callOuts.add(builder.toString());
		}

		return callOuts;
	}
	
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
			
			lines.addAll(generate(method, getTheCallOuts(cg, classes, method)));
			lines.addAll(generate(method, getTheCallIns(cg, classes, method)));
		}

		return lines;
	}

}
