package info.colinhan.sisyphus.model;

import java.util.ArrayList;
import java.util.List;

public class Block implements Node {
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
}
