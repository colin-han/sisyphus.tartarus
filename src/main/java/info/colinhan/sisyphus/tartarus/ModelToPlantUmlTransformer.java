package info.colinhan.sisyphus.tartarus;

import info.colinhan.sisyphus.tartarus.model.*;
import info.colinhan.sisyphus.tartarus.runtime.GetAssigneeExecutionContext;
import info.colinhan.sisyphus.tartarus.runtime.SwimLaneCollector;

import java.io.FileWriter;
import java.util.*;

public class ModelToPlantUmlTransformer extends AbstractModelVisitor<Object> {
    public ModelToPlantUmlTransformer(StringBuilder stringBuilder) {
        this.writer = new IndentingWriter(stringBuilder);
    }

    public ModelToPlantUmlTransformer(StringBuffer stringBuffer) {
        this.writer = new IndentingWriter(stringBuffer);
    }

    public ModelToPlantUmlTransformer(FileWriter fileWriter) {
        this.writer = new IndentingWriter(fileWriter);
    }

    private static class SwimLane {
        private String name;
        private String text;
    }

    private final IndentingWriter writer;
    private final GetAssigneeExecutionContext getAssigneeExecutionContext = new GetAssigneeExecutionContext();

    private void start(Flow flow) {
        this.writer.writeLine("@startuml");
        SwimLaneCollector swimLaneCollector = new SwimLaneCollector(this.writer, this.getAssigneeExecutionContext);
        flow.accept(swimLaneCollector);
        this.writer.writeLine("start");
        this.writer.indent();
    }

    private void stop() {
        this.writer.unindent();
        this.writer.writeLine("stop");
        this.writer.writeLine("@enduml");
    }

    @Override
    public Object visitFlow(Flow flow) {
        this.start(flow);
        super.visitFlow(flow);
        this.stop();
        return null;
    }


    @Override
    public Object visitAction(Action action) {
        this.writer.writeLine("|%s|", action.getAssignee(getAssigneeExecutionContext));
        this.writer.writeIndent();
        this.writer.write(": **\"\"");
        this.writer.write(action.getName());
        this.writer.write("\"\"** ");
        this.visit(action.getPositionedParameter());
        Map<String, TemplateString> namedParameters = action.getNamedParameters();
        if (!namedParameters.isEmpty()) {
            int maxLength = namedParameters.keySet().stream()
                    .map(String::length)
                    .max(Integer::compareTo)
                    .orElseThrow(() -> new IllegalStateException("No named parameters")) + 4;
            namedParameters.forEach((k, v) -> {
                this.writer.write("\n");
                this.writer.writeIndent();
                this.writer.write("\"\"  ");
                this.writer.write(String.format("%1$" + maxLength + "s", "__" + k + "__"));
                this.writer.write(":\"\" ");
                this.visit(v);
            });
        }
        this.writer.write(";\n");
        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement) {
        this.writer.writeLine("|%s|", ifStatement.getAssignee(getAssigneeExecutionContext));
        this.writer.writeIndent();
        this.writer.write("if (");
        this.visit(ifStatement.getThenBlocks().get(0).getCondition());
        this.writer.write(") then (yes)\n");
        this.writer.indent();
        this.visit(ifStatement.getThenBlocks().get(0).getBlock());
        this.writer.unindent();
        for (int i = 1; i < ifStatement.getThenBlocks().size(); i++) {
            this.writer.writeIndent();
            this.writer.write("elseif (");
            this.visit(ifStatement.getThenBlocks().get(i).getCondition());
            this.writer.write(") then (yes)\n");
            this.writer.indent();
            this.visit(ifStatement.getThenBlocks().get(i).getBlock());
            this.writer.unindent();
        }
        if (ifStatement.getElseBlock() != null) {
            this.writer.writeLine("else (no)");
            this.writer.indent();
            this.visit(ifStatement.getElseBlock());
            this.writer.unindent();
        }
        this.writer.writeLine("endif");
        return null;
    }

    @Override
    public Object visitLiteral(Literal literal) {
        this.writer.write(literal.getValue());
        return null;
    }

    @Override
    public Object visitReference(Reference reference) {
        if (reference.getDefaultValue() == null) {
            this.writer.write(reference.getType().getValue() + reference.getVariableName());
        } else {
            this.writer.write(String.format("%s{%s:%s}",
                    reference.getType().getValue(),
                    reference.getVariableName(),
                    reference.getDefaultValue()));
        }
        return null;
    }

    @Override
    public Object visitWhileStatement(WhileStatement whileStatement) {
        String variableName = whileStatement.getVariableName();
        getAssigneeExecutionContext.registerAlias(variableName, whileStatement.getArraySource());
        String assignee = whileStatement.getAssignee(getAssigneeExecutionContext);
        this.writer.writeLine("|%s|", assignee);
        if (whileStatement.isParallel()) {
            this.writer.writeLine("fork");
            this.writer.indent();
            this.writer.writeIndent();
            this.writer.write("-> ");
            this.writer.write(whileStatement.getVariableName());
            this.writer.write(" in ");
            this.visit(whileStatement.getArraySource());
            this.writer.write(";\n");
            this.visit(whileStatement.getBlock());
            this.writer.unindent();
            this.writer.writeLine("fork again");
            this.writer.indent();
            this.writer.writeLine(": ...;");
            this.writer.unindent();
            this.writer.writeLine("end fork");
        } else {
            this.writer.writeIndent();
            this.writer.write("while (");
            this.writer.write(whileStatement.getVariableName());
            this.writer.write(" in ");
            this.visit(whileStatement.getArraySource());
            this.writer.write(")\n");
            this.writer.indent();
            this.visit(whileStatement.getBlock());
            this.writer.unindent();
            this.writer.writeLine("endwhile");
        }
        getAssigneeExecutionContext.unregisterAlias(variableName);
        return null;
    }

    @Override
    public Object visitArrayLiteral(ArrayLiteral arrayLiteral) {
        this.writer.write("[");
        List<ValueSource> values = arrayLiteral.getValues();
        if (!values.isEmpty()) {
            this.visit(values.get(0));
            for (int i = 1; i < values.size(); i++) {
                this.writer.write(", ");
                this.visit(values.get(i));
            }
        }
        return null;
    }

    @Override
    public Object visitConditionalBlock(ConditionalBlock conditionalBlock) {
        throw new RuntimeException("Not support.");
    }

    @Override
    public Object visitNotCondition(NotCondition notCondition) {
        this.writer.write("!");
        return super.visitNotCondition(notCondition);
    }

    @Override
    public Object visitAndOrCondition(AndOrCondition andOrCondition) {
        List<Condition> conditions = andOrCondition.getConditions();
        this.writeCondition(conditions.get(0));
        for (int i = 1; i < conditions.size(); i++) {
            this.writer.write(String.format(" %s ", andOrCondition.getOperator().toString()));
            this.writeCondition(conditions.get(i));
        }
        return null;
    }

    private void writeCondition(Condition condition) {
        if (condition instanceof AndOrCondition) {
            this.writer.write("(");
            this.visit(condition);
            this.writer.write(")");
        } else {
            this.visit(condition);
        }
    }

    @Override
    public Object visitCompareCondition(CompareCondition compareCondition) {
        this.visit(compareCondition.getLeft());
        this.writer.write(String.format(" %s ", compareCondition.getComparison().toString()));
        this.visit(compareCondition.getRight());
        return null;
    }
}
