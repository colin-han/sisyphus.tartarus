package info.colinhan.sisyphus;

import info.colinhan.sisyphus.model.*;
import info.colinhan.sisyphus.parser.SisyphusParser;
import info.colinhan.sisyphus.parser.SisyphusParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ModelTransformer extends SisyphusParserBaseVisitor<Node> {
    @Override
    public Node visitDiagram(SisyphusParser.DiagramContext ctx) {
        Flow flow = new Flow();
        flow.setBlock((Block)visitBlock(ctx.block()));
        return flow;
    }

    @Override
    public Node visitBlock(SisyphusParser.BlockContext ctx) {
        Block block = new Block();
        for (SisyphusParser.StatementContext statementContext : ctx.statement()) {
            block.addStatement((Statement)visitStatement(statementContext));
        }
        return block;
    }

    @Override
    public Node visitAction(SisyphusParser.ActionContext ctx) {
        Action action = new Action();
        SisyphusParser.ActionDefinitionContext actionDefinitionContext = ctx.actionDefinition();
        action.setName(actionDefinitionContext.ActionName().getText());
        if (actionDefinitionContext.positionedParameter() != null) {
            action.setPositionedParameter((TemplateString)this.visitLiteral(actionDefinitionContext.positionedParameter().literal()));
        }
        for (SisyphusParser.NamedParameterContext namedParameterContext : actionDefinitionContext.namedParameter()) {
            action.setNamedParameter(
                    namedParameterContext.ParameterName().getText(),
                    (TemplateString)this.visit(namedParameterContext.literal())
            );
        }
        return action;
    }

    @Override
    public Node visitLiteral(SisyphusParser.LiteralContext ctx) {
        TemplateString result = new TemplateString();
        for (ParseTree child : ctx.children) {
            if (child instanceof SisyphusParser.LiteralAtomContext) {
                result.addNode((TemplateNode) this.visitLiteralAtom((SisyphusParser.LiteralAtomContext)child));
            } else if (child instanceof TerminalNode) {
                Literal literalStr = new Literal();
                literalStr.setValue(child.getText());
                result.addNode(literalStr);
            }
        }
        return result;
    }

    @Override
    public Node visitCompReferenceInLiteral(SisyphusParser.CompReferenceInLiteralContext ctx) {
        Reference reference = new Reference();
        reference.setVariableName(ctx.CompReferenceId().getText());
        if (ctx.CompReferenceDefaultValue() != null) {
            reference.setDefaultValue(ctx.CompReferenceDefaultValue().getText());
        }
        return reference;
    }

    @Override
    public Node visitReferenceInLiteral(SisyphusParser.ReferenceInLiteralContext ctx) {
        Reference result = new Reference();
        result.setType(ReferenceType.fromValue(ctx.StartOfRefInLiteral().getText()));
        result.setVariableName(ctx.ReferenceId().getText());
        return result;
    }

    @Override
    public Node visitWhileStatement(SisyphusParser.WhileStatementContext ctx) {
        WhileStatement result = new WhileStatement();
        result.setVariableName(ctx.Id().getText());
        result.setArraySource((ArraySource) this.visit(ctx.arrayExpression()));
        result.setBlock((Block) this.visit(ctx.block()));
        return result;
    }

    @Override
    public Node visitArray(SisyphusParser.ArrayContext ctx) {
        ArrayLiteral result = new ArrayLiteral();
        for (SisyphusParser.ExpressionValueContext expressionValueContext : ctx.expressionValue()) {
            result.addValue((ValueSource) this.visit(expressionValueContext));
        }
        return result;
    }

    @Override
    public Node visitIfStatement(SisyphusParser.IfStatementContext ctx) {
        IfStatement result = new IfStatement();

        ConditionalBlock thenBlock = new ConditionalBlock();
        thenBlock.setCondition((Condition) this.visit(ctx.logicalExpression()));
        thenBlock.setBlock((Block) this.visit(ctx.block()));
        result.addThenBlock(thenBlock);

        for (SisyphusParser.ElseifStatementContext elseifStatementContext : ctx.elseifStatement()) {
            result.addThenBlock((ConditionalBlock) this.visit(elseifStatementContext));
        }

        if (ctx.elseStatement() != null) {
            result.setElseBlock((Block) this.visit(ctx.elseStatement()));
        }

        return result;
    }

    @Override
    public Node visitElseifStatement(SisyphusParser.ElseifStatementContext ctx) {
        ConditionalBlock result = new ConditionalBlock();

        result.setCondition((Condition) this.visit(ctx.logicalExpression()));
        result.setBlock((Block) this.visit(ctx.block()));
        return result;
    }

    @Override
    public Node visitOrExpression(SisyphusParser.OrExpressionContext ctx) {
        if (ctx.andExpression().size() > 1) {
            AndOrCondition result = new AndOrCondition(AndOr.OR);
            for (SisyphusParser.AndExpressionContext andExpressionContext : ctx.andExpression()) {
                result.addCondition((Condition) this.visit(andExpressionContext));
            }
            return result;
        } else {
            return this.visit(ctx.andExpression(0));
        }
    }

    @Override
    public Node visitAndExpression(SisyphusParser.AndExpressionContext ctx) {
        if (ctx.equalityExpression().size() > 1) {
            AndOrCondition result = new AndOrCondition(AndOr.OR);
            for (SisyphusParser.EqualityExpressionContext andExpressionContext : ctx.equalityExpression()) {
                result.addCondition((Condition) this.visit(andExpressionContext));
            }
            return result;
        } else {
            return this.visit(ctx.equalityExpression(0));
        }
    }

    @Override
    public Node visitEqualityExpression(SisyphusParser.EqualityExpressionContext ctx) {
        if (ctx.compare() != null) {
            CompareCondition result = new CompareCondition();
            result.setLeft((ValueSource) this.visit(ctx.comparisonExpression(0)));
            result.setRight((ValueSource) this.visit(ctx.comparisonExpression(1)));
            result.setComparison(Comparison.fromValue(ctx.compare().getText()));
            return result;
        } else {
            return this.visit(ctx.comparisonExpression(0));
        }
    }

    @Override
    public Node visitPrimaryExpression(SisyphusParser.PrimaryExpressionContext ctx) {
        if (ctx.LeftBracket() != null) {
            return this.visit(ctx.logicalExpression());
        } else if (ctx.Not() != null) {
            NotCondition result = new NotCondition();
            result.setCondition((Condition) this.visit(ctx.primaryExpression()));
            return result;
        } else {
            return this.visit(ctx.expressionValue());
        }
    }

    @Override
    public Node visitConstant(SisyphusParser.ConstantContext ctx) {
        Literal literal= new Literal();
        literal.setValue(ctx.getText());
        return literal;
    }

    @Override
    public Node visitSimpleReference(SisyphusParser.SimpleReferenceContext ctx) {
        Reference result = new Reference();
        result.setType(ReferenceType.fromValue(ctx.StartOfRef().getText()));
        result.setVariableName(ctx.ReferenceId().getText());
        return result;
    }

    @Override
    public Node visitCompReference(SisyphusParser.CompReferenceContext ctx) {
        Reference result = new Reference();
        result.setVariableName(ctx.CompReferenceId().getText());
        if (ctx.CompReferenceDefaultValue() != null) {
            result.setDefaultValue(ctx.CompReferenceDefaultValue().getText());
        }
        return result;
    }
}
