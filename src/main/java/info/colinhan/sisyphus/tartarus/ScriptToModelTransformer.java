package info.colinhan.sisyphus.tartarus;

import info.colinhan.sisyphus.tartarus.model.*;
import info.colinhan.sisyphus.tartarus.parser.TartarusParser;
import info.colinhan.sisyphus.tartarus.parser.TartarusParserBaseVisitor;
import info.colinhan.sisyphus.tartarus.runtime.NormalizeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ScriptToModelTransformer extends TartarusParserBaseVisitor<Node> {
    @Override
    public Node visitDiagram(TartarusParser.DiagramContext ctx) {
        Flow flow = new Flow();
        if (ctx.block() != null) {
            flow.setBlock((Block) visitBlock(ctx.block()));
        } else {
            flow.setBlock(new Block());
        }
        flow.accept(new NormalizeVisitor());
        return flow;
    }

    @Override
    public Node visitBlock(TartarusParser.BlockContext ctx) {
        Block block = new Block();
        for (TartarusParser.StatementContext statementContext : ctx.statement()) {
            block.addStatement((Statement)visitStatement(statementContext));
        }
        return block;
    }

    @Override
    public Node visitAction(TartarusParser.ActionContext ctx) {
        Action action = new Action();
        TartarusParser.ActionDefinitionContext actionDefinitionContext = ctx.actionDefinition();
        action.setName(actionDefinitionContext.ActionName().getText());
        if (actionDefinitionContext.positionedParameter() != null) {
            action.setPositionedParameter((TemplateString)this.visitLiteral(actionDefinitionContext.positionedParameter().literal()));
        }
        for (TartarusParser.NamedParameterContext namedParameterContext : actionDefinitionContext.namedParameter()) {
            action.setNamedParameter(
                    namedParameterContext.ParameterName().getText(),
                    (TemplateString)this.visit(namedParameterContext.literal())
            );
        }
        return action;
    }

    @Override
    public Node visitLiteral(TartarusParser.LiteralContext ctx) {
        TemplateString result = new TemplateString();
        for (ParseTree child : ctx.children) {
            if (child instanceof TartarusParser.LiteralAtomContext) {
                result.addNode((TemplateNode) this.visitLiteralAtom((TartarusParser.LiteralAtomContext)child));
            } else if (child instanceof TerminalNode) {
                Literal literalStr = new Literal();
                literalStr.setType(LiteralType.STRING);
                literalStr.setValue(child.getText());
                result.addNode(literalStr);
            }
        }
        if (result.getNodes().size() > 0) {
            TemplateNode first = result.getNodes().get(0);
            if (first instanceof Literal) {
                Literal firstLiteral = (Literal) first;
                firstLiteral.setType(LiteralType.STRING);
                firstLiteral.setValue(firstLiteral.getValue().replaceAll("^\\s+", ""));
            }
            TemplateNode last = result.getNodes().get(result.getNodes().size() - 1);
            if (last instanceof Literal) {
                Literal lastLiteral = (Literal) last;
                lastLiteral.setType(LiteralType.STRING);
                lastLiteral.setValue(lastLiteral.getValue().replaceAll("\\s+$", ""));
            }
        }
        return result;
    }

    @Override
    public Node visitCompReferenceInLiteral(TartarusParser.CompReferenceInLiteralContext ctx) {
        Reference reference = new Reference();
        reference.setVariableName(ctx.CompReferenceId().getText());
        if (ctx.CompReferenceDefaultValue() != null) {
            reference.setDefaultValue(ctx.CompReferenceDefaultValue().getText());
        }
        return reference;
    }

    @Override
    public Node visitReferenceInLiteral(TartarusParser.ReferenceInLiteralContext ctx) {
        Reference result = new Reference();
        result.setType(ReferenceType.fromValue(ctx.StartOfRefInLiteral().getText()));
        result.setVariableName(ctx.ReferenceId().getText());
        return result;
    }

    @Override
    public Node visitWhileStatement(TartarusParser.WhileStatementContext ctx) {
        WhileStatement result = new WhileStatement(false);
        result.setVariableName(ctx.Id().getText());
        result.setArraySource((ArraySource) this.visit(ctx.arrayExpression()));
        result.setBlock((Block) this.visit(ctx.block()));
        return result;
    }

    @Override
    public Node visitParallelStatement(TartarusParser.ParallelStatementContext ctx) {
        WhileStatement result = new WhileStatement(true);
        result.setVariableName(ctx.Id().getText());
        result.setArraySource((ArraySource) this.visit(ctx.arrayExpression()));
        result.setBlock((Block) this.visit(ctx.block()));
        return result;
    }

    @Override
    public Node visitArray(TartarusParser.ArrayContext ctx) {
        ArrayLiteral result = new ArrayLiteral();
        for (TartarusParser.ExpressionValueContext expressionValueContext : ctx.expressionValue()) {
            result.addValue((ValueSource) this.visit(expressionValueContext));
        }
        return result;
    }

    @Override
    public Node visitIfStatement(TartarusParser.IfStatementContext ctx) {
        IfStatement result = new IfStatement();

        ConditionalBlock thenBlock = new ConditionalBlock();
        thenBlock.setCondition((Condition) this.visit(ctx.logicalExpression()));
        thenBlock.setBlock((Block) this.visit(ctx.block()));
        result.addThenBlock(thenBlock);

        for (TartarusParser.ElseifStatementContext elseifStatementContext : ctx.elseifStatement()) {
            result.addThenBlock((ConditionalBlock) this.visit(elseifStatementContext));
        }

        if (ctx.elseStatement() != null) {
            result.setElseBlock((Block) this.visit(ctx.elseStatement()));
        }

        return result;
    }

    @Override
    public Node visitElseifStatement(TartarusParser.ElseifStatementContext ctx) {
        ConditionalBlock result = new ConditionalBlock();

        result.setCondition((Condition) this.visit(ctx.logicalExpression()));
        result.setBlock((Block) this.visit(ctx.block()));
        return result;
    }

    @Override
    public Node visitOrExpression(TartarusParser.OrExpressionContext ctx) {
        if (ctx.andExpression().size() > 1) {
            AndOrCondition result = new AndOrCondition(AndOr.OR);
            for (TartarusParser.AndExpressionContext andExpressionContext : ctx.andExpression()) {
                result.addCondition((Condition) this.visit(andExpressionContext));
            }
            return result;
        } else {
            return this.visit(ctx.andExpression(0));
        }
    }

    @Override
    public Node visitAndExpression(TartarusParser.AndExpressionContext ctx) {
        if (ctx.equalityExpression().size() > 1) {
            AndOrCondition result = new AndOrCondition(AndOr.OR);
            for (TartarusParser.EqualityExpressionContext andExpressionContext : ctx.equalityExpression()) {
                result.addCondition((Condition) this.visit(andExpressionContext));
            }
            return result;
        } else {
            return this.visit(ctx.equalityExpression(0));
        }
    }

    @Override
    public Node visitEqualityExpression(TartarusParser.EqualityExpressionContext ctx) {
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
    public Node visitPrimaryExpression(TartarusParser.PrimaryExpressionContext ctx) {
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
    public Node visitConstant(TartarusParser.ConstantContext ctx) {
        Literal literal= new Literal();
        String text = ctx.getText();
        if (text.startsWith("\"")) {
            literal.setType(LiteralType.STRING);
            literal.setValue(text.substring(1, text.length() - 1));
        } else {
            literal.setType(LiteralType.NUMBER);
            literal.setValue(text);
        }
        return literal;
    }

    @Override
    public Node visitSimpleReference(TartarusParser.SimpleReferenceContext ctx) {
        Reference result = new Reference();
        result.setType(ReferenceType.fromValue(ctx.StartOfRef().getText()));
        result.setVariableName(ctx.ReferenceId().getText());
        return result;
    }

    @Override
    public Node visitCompReference(TartarusParser.CompReferenceContext ctx) {
        Reference result = new Reference();
        result.setVariableName(ctx.CompReferenceId().getText());
        if (ctx.CompReferenceDefaultValue() != null) {
            result.setDefaultValue(ctx.CompReferenceDefaultValue().getText());
        }
        return result;
    }
}
