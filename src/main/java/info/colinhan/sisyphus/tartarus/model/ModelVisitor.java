package info.colinhan.sisyphus.tartarus.model;

public interface ModelVisitor<T> {
    T visit(Node node);

    T visitAction(Action action);

    T visitAndOrCondition(AndOrCondition andOrCondition);

    T visitArrayLiteral(ArrayLiteral arrayLiteral);

    T visitBlock(Block block);

    T visitCompareCondition(CompareCondition compareCondition);

    T visitConditionalBlock(ConditionalBlock conditionalBlock);

    T visitFlow(Flow flow);

    T visitIfStatement(IfStatement ifStatement);

    T visitLiteral(Literal literal);

    T visitNotCondition(NotCondition notCondition);

    T visitReference(Reference reference);

    T visitTemplateString(TemplateString templateString);

    T visitWhileStatement(WhileStatement whileStatement);
}
