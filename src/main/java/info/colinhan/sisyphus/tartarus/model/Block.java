package info.colinhan.sisyphus.tartarus.model;

import java.util.ArrayList;
import java.util.List;

public class Block extends AbstractNode implements Node {
    private List<Statement> statements = new ArrayList<>();

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    public void addStatement(Statement statement) {
        this.statements.add(statement);
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitBlock(this);
    }
}
