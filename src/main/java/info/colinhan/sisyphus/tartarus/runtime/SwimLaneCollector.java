package info.colinhan.sisyphus.tartarus.runtime;

import info.colinhan.sisyphus.tartarus.IndentingWriter;
import info.colinhan.sisyphus.tartarus.ModelToPlantUmlTransformer;
import info.colinhan.sisyphus.tartarus.model.*;

import java.util.HashSet;
import java.util.Set;

public class SwimLaneCollector extends AbstractModelVisitor<Object> {
    private final IndentingWriter writer;
    private final GetAssigneeExecutionContext getAssigneeExecutionContext;
    private final Set<String> swimLanes = new HashSet<>();
    private String firstLane = null;
    private boolean containsRobot = false;

    public SwimLaneCollector(IndentingWriter writer, GetAssigneeExecutionContext getAssigneeExecutionContext) {
        this.writer = writer;
        this.getAssigneeExecutionContext = getAssigneeExecutionContext;
    }

    private void addSwimLane(String name) {
        if (firstLane == null) {
            firstLane = name;
        }
        if (name.equals(Constants.ROBOT)) {
            containsRobot = true;
            return;
        }

        if (swimLanes.contains(name)) {
            return;
        }

        writeSwimLane(name);
    }

    private void writeSwimLane(String name) {
        if (swimLanes.size() % 2 == 0) {
            writer.writeLine("|%s|", name);
        } else {
            writer.writeLine("|#eee|%s|", name);
        }

        swimLanes.add(name);
    }

    @Override
    public Object visitFlow(Flow flow) {
        Object r = super.visitFlow(flow);
        if (containsRobot) {
            writeSwimLane(Constants.ROBOT);
        }

        writer.writeLine("");
        writer.writeLine("|%s|", firstLane);
        return r;
    }

    @Override
    public Object visitAction(Action action) {
        addSwimLane(action.getAssignee(getAssigneeExecutionContext));
        return super.visitAction(action);
    }

    @Override
    public Object visitWhileStatement(WhileStatement whileStatement) {
        String variableName = whileStatement.getVariableName();
        ArraySource arraySource = whileStatement.getArraySource();
        if (arraySource instanceof Reference) {
            getAssigneeExecutionContext.registerAlias(variableName, arraySource);
        }
        Object r = super.visitWhileStatement(whileStatement);
        getAssigneeExecutionContext.unregisterAlias(variableName);
        return r;
    }
}
