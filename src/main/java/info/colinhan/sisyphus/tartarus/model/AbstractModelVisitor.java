package info.colinhan.sisyphus.tartarus.model;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractModelVisitor<T> implements ModelVisitor<T> {
    @Override
    public T visit(Node node) {
        return node.accept(this);
    }

    @Override
    public T visitAction(Action action) {
        AtomicReference<T> result = new AtomicReference<>(null);
        action.getParameters().forEach((__, np) -> {
            result.set(this.visit(np));
        });
        return result.get();
    }

    @Override
    public T visitAndOrCondition(AndOrCondition andOrCondition) {
        AtomicReference<T> result = new AtomicReference<>(null);
        for (Condition condition : andOrCondition.getConditions()) {
            result.set(this.visit(condition));
        }
        return result.get();
    }

    @Override
    public T visitArrayLiteral(ArrayLiteral arrayLiteral) {
        AtomicReference<T> result = new AtomicReference<>(null);
        for (ValueSource value : arrayLiteral.getValues()) {
            result.set(this.visit(value));
        }
        return result.get();
    }

    @Override
    public T visitBlock(Block block) {
        AtomicReference<T> result = new AtomicReference<>(null);
        for (Statement statement : block.getStatements()) {
            result.set(this.visit(statement));
        }
        return result.get();
    }

    @Override
    public T visitCompareCondition(CompareCondition compareCondition) {
        AtomicReference<T> result = new AtomicReference<>(null);
        result.set(this.visit(compareCondition.getLeft()));
        result.set(this.visit(compareCondition.getRight()));
        return result.get();
    }

    @Override
    public T visitConditionalBlock(ConditionalBlock conditionalBlock) {
        AtomicReference<T> result = new AtomicReference<>(null);
        result.set(this.visit(conditionalBlock.getCondition()));
        result.set(this.visit(conditionalBlock.getBlock()));
        return result.get();
    }

    @Override
    public T visitFlow(Flow flow) {
        return visit(flow.getBlock());
    }

    @Override
    public T visitIfStatement(IfStatement ifStatement) {
        AtomicReference<T> result = new AtomicReference<>(null);
        for (ConditionalBlock thenBlock : ifStatement.getThenBlocks()) {
            result.set(this.visit(thenBlock));
        }
        if (ifStatement.getElseBlock() != null) {
            result.set(this.visit(ifStatement.getElseBlock()));
        }
        return result.get();
    }

    @Override
    public T visitLiteral(Literal literal) {
        return null;
    }

    @Override
    public T visitNotCondition(NotCondition notCondition) {
        return visit(notCondition.getCondition());
    }

    @Override
    public T visitReference(Reference reference) {
        return null;
    }

    @Override
    public T visitTemplateString(TemplateString templateString) {
        AtomicReference<T> result = new AtomicReference<>(null);
        for (TemplateNode node : templateString.getNodes()) {
            result.set(this.visit(node));
        }
        return result.get();
    }

    @Override
    public T visitWhileStatement(WhileStatement whileStatement) {
        AtomicReference<T> result = new AtomicReference<>(null);
        result.set(this.visit(whileStatement.getArraySource()));
        result.set(this.visit(whileStatement.getBlock()));
        return result.get();
    }
}
