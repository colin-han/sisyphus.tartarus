package info.colinhan.sisyphus.tartarus;

import info.colinhan.sisyphus.model.ReferenceType;
import info.colinhan.sisyphus.tartarus.exceptions.TartarusExecutionException;
import info.colinhan.sisyphus.tartarus.model.*;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;
import java.util.UUID;

public class ModelToBPMNTransformer extends AbstractModelVisitor<Void> {
    private final int flowVersionId;
    private final String flowName;
    private final XMLStreamWriter writer;

    private final TFormalExpressionTransformer expressionTransformer = new TFormalExpressionTransformer();

    private static class ConnectionInfo {
        private final String nodeId;
        private final Condition condition;
        private final String pathId;

        public ConnectionInfo(String nodeId) {
            this.nodeId = nodeId;
            this.condition = null;
            this.pathId = "path-" + UUID.randomUUID();
        }

        public ConnectionInfo(String nodeId, Condition condition) {
            this.nodeId = nodeId;
            this.condition = condition;
            this.pathId = "path-" + UUID.randomUUID();
        }

        public ConnectionInfo(String nodeId, String pathId) {
            this.nodeId = nodeId;
            this.condition = null;
            this.pathId = pathId;
        }

        public String getNodeId() {
            return nodeId;
        }

        public boolean hasCondition() {
            return this.condition != null;
        }
        public Condition getCondition() {
            return condition;
        }

        public String getPathId() {
            return pathId;
        }
    }

    private ConnectionInfo lastConnection;

    public ModelToBPMNTransformer(int flowVersionId, String flowName, Writer writer) {
        this.flowVersionId = flowVersionId;
        this.flowName = flowName;
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            this.writer = factory.createXMLStreamWriter(writer);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Void visitFlow(Flow flow) {
        try {
            writer.writeStartDocument("UTF-8", "1.0");

            writer.writeStartElement("definitions");
            writer.setDefaultNamespace("http://www.omg.org/spec/BPMN/20100524/MODEL");
            writer.writeDefaultNamespace("http://www.omg.org/spec/BPMN/20100524/MODEL");
            writer.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            writer.writeNamespace("activiti", "http://activiti.org/bpmn");
            writer.writeNamespace("bpmndi", "http://www.omg.org/spec/BPMN/20100524/DI");
            writer.writeAttribute("typeLanguage", "http://www.w3.org/2001/XMLSchema");
            writer.writeAttribute("expressionLanguage", "http://www.w3.org/1999/XPath");
            writer.writeAttribute("targetNamespace", "http://www.activiti.org/processdef");

            writer.writeStartElement("process");
            writer.writeAttribute("id", "flow-" + flowVersionId);
            writer.writeAttribute("name", flowName);
            writer.writeAttribute("isExecutable", "true");

            writer.writeStartElement("startEvent");
            writer.writeAttribute("id", "start");
            writer.writeAttribute("name", "Start");
            writer.writeEndElement();

            this.lastConnection = new ConnectionInfo("start");

            this.visit(flow.getBlock());

            writeSequenceFLow(this.lastConnection, "end");

            writer.writeStartElement("endEvent");
            writer.writeAttribute("id", "end");
            writer.writeAttribute("name", "End");
            writer.writeEndElement(); // endEvent

            writer.writeEndElement(); // process

            writer.writeStartElement("http://www.omg.org/spec/BPMN/20100524/DI", "BPMNDiagram");
            writer.writeAttribute("id", "diag-" + flowVersionId);

            writer.writeStartElement("http://www.omg.org/spec/BPMN/20100524/DI", "BPMNPlane");
            writer.writeAttribute("bpmnElement", "flow-" + flowVersionId);
            writer.writeAttribute("id", "plane-" + flowVersionId);
            writer.writeEndElement(); // BPMNPlane

            writer.writeEndElement(); // BPMNDiagram

            writer.writeEndElement(); // definitions

            writer.writeEndDocument();

        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Void visitAction(Action action) {
        try {
            String currentId = "act-" + action.getUUID();

            writeSequenceFLow(this.lastConnection, currentId);

            writer.writeStartElement("userTask");
            writer.writeAttribute("id", currentId);
            if (action.getNamedParameters().containsKey("by")) {
                TemplateString by = action.getNamedParameters().get("by");
                if (by.getNodes().size() != 1) {
                    throw TartarusExecutionException.withWrapper("'by' parameter should be a ref to user or group!");
                }
                TemplateNode templateNode = by.getNodes().get(0);
                if (templateNode instanceof Reference) {
                    Reference ref = (Reference) templateNode;
                    if (ref.getType() == ReferenceType.RULE) {
                        writer.writeAttribute("http://activiti.org/bpmn", "candidateGroups", ref.getVariableName());
                    } else if (ref.getType() == ReferenceType.VARIABLE) {
                        writer.writeAttribute("http://activiti.org/bpmn", "assignee", "${" + ref.getVariableName() + "}");
                    } else {
                        throw TartarusExecutionException.withWrapper("Env type reference is not supported in 'by' parameter!");
                    }
                }
            }
            writer.writeEndElement();

            this.lastConnection = new ConnectionInfo(currentId);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void writeSequenceFLow(ConnectionInfo connectionInfo, String targetId) throws XMLStreamException {
        writer.writeStartElement("sequenceFlow");
        writer.writeAttribute("id", connectionInfo.getPathId());
        writer.writeAttribute("sourceRef", connectionInfo.getNodeId());
        writer.writeAttribute("targetRef", targetId);
        if (connectionInfo.hasCondition()) {
            writeConditionExpression(connectionInfo.getCondition());
        }
        writer.writeEndElement();
    }

    @Override
    public Void visitIfStatement(IfStatement ifStatement) {
        try {
            String currentId = "if-" + ifStatement.getUUID();
            String endNodeId = "endif-" + ifStatement.getUUID();
            String elsePathId = "path-" + UUID.randomUUID();

            writeSequenceFLow(this.lastConnection, currentId);

            writer.writeStartElement("exclusiveGateway");
            writer.writeAttribute("id", currentId);
            writer.writeAttribute("default", elsePathId);
            writer.writeEndElement();

            for (ConditionalBlock thenBlock : ifStatement.getThenBlocks()) {
                this.lastConnection = new ConnectionInfo(currentId, thenBlock.getCondition());
                this.visit(thenBlock.getBlock());
                writeSequenceFLow(this.lastConnection, endNodeId);
            }

            Block elseBlock = ifStatement.getElseBlock();
            this.lastConnection = new ConnectionInfo(currentId, elsePathId);
            if (elseBlock != null) {
                this.visit(elseBlock);
            }
            writeSequenceFLow(this.lastConnection, endNodeId);

            writer.writeStartElement("inclusiveGateway");
            writer.writeAttribute("id", endNodeId);
            writer.writeEndElement();

            this.lastConnection = new ConnectionInfo(endNodeId);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void writeConditionExpression(Condition condition) throws XMLStreamException {
        writer.writeStartElement("conditionExpression");
        writer.writeAttribute("http://www.w3.org/2001/XMLSchema-instance", "type", "tFormalExpression");

        writer.writeCData("${" + expressionTransformer.visit(condition) + "}");

        writer.writeEndElement(); // conditionExpression
    }
}
